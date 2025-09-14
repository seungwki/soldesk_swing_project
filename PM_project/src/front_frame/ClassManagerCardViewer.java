package front_frame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import VO.Project;
import VO.Team;
import front_ui.AutoGrowBox;
import front_ui.FolderTab;
import front_ui.ProjectRow;
import front_ui.TabSpec;
import front_ui.TabsBar;
import front_ui.TagChip; // 태그 칩 존재 가정
import front_ui.TopBar;
import front_util.Theme;

// 수업관리 카드 안의 뷰어(껍데기 화면)  // ★ 기존 주석 유지
public class ClassManagerCardViewer extends BasePage {
	//백엔드 데이터
	private Project project;
	private TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
	private boolean isClicked;
	private boolean isCreatable;
	// ── 레이아웃 상수(기존 값 유지) ─────────────────────────────────────────────
	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24; // 탭 기준 Y(탭은 이 기준 유지)
	final int boxDrop = 12; // 박스/내용물만 아래로 내림
	final int boxY = boxBaseY + boxDrop;

	final int contentX = 16, contentY = 34;
	final int contentW = boxW - contentX * 2;
	final int contentH = boxH - contentY - 16;

	// 탭
	private TabsBar tabs;
	final int tabW = 100, tabH = 28;
	final int tabBottom = boxBaseY + Theme.BORDER_THICK; // 내부 상단선
	final int tabY = tabBottom - tabH;
	private int selectedTab = 0; // ★ 추가: 현재 선택 탭

	private AutoGrowBox box;

	// 컬러 (요구안: 선택=박스 테두리색 / 비선택=흰색)
	private static final Color SELECT_COLOR = Theme.BORDER_STUDENT;
	private static final Color UNSELECT_COLOR = Color.WHITE; // ★ 추가

	// 더미 데이터
	private final List<ProjectRow> rows = new ArrayList<>(); // ★ 추가

	// Card에서 호출되는 기존 시그니처와 호환

