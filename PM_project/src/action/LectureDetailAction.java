package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import VO.Lecture;
import basicFrame.BasicFrame;
import jpanel.LectureDetail;

public class LectureDetailAction implements ActionListener {
	private Lecture lecture;

	public LectureDetailAction(Lecture lecture) {
		this.lecture = lecture;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			BasicFrame.getInstance(new LectureDetail(lecture));// 수업관리-수업 상세 화면으로 이동
		} catch (Exception e2) {
		}
	}
}
