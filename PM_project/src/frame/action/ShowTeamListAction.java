package frame.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;
import frame.CRUDOutput;

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
			Team team = project.getTeams2().get(i);
			if (project.getTeams2().get(i).getDegree() == degree && team.getOutput() != null) {
				JPanel tempPanel = new JPanel();
				tempPanel.setLayout(new GridLayout(0, 1));//임시용
				JLabel tempLabel1 = new JLabel(team.getTName());
				JLabel tempLabel2 = new JLabel(team.getOutput().getTitle());
				JPanel tempTagList = new JPanel();
				for (int j = 0; j < team.getOutput().getTagList().size(); j++) {
					tempTagList.add(new JButton(team.getOutput().getTagList().get(i).getName()));
				}
				tempPanel.add(tempLabel1);
				tempPanel.add(tempLabel2);
				tempPanel.add(tempTagList);
				tempPanel.setBackground(new Color(0xB4EEEE));
				tempPanel.setName(team.getTName());
				tempPanel.addMouseListener(new OutputDetailAction(project, team));
				teamListPanel.add(tempPanel);
			}
		}
		//팀 생성 버튼(사실 아웃풋만 생성하는거긴 함)버튼 추가
		JLabel addTeamLabel = new JLabel("+");
		JPanel addTeamPanel = new JPanel();
		addTeamPanel.add(addTeamLabel);
		teamListPanel.add(addTeamPanel);
		addTeamPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new CRUDOutput(CRUDOutput.CREATE_OUTPUT, project, null, null).actionPerformed(new ActionEvent(addTeamPanel, ActionEvent.ACTION_PERFORMED, "createOutput"));
			}
		});

		teamListPanel.revalidate();
		teamListPanel.repaint();
	}//public void actionPerformed(ActionEvent e) {
}