	// 내부에서 사용(직접 테스트용)
	public ClassManagerCardViewer(Project project) {
		super(new TopBar.OnMenuClick() {
			@Override
			public void onClass() {
				DefaultFrame.getInstance(new ClassManager());
			}

			@Override
			public void onStudent() {
				DefaultFrame.getInstance(new StudentManager());
			}

			@Override
			public void onTag() {
				DefaultFrame.getInstance(new TagManager());
			}
		});
		getTopBar().selectOnly("class");

		this.project = project;
		// ── 박스(자동 확장형) ─────────────────────────────
		box = new AutoGrowBox();
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(SELECT_COLOR); // ★ 추가: 선택 색과 동일
		getContentPanel().add(box);

		// ── 탭 바(1차~5차 +) ────────────────────────────//
		//팀 가져와서 차수 별로 맵에 넣기
		for (Team team : project.getTeams2()) {
			int degree = team.getDegree();
			teamMap.computeIfAbsent(degree, k -> new ArrayList<>()).add(team);
			teamMap.get(degree).add(team);
		}
		TabSpec[] tabSpecs = new TabSpec[teamMap.keySet().size() + 1];
		ArrayList<Integer> degreeList = new ArrayList<Integer>();
		Iterator<Integer> degreeIter = teamMap.keySet().iterator();
		while (degreeIter.hasNext()) {
			degreeList.add(degreeIter.next());
		}
		degreeList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
		});
		for (int i = 0; i < degreeList.size(); i++) {
			tabSpecs[i] = new TabSpec(degreeList.get(i) + "차", UNSELECT_COLOR);
			tabSpecs[i].setDegree(String.valueOf(degreeList.get(i)));
		}
		tabSpecs[tabSpecs.length - 1] = new TabSpec("+", UNSELECT_COLOR);
		tabSpecs[tabSpecs.length - 1].setDegree("+");
		tabs = new TabsBar(tabSpecs, tabW, tabH);

		final int gap = 110;
		for (int i = 0; i < tabSpecs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}
		int tabsRight = boxX + (tabSpecs.length - 1) * gap + tabW;
		int tabsBottom = tabY + tabH;
		tabs.setBounds(0, 0, tabsRight, tabsBottom);
		getContentPanel().add(tabs);
		getContentPanel().setComponentZOrder(tabs, 0);
		getContentPanel().setComponentZOrder(box, 1);

		// 탭 선택 동작 //TODO 2
		tabs.setOnChange(idx -> {
			if (idx == tabs.getTabCount() - 1) {
				//				//차수 생성
				//				tabs.setSelectedIndex(selectedTab, true);
				handleAddNewDegree();
				return;
			}
			selectedTab = idx;
			handleTabClicked(idx);
			applyTabSelection();
		});
		tabs.setSelectedIndex(0, true); // 최초 1차 선택
		//		applyTabSelection();

		//		 ── 프로젝트 행 생성 ─────────────────────
		// 배치 기준: "앞으로는 여기 위에 배치하세요" 요청 → y = contentY - 20, gap = 20  // ★ 추가
		//		int y = contentY - 20, gapY = 20;
		//		for (int i = 0; i < project.getTeams2().size(); i++) {
		//			ProjectRow row = new ProjectRow(contentX, y, contentW, project.getTeams2().get(i).getOutput());
		//			row.setOutputTitle(row.getOutput() != null ? row.getOutput().getTitle() : "");
		//			row.setTeamTitle(project.getTeams2().get(i).getTName());
		//			//			row.setTagChips(dummyTags(i < 2 ? 4 : 0));
		//			rows.add(row);
		//			box.add(row);
		//			y += row.getPreferredHeight() + gapY;
		//		}

		box.autoGrow();
		refreshScroll();
	}

	// ★ 추가: 탭/박스 색 동기화(선택 탭=파란색, 비선택=흰색)
	private void applyTabSelection() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			FolderTab t = tabs.getTab(i);
			boolean sel = (i == selectedTab);
			t.setSelected(sel); // FolderTab은 setSelected만 사용
		}
		box.setBorderColor(SELECT_COLOR); // 박스 테두리 고정색(요구사항)
		getContentPanel().repaint();
	}

	// ★ 추가: 간단 더미 태그 (TagChip(String, Color, Color, int, int) 에 맞춤)
	//143
	//	private List<TagChip> dummyTags(int n) {
	//		List<TagChip> list = new ArrayList<>();
	//		final int h = 28; // 칩 높이 (ProjectRow의 태그 높이와 어울리게)
	//		final int minW = 60; // 최소 폭
	//		final int pad = 20; // 좌우 여백 보정
	//		final Color chipBg = new Color(0xDAE3F3); // 밝은 파랑 계열
	//		final Color chipFg = new Color(0x3A4764); // 어두운 텍스트
	//
	//		for (int i = 0; i < n; i++) {
	//			String label = "태그" + (i + 1);
	//			// 대략적 폭 계산(문자 수 기반) — 실제 폰트폭 대신 간단 계산
	//			int w = Math.max(minW, pad + label.length() * 14);
	//			list.add(new TagChip(label, chipBg, chipFg, w, h));
	//		}
	//		return list;
	//	}
	private void handleTabClicked(int idx) {
		String degreeStr = tabs.getTabSpec(idx).getDegree();
		//		if (degreeStr.equals("+")) {
		//			return;
		//		}

		int degree = Integer.parseInt(degreeStr);

		box.removeAll();
		rows.clear();

		int y = contentY - 20, gapY = 20;
		for (int i = 0; i < project.getTeams2().size(); i++) {
			Team team = project.getTeams2().get(i);
			if (degree == team.getDegree()) {
				System.out.println(team);
				ProjectRow row = new ProjectRow(contentX, y, contentW, team.getOutput());
				row.setOutputTitle(team.getOutput() != null ? team.getOutput().getTitle() : "");
				row.setTeamTitle(team.getTName());
				// ✅ 꼭 추가해보세요 (보이지 않을 수 있음)
//				row.setBounds(contentX, y, contentW, row.getPreferredHeight());
				System.out.println("check 1");
				rows.add(row);
				System.out.println("check 2");
				box.add(row);
				System.out.println("check 3");
				y += row.getPreferredHeight() + gapY;
			}
		}
		box.autoGrow();
		box.revalidate();
		box.repaint();
	}

	private void handleAddNewDegree() {//새 차수 생성
	}
}
