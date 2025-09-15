package front_frame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import VO.Project;
import VO.Team;
import front_ui.*;
import front_util.Theme;

// 수업관리 카드 안의 뷰어(껍데기 화면)  // ★ 기존 주석 유지
public class ClassManagerCardViewer extends BasePage {
	//백엔드 데이터
	private Project project;
	private TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
	// ── 레이아웃 상수(기존 값 유지) ─────────────────────────────────────────────

	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24, boxDrop = 12, boxY = boxBaseY + boxDrop;
	final int contentX = 16, contentY = 34;
	final int contentW = boxW - contentX * 2;
	final int contentH = boxH - contentY - 16;//차이 있음

	// 탭
	private AutoGrowBox box;
	private TabsBar tabs;
	final int tabW = 100, tabH = 28;
	final int tabBottom = boxBaseY + Theme.BORDER_THICK;
	final int tabY = tabBottom - tabH;
	//	private final int gap = 110;
	int selectedTab = 0;

	// 팔레트
	private static final Color SELECT_COLOR = new Color(0xAFC2F5);
	private static final Color UNSELECT_COLOR = Color.WHITE;

	// 행들
	final List<ProjectRow> rows = new ArrayList<>();

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
		// 파일 박스
		box = new AutoGrowBox();
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(SELECT_COLOR);
		box.setBackground(SELECT_COLOR);
		getContentPanel().add(box);

		// 탭 바
		//팀 가져와서 차수 별로 맵에 넣기
		for (Team team : project.getTeams2()) {
			int degree = team.getDegree();
			teamMap.computeIfAbsent(degree, k -> new ArrayList<>()).add(team);
			//			teamMap.get(degree).add(team);
		}
		//		TabSpec[] tabSpecs = new TabSpec[degreeList.size() + 1];
		ArrayList<Integer> degreeList = new ArrayList<Integer>();
		Iterator<Integer> degreeIter = teamMap.keySet().iterator();
		while (degreeIter.hasNext()) {
			degreeList.add(degreeIter.next());
		}
		TabSpec[] tabSpecs = new TabSpec[degreeList.size() + 1];
		degreeList.sort(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2;
			}
			//			TabSpec[] tabSpecs = new TabSpec[teamMap.keySet().size() + 1];
		});
		for (int i = 0; i < degreeList.size(); i++) {
			tabSpecs[i] = new TabSpec(degreeList.get(i) + "차", UNSELECT_COLOR);
			tabSpecs[i].setDegree(String.valueOf(degreeList.get(i)));
		}
		tabSpecs[tabSpecs.length - 1] = new TabSpec("+", UNSELECT_COLOR);
		tabSpecs[tabSpecs.length - 1].setDegree("+");
		tabs = new TabsBar(tabSpecs, tabW, tabH);
		getContentPanel().add(tabs);
		tabs.setLayout(null);

		final int gap = 110;
		for (int i = 0; i < tabSpecs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}
		int tabsRight = boxX + (tabSpecs.length - 1) * gap + tabW;
		int tabsBottom = tabY + tabH;
		tabs.setBounds(0, 0, tabsRight, tabsBottom);
		getContentPanel().setComponentZOrder(box, 1);
		getContentPanel().setComponentZOrder(tabs, 0);

		// 탭 클릭
		tabs.setOnChange(idx -> {
			if (idx == tabs.getTabCount() - 1) {
				handleAddNewDegree();
				return;
			}
			selectedTab = idx;
			handleTabClicked(idx);
			applyTabSelection();
		});
		tabs.setSelectedIndex(0, true);
		//		tabs.setVisible(true);
		applyTabSelection();
		box.autoGrow();
		refreshScroll();
	}//생성자

	// 선택 탭 색 반영
	private void applyTabSelection() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			FolderTab t = tabs.getTab(i);
			boolean sel = (i == selectedTab);
			t.setTabColors(sel ? SELECT_COLOR : null, !sel ? UNSELECT_COLOR : null, SELECT_COLOR, null);
			t.setSelected(sel);
		}
		box.setBorderColor(SELECT_COLOR);
		box.setBackground(SELECT_COLOR);
		tabs.repaint();
	}//applyTabSelection

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
						BasePage.changePage(new TeamDetailViewer(team, selectedTab));
					}
				});
				y += row.getPreferredHeight() + gapY;
			} //if
		}
		ProjectRow row = new ProjectRow(contentX, y, contentW, null);
		row.setOutputTitle("+");
		rows.add(row);
		box.add(row);
		y += row.getPreferredHeight() + gapY;
		box.autoGrow();
		box.revalidate();
		box.repaint();
	}//handleTabClicked

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
		final int gap = 110;
		tabs = new TabsBar(newSpecs, tabW, tabH);
		System.out.println(newSpecs.length);
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
		getContentPanel().setComponentZOrder(tabs, 1);
		getContentPanel().setComponentZOrder(box, 0);

		getContentPanel().revalidate();
		getContentPanel().repaint();
	}//handleAddNewDegree

}
