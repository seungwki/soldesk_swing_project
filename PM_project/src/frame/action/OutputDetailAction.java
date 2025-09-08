package frame.action;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import VO.Project;
import VO.Team;
import frame.BasicFrame;
import frame.OutputDetail;

public class OutputDetailAction implements MouseListener {
	Project project;
	Team team;

	public OutputDetailAction(Project project, Team team) {
		this.project = project;
		this.team = team;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		BasicFrame.getInstance(new OutputDetail(project, team));
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}