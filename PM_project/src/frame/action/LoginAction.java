package frame.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;

import frame.BasicFrame;
import frame.ProjectList;

public class LoginAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		ProjectList projectList = new ProjectList();
		BasicFrame.getInstance(projectList);// 수업관리-수업 목록 화면
	}
}
