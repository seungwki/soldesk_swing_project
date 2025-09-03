package jpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import VO.Lecture;

public class deleteLectureAction implements ActionListener {
	private Lecture lecture;

	public deleteLectureAction(Lecture lecture) {
		this.lecture = lecture;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = lecture.getlName();
	}
}
