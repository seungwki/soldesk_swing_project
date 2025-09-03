package jpanel;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import VO.Lecture;
import action.LectureDetailAction;

public class LectureList extends JPanel {
	public LectureList() {
		// TODO : resource\data\LectureList.txt 불러와서 수업 객체들 생성하기
		ArrayList<Lecture> lectureList = new ArrayList<>();
		// 파일 읽어오기
		String lectureListFile = "resource\\data\\LectureList.txt";
		try (FileReader fileReader = new FileReader(lectureListFile)) {
			// 파일 읽어 String 생성
			StringBuilder stringBuilder = new StringBuilder();
			int data;
			while ((data = fileReader.read()) != -1) {
				stringBuilder.append((char) data);
			}
			String lectureListRead = stringBuilder.toString();
			// 잘라서 Lecture 생성
			String[] lectureSplit = lectureListRead.split("\n");
			for (int i = 1; i < lectureSplit.length; i++) {
				String[] lectureInfo = lectureSplit[i].split(String.valueOf((char) 3));
				if (lectureInfo[3].equals("0")) {// 삭제 되지 않은 것만 추가
					lectureList.add(new Lecture(lectureInfo[0], lectureInfo[1], lectureInfo[2]));
				}
			}
			// lectureList의 길이만큼 버튼 만들어서 붙이기
			JButton[] btnList = new JButton[lectureList.size() + 1];
			for (int i = 0; i < lectureList.size(); i++) {
				btnList[i] = new JButton(lectureList.get(i).getlName());
				btnList[i].addActionListener(new LectureDetailAction(lectureList.get(i)));
			}
			btnList[lectureList.size()] = new JButton("+");
			for (int i = 0; i < btnList.length; i++) {
				add(btnList[i]);
			}
			JLabel lectureName = new JLabel("new Lecture Name");
			JTextArea lectureNameInput = new JTextArea(1,10);
			JLabel lecturePlace = new JLabel("new Lecture Place");
			JTextArea lecturePlaceInput = new JTextArea(1,10);
			add(lectureName);
			add(lectureNameInput);
			add(lecturePlace);
			add(lecturePlaceInput);
			btnList[lectureList.size()]
					.addActionListener(new CreateNewLecture(lectureNameInput,lecturePlaceInput));
		} catch (Exception e) {
		} // try~catch 파일 읽어오기 끝
		setBackground(Color.GRAY);
		setVisible(true);
	}
}