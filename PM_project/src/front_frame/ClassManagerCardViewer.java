package front_frame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import VO.Project;
import VO.Team;
import front_ui.AutoGrowBox;
import front_ui.FolderTab;
import front_ui.TabSpec;
import front_ui.TabsBar;
import front_ui.TagListPanel;
import front_util.Theme;

public class ClassManagerCardViewer extends BasePage {
	//백엔드 필드
	private Project project;
	TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
	boolean isClicked;
	boolean isCreatable;
	//프론트엔드 필드
	//파일 모양의 내용 표기 할 공간
	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24; // 탭 기준 Y(탭은 이 기준 유지)
	final int boxDrop = 12; // 박스/내용물만 아래로 내림
	final int boxY = boxBaseY + boxDrop;
	final int contentX = 16, contentY = 34;
	final int contentW = boxW - contentX * 2;
	final int contentH = boxH - contentY - 16;
	//탭
	private AutoGrowBox box;
	private TagListPanel studentList, outputList;
	private TabsBar tabs;
	final int tabW = 100, tabH = 28;
	final int tabBottom = boxBaseY + Theme.BORDER_THICK; // 내부 상단선
	final int tabY = tabBottom - tabH;
	private final int gap = 110;
	//색
	private static final Color BORDER_SELECTED = Theme.TAB_SELECTED;
	private static final Color BORDER_UNSELECTED = Theme.TAB_UNSELECTED;
	private static final Color TAG_LIST_BACKGROUND = Theme.TAB_UNSELECTED;
	private static final Color TEAM_NAME_BACKGROUND = Theme.TAB_UNSELECTED;
	//콘텐츠 담기는 곳
	private List<TabContentPanel> contentPanels = new ArrayList<>();

