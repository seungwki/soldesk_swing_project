package jpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;

import javax.swing.JTextArea;

import VO.Lecture;
import basicFrame.BasicFrame;

public class CreateNewLecture implements ActionListener {
//	private String lectureName;
//	private String lecturePlace;
	private JTextArea lectureName;
	private JTextArea lecturePlace;

	public CreateNewLecture(JTextArea lectureName, JTextArea lecturePlace) {
		this.lectureName = lectureName;
		this.lecturePlace = lecturePlace;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!lectureName.getText().equals("") && !lecturePlace.getText().equals("")) {
			String lectureName2 = lectureName.getText();
			String lecturePlace2 = lecturePlace.getText();
			Lecture newLecture = new Lecture(lectureName2, lecturePlace2, "-1");
			new DataWriter().lectureWrite(newLecture);
			BasicFrame.getInstance(new LectureList());// 수업관리-수업 목록 화면을 다시 불러옴
		} else {
			System.out.println("값이 비어있습니다! name >> " + lectureName.getText() + " place >> " + lecturePlace.getText());
		}
	}
}
