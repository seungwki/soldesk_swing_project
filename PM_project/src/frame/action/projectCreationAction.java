package frame.action;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import VO.Data;
import VO.Project;
import frame.BasicFrame;
import frame.ProjectList;

public class projectCreationAction implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		showCreateProjectDialog("", "");
		BasicFrame.getInstance(new ProjectList());
	}

	public static void showCreateProjectDialog(String name, String place) {
		JTextField nameField = new JTextField(30);
		JTextField placeField = new JTextField(30);
		if (!name.equals("")) {
			nameField.setText(name);
		}
		if (!place.equals("")) {
			placeField.setText(place);
		}
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("이름(필수) :"));
		panel.add(nameField);
		panel.add(new JLabel("위치(필수) :"));
		panel.add(placeField);

		int result = JOptionPane.showConfirmDialog(null, panel, "프로젝트 생성", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		String nameInput = nameField.getText();
		String placeInput = placeField.getText();

		if (result == JOptionPane.OK_OPTION) {
			if (nameInput.equals("")) {
				JOptionPane.showMessageDialog(null, "이름을 입력해주십시오.");
				showCreateProjectDialog("", placeInput);
			} else if (placeInput.equals("")) {
				JOptionPane.showMessageDialog(null, "위치를 입력해주십시오.");
				showCreateProjectDialog(nameInput, "");
			} else {
				Data.projectData.add(new Project(nameInput, placeInput));
			}
		}
	}//showCreateProjectDialog

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
