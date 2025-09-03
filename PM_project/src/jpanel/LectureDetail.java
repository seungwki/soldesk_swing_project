package jpanel;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Lecture;
import VO.Session;
import action.SessionCreateAction;
import action.showTeamListAction;

public class LectureDetail extends JPanel {
	public LectureDetail(Lecture lecture) throws Exception {
		ArrayList<Session> sessionList = lecture.getSessionList();
		// 파일 가져와서 읽고 인덱스에 해당하는 세션 목록들 가져와서 sessionList 채우기
		int lectureIndex = lecture.getIndex();

		// Read file
		String path = "resource\\data\\SessionList";
		String extension = ".txt";
		String fileContent;
		int data;
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(new File(path + extension));
		while ((data = fr.read()) != -1) {
			sb.append((char) data);
		}
		fr.close();
		fileContent = sb.toString();
		// split char3
		String[] splitNewline = fileContent.split("\n");
		String[] sessionInfoList;// split char3
		String[] sessionNameList;// split char4
		for (int i = 0; i < splitNewline.length; i++) {
			sessionInfoList = splitNewline[i].split(String.valueOf((char) 3));
			if (sessionInfoList[0].equals(String.valueOf(lectureIndex))
//					&& sessionInfoList[3].equals(String.valueOf(0))
			) {
				sessionNameList = sessionInfoList[1].split(String.valueOf((char) 4));
				for (int j = 0; j < sessionNameList.length; j++) {
					sessionList.add(new Session(sessionNameList[j], Integer.parseInt(sessionInfoList[2])));
				}
				break;
			}
		} // for
			// Lecture 이름 보여주기
		JLabel lectureName = new JLabel(lecture.getlName());
		// Session 목록 가져와서 이름대로 버튼 만들어서 뿌려주기
		JButton[] sessionBtn = new JButton[sessionList.size() + 1];
		for (int i = 0; i < sessionBtn.length - 1; i++) {
			sessionBtn[i] = new JButton(sessionList.get(i).getSsName());
			sessionBtn[i].addActionListener(new showTeamListAction(lecture, sessionList.get(i)));// session 버튼을 눌러 팀 목록
																									// 조회 기능 추가
		}
		sessionBtn[sessionList.size()] = new JButton("+");
		sessionBtn[sessionList.size()].addActionListener(new SessionCreateAction());// session 생성 기능 추가

		add(lectureName);
		for (JButton jButton : sessionBtn) {
			add(jButton);
		}
		setBackground(Color.GRAY);
	}
}