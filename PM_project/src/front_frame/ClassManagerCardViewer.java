package front_frame;

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
import front_util.Theme;

public class ClassManagerCardViewer extends BasePage {

	//백엔드 필드
	private Project project;
	TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
	boolean isClicked;
	boolean isCreatable;

	private AutoGrowBox box;
	private TabsBar tabs;
	private List<TabContentPanel> contentPanels = new ArrayList<>();

	private final int boxX = 10, boxW = 770, boxH = 380;
	private final int tabW = 100, tabH = 30;
	private final int tabY = 20;

	private final int contentX = 15;
	private final int contentY = tabY + tabH + 5; // 탭 바로 아래 5px 여백
	private final int contentW = boxW - 2 * contentX;
	private final int contentH = 350;

	private final int gap = 110;

	public ClassManagerCardViewer(Project project) {//생성자
		//백엔드 필드
		boolean isClicked = false;
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

		// ── 박스 생성 (내용 영역) ─────────────────────
		box = new AutoGrowBox();
		box.setBounds(boxX, contentY, boxW, boxH);
		box.setBorderColor(Theme.LIGHT_BORDER); // 연한 테두리
		box.setBackground(Theme.TAB_UNSELECTED); // 연한 배경색 (탭 비선택 배경색과 동일)
		box.setOpaque(true);
		getContentPanel().add(box);
		// 탭 생성
		initTabs(project);
		// ── 뒤로가기 버튼 ─────────────────────
		//		JButton backButton = new JButton("<");
		//		backButton.setBounds(20, contentY + boxH + 20, 120, 30);
		//		getContentPanel().add(backButton);
		//		backButton.addActionListener(e -> {
		//			DefaultFrame.getInstance(new ClassManager());
		//		});
		box.autoGrow();
		refreshScroll();
	}//생성자

	private void initTabs(Project project) {
		//차수 정렬
		Iterator<Integer> iter = teamMap.keySet().iterator();
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		while (iter.hasNext()) {
			int i = iter.next();
			if (teamMap.get(i).size() != 0) {
				System.out.println(i);
				arrayList.add(i);
			}
		}
		TabSpec[] tabSpecs;
		if (arrayList.size() < 5) {
			tabSpecs = new TabSpec[arrayList.size() + 1];
			for (int i = 0; i < arrayList.size(); i++) {
				tabSpecs[i] = new TabSpec(arrayList.get(i) + "차", Theme.TAB_UNSELECTED);
			}
			tabSpecs[arrayList.size()] = new TabSpec("+", Theme.TAB_OFF_BG);
			tabs = new TabsBar(tabSpecs, tabW, tabH);
		} else {
			tabSpecs = new TabSpec[arrayList.size()];
			for (int i = 0; i < arrayList.size(); i++) {
				tabSpecs[i] = new TabSpec(arrayList.get(i) + "차", Theme.TAB_UNSELECTED);
			}
			tabs = new TabsBar(tabSpecs, tabW, tabH);
		}
		//

		// 탭 위치 설정
		for (int i = 0; i < tabSpecs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}

		int tabsRight = boxX + (tabSpecs.length - 1) * gap + tabW;
		int tabsBottom = tabY + tabH;
		tabs.setBounds(0, 0, tabsRight, tabsBottom);

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

		//		showTabContent(0);

		// 탭 클릭 이벤트 처리
		tabs.setOnChange(e -> {
			if (e == tabs.getTabCount() - 1) {
				addNewTab();
				return;
			}
			showTabContent(e);
			updateTabColors(e);
		});
	}//initTabs

	private void addNewTab() {
		int currentCount = tabs.getTabCount() - 1;
		String newTabTitle = (currentCount + 1) + "차";
		TabSpec[] newTabs;
		if (currentCount < 4) {
			newTabs = new TabSpec[currentCount + 2];
		} else {
			newTabs = new TabSpec[currentCount + 1];
		}
		for (int i = 0; i < currentCount; i++) {
			newTabs[i] = tabs.getTabSpec(i);
		} //이럴거면 arrayList를 써야지
		newTabs[currentCount] = new TabSpec(newTabTitle, Theme.TAB_UNSELECTED);
		if (currentCount < 4) {
			newTabs[currentCount + 1] = new TabSpec("+", Theme.TAB_UNSELECTED);
		}

		getContentPanel().remove(tabs);

		tabs = new TabsBar(newTabs, tabW, tabH);
		for (int i = 0; i < newTabs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		}
		int tabsRight = boxX + (newTabs.length - 1) * gap + tabW;
		tabs.setBounds(0, 0, tabsRight, tabY + tabH);

		getContentPanel().add(tabs);
		getContentPanel().setComponentZOrder(tabs, 0);
		getContentPanel().setComponentZOrder(box, 1);

		TabContentPanel newPanel = createContentPanel(newTabTitle + " 내용");
		contentPanels.add(newPanel);
		box.add(newPanel);

		showTabContent(currentCount);

		tabs.setOnChange(selected -> {
			if (selected == tabs.getTabCount() - 1) {
				addNewTab();
				return;
			}
			showTabContent(selected);
			updateTabColors(selected);
		});

		tabs.setSelectedIndex(currentCount, true);

		updateTabColors(currentCount);

		getContentPanel().revalidate();
		getContentPanel().repaint();
	}//addNewTab

	private void showTabContent(int index) {
		for (int i = 0; i < contentPanels.size(); i++) {
			contentPanels.get(i).setVisible(i == index);
		}
		getContentPanel().repaint();
	}//showTabContent

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

	// 선택된 탭만 노란색, 나머지 회색으로 색상 변경
	private void updateTabColors(int selectedIndex) {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			FolderTab tab = tabs.getTab(i);
			tab.setSelected(i == selectedIndex);
		}
		tabs.repaint();
	}//updateTabColors
}
