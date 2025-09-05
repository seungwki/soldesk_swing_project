package frame;

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

		//차수(degree에 따른 팀 정렬)
		TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
		for (int i = 1; i < 6; i++) {
			teamMap.put(i, new ArrayList<Team>());
		}
		System.out.println("team size : "+project.getTeams().size());
		for (Team team : project.getTeams()) {
			teamMap.get(team.getDegree()).add(team);
		}
		//차수(degree)에 해당하는 팀 길이를 체크하고, size!=0이라면 차수 버튼을 추가
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() != 0) {
				JButton tempDegreeBtn = new JButton(i + "차");
				add(tempDegreeBtn);
				//차수 버튼에 "차수에 해당하는 팀 목록들 가져오기" 기능 추가
				tempDegreeBtn.addActionListener(new ShowTeamListAction(i));
			}
		}
		//차수가 5차 미만이라면 차수 추가 버튼 만들기
		//1~5까지 조회하면서 size 0인 array가 있다면 break하고 +버튼 추가하기
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() == 0) {
				JButton createBtn = new JButton("+");
				add(createBtn);
				createBtn.addActionListener(new createSessionAction());
				break;
			}
		}
		//모든 차수의 array.size()가 0이 아니라면 1차 버튼을 자동으로 선택하기(doClick 쓰세요)
	}
}