	//생성자
	public ClassManagerCardViewer(Project project) {
		//백엔드 필드
		this.project = project;
		for (int i = 1; i < 6; i++) {
			teamMap.put(i, new ArrayList<Team>());
		}
		for (int i = 0; i < project.getTeams2().size(); i++) {
			int degree = project.getTeams2().get(i).getDegree();
			teamMap.get(degree).add(project.getTeams2().get(i));
		}
		//1~5까지 조회하면서 size==0이라면 추가 가능, break
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() == 0) {
				isCreatable = true;
				break;
			}
		}

		//프론트엔드 필드
		// ── 박스(자동 확장형) ───────────────────────────── , 내용 추가 시 스크롤바 갱신
		box = new AutoGrowBox();
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(Theme.LIGHT_BORDER);
		getContentPanel().add(box);

		//로직
		initTabs(project);
		refreshScroll();
	}//생성자

	private void initTabs(Project project) {
		//차수 정렬
		Iterator<Integer> iter = teamMap.keySet().iterator();
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		while (iter.hasNext()) {
			int i = iter.next();
			if (teamMap.get(i).size() != 0) {
				//				System.out.println(i);
				arrayList.add(i);
			}
		}
		TabSpec[] tabSpecs;
		//길이가 5보다 작다면 차수 생성 버튼을 추가
		if (isCreatable) {
			tabSpecs = new TabSpec[arrayList.size() + 1];
			for (int i = 0; i < arrayList.size(); i++) {
				tabSpecs[i] = new TabSpec(arrayList.get(i) + "차", Theme.TAB_UNSELECTED);
			}
			tabSpecs[arrayList.size()] = new TabSpec("+", Theme.TAB_UNSELECTED);
			tabs = new TabsBar(tabSpecs, tabW, tabH);
		} else {//아니라면 차수만 생성
			tabSpecs = new TabSpec[arrayList.size()];
			for (int i = 0; i < arrayList.size(); i++) {
				tabSpecs[i] = new TabSpec(arrayList.get(i) + "차", Theme.TAB_UNSELECTED);
			}
			tabs = new TabsBar(tabSpecs, tabW, tabH);
		}
		//		// 탭 위치 설정
		for (int i = 0; i < tabSpecs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}

		//		int tabsRight = boxX + (tabSpecs.length - 1) * gap + tabW;
		//		int tabsBottom = tabY + tabH;
		//		tabs.setBounds(0, 0, tabsRight, tabsBottom);

		getContentPanel().add(tabs);
		getContentPanel().setComponentZOrder(tabs, 0);
		getContentPanel().setComponentZOrder(box, 1);

		contentPanels.clear();
		box.removeAll();

		for (int i = 0; i < arrayList.size(); i++) {
			TabContentPanel panel = createContentPanel((i + 1) + "차 내용");
			contentPanels.add(panel);
			box.add(panel);
		}
		//		// 탭 클릭 이벤트 처리
		//		tabs.setOnChange(e -> {
		//			if (e == tabs.getTabCount() - 1) {
		//				addNewTab();
		//				return;
		//			}
		//			showTabContent(e);
		//			updateTabColors(e);
		//		});
	}//initTabs

	//	private void addNewTab() {
	//		int currentCount = tabs.getTabCount() - 1;
	//		String newTabTitle = (currentCount + 1) + "차";
	//		TabSpec[] newTabs;
	//		if (currentCount < 4) {
	//			newTabs = new TabSpec[currentCount + 2];
	//		} else {
	//			newTabs = new TabSpec[currentCount + 1];
	//		}
	//		for (int i = 0; i < currentCount; i++) {
	//			newTabs[i] = tabs.getTabSpec(i);
	//		} //이럴거면 arrayList를 썼어야지
	//		newTabs[currentCount] = new TabSpec(newTabTitle, Theme.TAB_UNSELECTED);
	//		if (currentCount < 4) {
	//			newTabs[currentCount + 1] = new TabSpec("+", Theme.TAB_UNSELECTED);
	//		}
	//
	//		getContentPanel().remove(tabs);
	//
	//		tabs = new TabsBar(newTabs, tabW, tabH);
	//		for (int i = 0; i < newTabs.length; i++) {
	//			tabs.setTabLocation(i, boxX + i * gap, tabY);
	//		}
	//		int tabsRight = boxX + (newTabs.length - 1) * gap + tabW;
	//		tabs.setBounds(0, 0, tabsRight, tabY + tabH);
	//
	//		getContentPanel().add(tabs);
	//		getContentPanel().setComponentZOrder(tabs, 0);
	//		getContentPanel().setComponentZOrder(box, 1);
	//
	//		TabContentPanel newPanel = createContentPanel(newTabTitle + " 내용");
	//		contentPanels.add(newPanel);
	//		box.add(newPanel);
	//
	//		showTabContent(currentCount);
	//
	//		tabs.setOnChange(selected -> {
	//			if (selected == tabs.getTabCount() - 1) {
	//				addNewTab();
	//				return;
	//			}
	//			showTabContent(selected);
	//			updateTabColors(selected);
	//		});
	//
	//		tabs.setSelectedIndex(currentCount, true);
	//
	//		updateTabColors(currentCount);
	//
	//		getContentPanel().revalidate();
	//		getContentPanel().repaint();
	//	}//addNewTab

	//	private void showTabContent(int index) {
	//		for (int i = 0; i < contentPanels.size(); i++) {
	//			contentPanels.get(i).setVisible(i == index);
	//		}
	//		getContentPanel().repaint();
	//}//showTabContent

	private TabContentPanel createContentPanel(String labelText) {
		TabContentPanel panel = new TabContentPanel();
		panel.setBounds(contentX, 10, contentW, contentH - 20); // box 안에 적절히 위치시킴
		panel.setVisible(false);
		// 배경색과 테두리로 내용 구분
		panel.setBackground(Theme.TAB_OFF_BG);
		panel.setOpaque(true);
		// 필요시 labelText를 사용해서 내용 설정 가능
		return panel;
	}//createContentPanel

	//	// 선택된 탭만 노란색, 나머지 회색으로 색상 변경
	//	private void updateTabColors(int selectedIndex) {
	//		for (int i = 0; i < tabs.getTabCount(); i++) {
	//			FolderTab tab = tabs.getTab(i);
	//			tab.setSelected(i == selectedIndex);
	//		}
	//		tabs.repaint();
	//	}//updateTabColors
}