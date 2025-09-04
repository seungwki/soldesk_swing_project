package frame.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class projectCreationAction implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Create new Project");
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
