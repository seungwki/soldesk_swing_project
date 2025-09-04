package frame.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import frame.BasicFrame;
import frame.ProjectList;

public class LoginAction implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BasicFrame.getInstance(new ProjectList());// 수업관리-수업 목록 화면
	}
}
