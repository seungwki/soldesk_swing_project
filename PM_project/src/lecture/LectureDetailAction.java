package lecture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import VO.Lecture;
import basicFrame.BasicFrame;

public class LectureDetailAction implements ActionListener {
	private Lecture lecture;

	public LectureDetailAction(Lecture lecture) {
		this.lecture = lecture;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		System.out.println(lecture.getlName());
//		System.out.println(lecture.getlPlace());
//		System.out.println(lecture.getIndex());
		try {
			BasicFrame.getInstance(new LectureDetail(lecture));// 수업관리-수업 상세 화면으로 이동
		} catch (Exception e2) {
		}
	}
}
