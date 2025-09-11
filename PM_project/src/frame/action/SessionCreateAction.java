package frame.action;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;

public class SessionCreateAction implements ActionListener {
	Project project;
	int degree;
	TreeMap<Integer, ArrayList<Team>> teamMap;

	public SessionCreateAction(Project project, int degree, TreeMap<Integer, ArrayList<Team>> teamMap) {
		this.project = project;
		this.degree = degree;
		this.teamMap = teamMap;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPanel sessionListPanel = (JPanel) (((Container) e.getSource()).getParent());
		//TODO : 기존의 +버튼을 제거
		for (int i = 0; i < sessionListPanel.getComponents().length; i++) {
			if (sessionListPanel.getComponents()[i].getName() != null && sessionListPanel.getComponents()[i].getName().equals("addSession")) {
				sessionListPanel.remove(sessionListPanel.getComponents()[i]);
			}
			//			System.out.println("comp >> "+sessionListPanel.getComponents()[i]);
			//			System.out.println("name >>> "+sessionListPanel.getComponents()[i].getName());
		}
		//TODO : 버튼 만들고
		JButton newSession = new JButton(degree + "차");
		newSession.setName(String.valueOf(degree));
		teamMap.put(degree, new ArrayList<Team>());
		//TODO : 기능 추가
//		newSession.addActionListener(new ShowTeamListAction(degree, project));
		//TODO : 부착하고
		sessionListPanel.add(newSession);
		//TODO : teamMap에 빈 팀을 하나 넣을 것(안 그러면 벗어났을 때 생성 안 됨)
		teamMap.get(degree).add(new Team(null, degree));
		//TODO : +버튼 만들어야 하는지 판별해서 한 번 더 하고
		for (int i = 1; i < 6; i++) {
			if (teamMap.get(i).size() == 0) {
				JButton createBtn = new JButton("+");
				sessionListPanel.add(createBtn);
				createBtn.setName("addSession");
				int degree = i;
				createBtn.addActionListener(new SessionCreateAction(project, degree, teamMap));
				break;
			}
		}
		//TODO : 다시 그리기 호출
		sessionListPanel.revalidate(); // 레이아웃 매니저에게 다시 배치하도록 요청
		sessionListPanel.repaint(); // 다시 그리기
	}
}
