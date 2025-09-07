package frame.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Project;
import VO.Student;

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
		for (int i = 0; i < project.getTeams().size(); i++) {
			if (project.getTeams().get(i).getDegree() == degree) {
				JPanel tempPanel = new JPanel();
				JLabel tempLabel = new JLabel(project.getTeams().get(i).getTName());
				tempPanel.add(tempLabel);
				tempPanel.setBackground(new Color(0xB4EEEE));
				tempPanel.setName(project.getTeams().get(i).getTName());
				//임시용 학생 목록 출력(여기부터 삭제)
				ArrayList<Student> temp = (ArrayList<Student>) project.getTeams().get(i).getMembers2();
				for (int j = 0; j < temp.size(); j++) {
					JLabel tempLabel2 = new JLabel(temp.get(j).getsName());
					tempPanel.add(tempLabel2);
				}
				//임시학생목록 출력 끝(여기까지 삭제)
				teamListPanel.add(tempPanel);
			}
		}
		teamListPanel.revalidate();
		teamListPanel.repaint();
	}//public void actionPerformed(ActionEvent e) {
}
