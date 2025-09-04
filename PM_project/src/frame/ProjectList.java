package frame;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import VO.Project;
import frame.action.ProjectPanelClickAction;
import frame.action.projectCreationAction;

public class ProjectList extends JPanel {
	// 하드코딩 프로젝트 데이터
	public static ArrayList<Project> projectData = new ArrayList<>();
	// 아이콘
	private ImageIcon bookmarkOff = new ImageIcon(new ImageIcon("resource\\image\\bookmark_off.png").getImage()
			.getScaledInstance(20, 20, Image.SCALE_SMOOTH));// 즐겨찾기 off(기본)
	private ImageIcon bookmarkOn = new ImageIcon(
			new ImageIcon("resource\\image\\bookmark_on.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));// 즐겨찾기
																														// on

	public ProjectList() {
		setProjectList();
		ArrayList<JPanel> projectPanelList = new ArrayList<JPanel>();
		// 수업 목록 패널 생성
		for (int i = 0; i < projectData.size(); i++) {
			JPanel tempPanel = new JPanel();
			JLabel tempLabel = new JLabel(projectData.get(i).getName());
			JLabel tempBookmark = new JLabel(bookmarkOff);
			tempPanel.add(tempLabel);
			tempPanel.add(tempBookmark);
			tempPanel.addMouseListener(new ProjectPanelClickAction(projectData.get(i)));
			projectPanelList.add(tempPanel);
		}
		// 수업생성 패널 생성
		JPanel projectCreationPanel = new JPanel();
		JLabel projectCreationLabel = new JLabel("+");
		projectCreationPanel.add(projectCreationLabel);
		projectCreationPanel.addMouseListener(new projectCreationAction());
		projectPanelList.add(projectCreationPanel);
		// 패널들 화면에 붙이기
		for (int i = 0; i < projectPanelList.size(); i++) {
			add(projectPanelList.get(i));
		}
	}

//	public static ArrayList<Project> getProjectData() {
//		if (projectData == null) {
//			setProjectList();
//		}
//		return projectData;
//	}

	// 하드
	private static void setProjectList() {
		if (projectData.isEmpty()) {
			projectData.add(new Project("[Microsoft] 인공지능 sw 아카데미"));
			projectData.add(new Project("언리얼엔진5 게임 프로그래밍 부트캠프"));
			projectData.add(new Project("데이터분석&AI엔지니어링 마스터 과정"));
			projectData.add(new Project("Java 풀스택 아카데미"));
			projectData.add(new Project("생성형 ai 활용 msa기반 풀스택 교육"));
			projectData.add(new Project("AWS Cloud School"));
			projectData.add(new Project("Java&Spring 기반 풀스택 개발자 양성 과정"));
			projectData.add(new Project("Node.js 백엔드 엔지니어 트랙"));
		}
	}
}
