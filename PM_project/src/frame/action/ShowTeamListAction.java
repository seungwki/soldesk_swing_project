package frame.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;

public class ShowTeamListAction implements ActionListener {
	int degree;
	Project project;

	public ShowTeamListAction(int degree, Project project) {
		this.degree = degree;
		this.project = project;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//팀목록 패널 초기화
		Component[] parentPanel = ((Container) e.getSource()).getParent().getParent().getComponents();
		JPanel teamListPanel = (JPanel) parentPanel[1];
		for (int i = 0; i < parentPanel.length; i++) {
			if (parentPanel[i].getName().equals("teamListPanel")) {
				teamListPanel = (JPanel) parentPanel[i];
				teamListPanel.removeAll();
				break;
			}
		}
		//추가
		for (int i = 0; i < project.getTeams2().size(); i++) {
			if (project.getTeams2().get(i).getDegree() == degree) {
				Team team = project.getTeams2().get(i);
				JPanel tempPanel = new JPanel();
				tempPanel.setLayout(new GridLayout(0, 1));//임시용
				JLabel tempLabel = new JLabel(team.getTName());
				JLabel tempLabel2 = new JLabel(team.getOutput().getTitle());
				tempPanel.add(tempLabel);
				tempPanel.add(tempLabel2);
				tempPanel.setBackground(new Color(0xB4EEEE));
				tempPanel.setName(team.getTName());
				//임시용 학생 목록 출력
				//				ArrayList<Student> temp = (ArrayList<Student>) project.getTeams().get(i).getMembers2();
				//				for (int j = 0; j < temp.size(); j++) {
				//					JLabel tempLabel2 = new JLabel(temp.get(j).getsName());
				//					tempPanel.add(tempLabel2);
				//				}
				//임시학생목록 출력 끝
				tempPanel.addMouseListener(new OutputDetailAction(project, team));
				teamListPanel.add(tempPanel);
			}
		}
		teamListPanel.revalidate();
		teamListPanel.repaint();
	}//public void actionPerformed(ActionEvent e) {
}
