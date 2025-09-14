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
				handleAddNewDegree();
				return;
			}
			selectedTab = idx;
			handleTabClicked(idx);
			applyTabSelection();
		});
		tabs.setSelectedIndex(0, true); // 최초 1차 선택
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
				ProjectRow row = new ProjectRow(contentX, y, contentW, team.getOutput());
				row.setOutputTitle(team.getOutput() != null ? team.getOutput().getTitle() : "");
				row.setTeamTitle(team.getTName());
				row.setTeam(team);
				rows.add(row);
				box.add(row);
				row.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (team.getOutput() == null)
							return;
						BasePage.changePage(new TeamDetailViewer(team));
					}
				});
				y += row.getPreferredHeight() + gapY;
			}
		}
		ProjectRow row = new ProjectRow(contentX, y, contentW, null);
		row.setOutputTitle("+");
		rows.add(row);
		box.add(row);
		y += row.getPreferredHeight() + gapY;
		box.autoGrow();
		box.revalidate();
		box.repaint();
	}

	//새 차수 생성
	private void handleAddNewDegree() {
		// 현재 차수 수
		int currentCount = teamMap.size();

		// 최대 5차까지 허용
		if (currentCount >= 5) {
			return;
		}

		// 새 차수 번호 = 가장 큰 key + 1
		int newDegree = teamMap.isEmpty() ? 1 : teamMap.lastKey() + 1;

		// 빈 팀 리스트로 추가
		teamMap.put(newDegree, new ArrayList<>());

		// 차수 리스트 새로 구성
		List<Integer> degreeList = new ArrayList<>(teamMap.keySet());
		degreeList.sort(Integer::compareTo);

		// "+" 탭 포함 여부 결정
		boolean addPlusTab = degreeList.size() < 5;

		// 탭 스펙 구성
		int specCount = degreeList.size() + (addPlusTab ? 1 : 0);
		TabSpec[] newSpecs = new TabSpec[specCount];

		for (int i = 0; i < degreeList.size(); i++) {
			newSpecs[i] = new TabSpec(degreeList.get(i) + "차", UNSELECT_COLOR);
			newSpecs[i].setDegree(String.valueOf(degreeList.get(i)));
		}

		if (addPlusTab) {
			newSpecs[specCount - 1] = new TabSpec("+", UNSELECT_COLOR);
			newSpecs[specCount - 1].setDegree("+");
		}

		// 기존 탭 제거
		getContentPanel().remove(tabs);

		// 새 TabsBar 생성
		tabs = new TabsBar(newSpecs, tabW, tabH);
		final int gap = 110;
		for (int i = 0; i < newSpecs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}
		int tabsRight = boxX + (newSpecs.length - 1) * gap + tabW;
		int tabsBottom = tabY + tabH;
		tabs.setBounds(0, 0, tabsRight, tabsBottom);

		// 탭 클릭 콜백 재등록
		tabs.setOnChange(idx -> {
			if (addPlusTab && idx == tabs.getTabCount() - 1) {
				// 마지막 탭이 "+"일 경우
				handleAddNewDegree();
				return;
			}
			selectedTab = idx;
			handleTabClicked(idx);
			applyTabSelection();
		});

		// 새로 추가된 차수를 자동 선택
		int newTabIndex = degreeList.indexOf(newDegree);
		tabs.setSelectedIndex(newTabIndex, true);
		selectedTab = newTabIndex;
		handleTabClicked(newTabIndex);
		applyTabSelection();

		// 새 탭 적용
		getContentPanel().add(tabs);
		getContentPanel().setComponentZOrder(tabs, 0);
		getContentPanel().setComponentZOrder(box, 1);

		getContentPanel().revalidate();
		getContentPanel().repaint();
	}//handleAddNewDegree

}