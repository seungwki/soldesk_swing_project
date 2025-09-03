package action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import VO.Lecture;
import VO.Session;
import basicFrame.BasicFrame;
import jpanel.LectureDetail;

public class showTeamListAction implements ActionListener {
	private Lecture lecture;
	private Session session;

	public showTeamListAction(Lecture lecture, Session session) {
		this.lecture = lecture;
		this.session = session;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			BasicFrame.getInstance(new TeamList(lecture, session));// 세션을 누르면 팀 목록을 보여줌
		} catch (Exception e2) {
		}
	}
}
