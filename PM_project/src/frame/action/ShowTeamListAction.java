package frame.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;

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
		for (int i = 0; i < project.getTeams().size(); i++) {
			if (project.getTeams().get(i).getDegree() == degree) {
				JPanel tempPanel = new JPanel();
				JLabel tempLabel = new JLabel(project.getTeams().get(i).getTName());
				tempPanel.add(tempLabel);
				tempPanel.setBackground(new Color(0xB4C7E7));
				tempPanel.setName(project.getTeams().get(i).getTName());
				teamListPanel.add(tempPanel);
			}
		}
		teamListPanel.revalidate();
		teamListPanel.repaint();
	}//public void actionPerformed(ActionEvent e) {
}
