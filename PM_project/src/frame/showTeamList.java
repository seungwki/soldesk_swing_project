package frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;
import frame.action.ShowTeamListAction;
import frame.action.createSessionAction;

public class showTeamList extends JPanel {
	Project project;

	public showTeamList(Project project) {
		this.project = project;
		setLayout(new GridLayout(0, 1));
		//차수 버튼 담을 패널 생성, 부착
		JPanel sessionListPanel = new JPanel();
		sessionListPanel.setBackground(new Color(0x1EDDFF));
		sessionListPanel.setName("sessionListPanel");
		add(sessionListPanel);
		//팀 담을 패널 생성, 부착
		JPanel teamListPanel = new JPanel();
		teamListPanel.setBackground(new Color(0xFFDD1E));
		teamListPanel.setName("teamListPanel");
		add(teamListPanel);
		//차수(degree에 따른 팀 정렬)
		TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
		for (int i = 1; i < 6; i++) {
			teamMap.put(i, new ArrayList<Team>());
		}
		System.out.println("project name : " + project.getName());
		for (int i = 0; i < project.getTeams().size(); i++) {
			int degree = project.getTeams().get(i).getDegree();
			teamMap.get(degree).add(project.getTeams().get(i));
		}
		//차수(degree)에 해당하는 팀 길이를 체크하고, size!=0이라면 차수 버튼을 추가
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() != 0) {
				JButton tempDegreeBtn = new JButton(i + "차");
				tempDegreeBtn.setName(String.valueOf(i));
				sessionListPanel.add(tempDegreeBtn);
				//차수 버튼에 "차수에 해당하는 팀 목록들 가져오기" 기능 추가
				tempDegreeBtn.addActionListener(new ShowTeamListAction(i, project));
				boolean isClicked = false;
				if (!isClicked) {
					tempDegreeBtn.doClick();
					isClicked = true;
				}
			}
		}

		//차수가 5차 미만이라면->차수에 해당하는 팀 길이size==0이라면 차수 추가 버튼 만들기
		//1~5까지 조회하면서 size 0인 array가 있다면 break하고 +버튼 추가하기
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() == 0) {
				JButton createBtn = new JButton("+");
				sessionListPanel.add(createBtn);
				createBtn.setName("addSession");
				createBtn.addActionListener(new createSessionAction());
				break;
			}
		}

	}
}
