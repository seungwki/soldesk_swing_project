package skeleton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import basicFrame.BasicFrame;
import lecture.LectureManagerList;

public class LoginAction implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		BasicFrame.getInstance(new LectureManagerList());// 수업관리-수업 목록 화면
	}
}
