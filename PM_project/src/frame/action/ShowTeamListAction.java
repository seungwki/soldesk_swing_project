package frame.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;
import front_ui.AutoGrowBox;
import front_ui.TabSpec;
import front_ui.TabsBar;
import front_util.Theme;

public class ShowTeamListAction implements ActionListener {
	//백엔드 필드
	private Project project;
	TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
	boolean isClicked;
	boolean isCreatable;

	public ShowTeamListAction(Project project) {
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
	}//생성자

	//todo 순서
	//패널들 초기화

	//프론트엔드 필드
	private TabsBar tabs;
	final int boxBaseY = 24; // 탭 기준 Y(탭은 이 기준 유지)
	private AutoGrowBox box;
//	private static final Color SELECTED = Theme.TAB_SELECTED;
//	private static final Color UNSELECTED = Theme.TAB_UNSELECTED;

	@Override
	public void actionPerformed(ActionEvent e) {
		//초기화 대상
		Component[] parentPanel = ((Container) e.getSource()).getParent().getParent().getComponents();
		JPanel teamListPanel = (JPanel) parentPanel[0];
		//초기화 진행
		teamListPanel.removeAll();
		//		teamListPanel.getParent().removeAll();
		// ── 박스(자동 확장형) ───────────────────────────── , 내용 추가 시 스크롤바 갱신
		box = new AutoGrowBox();
//		box.setBounds(boxX, boxY, boxW, boxH);
//		box.setBorderColor(UNSELECTED);
		//		getContentPanel().add(box);
		teamListPanel.add(box);

		// ── 탭 바(학생/산출물) ────────────────────────────
		final int tabW = 100, tabH = 28;
		final int tabBottom = boxBaseY + Theme.BORDER_THICK; // 내부 상단선
		final int tabY = tabBottom - tabH;
		Iterator<Integer> teamMapKey = teamMap.keySet().iterator();
		ArrayList<Integer> teamMapKeyArray = new ArrayList<Integer>();
		while (teamMapKey.hasNext()) {
			teamMapKeyArray.add(teamMapKey.next());
		}
		TabSpec[] tabSpec = new TabSpec[teamMapKeyArray.size()];
		for (int i = 0; i < tabSpec.length; i++) {
//			tabSpec[i] = new TabSpec(teamMapKeyArray.get(i) + "차", UNSELECTED);
			//			project.getTeams2().get(i).get;//차수 조회해서 채워넣을 것
		}
		tabs = new TabsBar(tabSpec, tabW, tabH);

//		final int tabStart = boxX;
		final int tabGap = 110; // 탭 사이 간격
		for (int j = 0; j < tabSpec.length; j++) {
//			tabs.setTabLocation(j, tabStart + tabGap * j, tabY);
		}

		// 부모(TabsBar) 크기를 자식 탭이 들어가도록 지정 (잘림 방지)
//		int tabsRight = tabStart + tabSpec.length * tabGap + tabW;
		int tabsBottom = tabY + tabH;
//		tabs.setBounds(0, 0, tabsRight, tabsBottom);

		//		getContentPanel().add(tabs);
		teamListPanel.add(tabs);
		// 탭이 박스 위에 보이도록 Z-순서 조정
		//		getContentPanel().setComponentZOrder(tabs, 0);
		//		getContentPanel().setComponentZOrder(box, 1);
		teamListPanel.setComponentZOrder(tabs, 0);
		teamListPanel.setComponentZOrder(box, 1);

		//차수(degree)에 해당하는 팀 길이를 체크하고, size!=0이라면 차수 버튼을 추가
		//차수1부터 조회하면서 사이즈!=0이라면 팀 목록 뿌리기
		//팀 생성 버튼 추가
		//revalidate();
		//repaint();
		teamListPanel.revalidate();
		teamListPanel.repaint();
	}//public void actionPerformed(ActionEvent e) {

}
