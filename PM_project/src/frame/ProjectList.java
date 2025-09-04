package frame;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import VO.*;
import frame.action.ProjectPanelClickAction;
import frame.action.projectCreationAction;

public class ProjectList extends JPanel {
	// 하드코딩 프로젝트 데이터
	public static ArrayList<Project> projectData = Data.projectData;
	// 아이콘
	private ImageIcon bookmarkOff = new ImageIcon(new ImageIcon("resource\\image\\bookmark_off.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));// 즐겨찾기 off(기본)
	private ImageIcon bookmarkOn = new ImageIcon(new ImageIcon("resource\\image\\bookmark_on.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));// 즐찾on

	public ProjectList() {
		setProjectList();
		setLayout(new GridLayout(0, 2, 10, 10));
		ArrayList<JPanel> projectPanelList = new ArrayList<JPanel>();
		Dimension projectDimension = new Dimension(400, 300);
		// 수업 목록 패널 생성
		for (int i = 0; i < projectData.size(); i++) {
			JPanel tempPanel = new JPanel();
			JLabel tempName = new JLabel(projectData.get(i).getName());
			JLabel tempPlace = new JLabel(projectData.get(i).getPlace());
			JLabel tempBookmark;
			tempPanel.setLayout(new GridLayout(3, 1, 5, 5));
			tempPanel.add(tempName);
			tempPanel.add(tempPlace);
			if (projectData.get(i).isBookmark()) {
				tempBookmark = new JLabel(bookmarkOn);
			} else {
				tempBookmark = new JLabel(bookmarkOff);
			}
			tempPanel.setPreferredSize(projectDimension);
			tempPanel.setBackground(Color.WHITE);
			tempPanel.add(tempBookmark);
			tempPanel.addMouseListener(new ProjectPanelClickAction(projectData.get(i)));
			projectPanelList.add(tempPanel);
		}
		// 수업생성 패널 생성
		JPanel projectCreationPanel = new JPanel();
		JLabel projectCreationLabel = new JLabel("+");
		projectCreationPanel.add(projectCreationLabel);
		projectCreationPanel.setPreferredSize(projectDimension);
		projectCreationPanel.setBackground(Color.WHITE);
		projectCreationPanel.addMouseListener(new projectCreationAction());
		projectPanelList.add(projectCreationPanel);
		// 패널들 화면에 붙이기
		for (int i = 0; i < projectPanelList.size(); i++) {
			add(projectPanelList.get(i));
		}
	}

	// 하드 코딩 데이터
	private static void setProjectList() {
		if (projectData.isEmpty()) {
			projectData.add(new Project("[Microsoft] 인공지능 sw 아카데미", "종로 501호"));
			projectData.add(new Project("언리얼엔진5 게임 프로그래밍 부트캠프", "종로 502호"));
			projectData.add(new Project("데이터분석&AI엔지니어링 마스터 과정", "종로 503호"));
			projectData.add(new Project("Java 풀스택 아카데미", "종로 504호"));
			projectData.add(new Project("생성형 ai 활용 msa기반 풀스택 교육", "종로 505호"));
			projectData.add(new Project("AWS Cloud School", "강남 602호"));
			projectData.add(new Project("Java&Spring 기반 풀스택 개발자 양성 과정", "강남 603호"));
			projectData.add(new Project("Node.js 백엔드 엔지니어 트랙", "강남 604호"));
		}

		for (int i = 0; i < projectData.size(); i++) {
			for (int j = 0; j < 5; j++) {
				Team tempTeam = new Team("팀" + (j + 1) + "-" + j, (i + 1));
				projectData.get(i).addTeam(tempTeam);
			}
		}

		projectData.sort(new Comparator<Project>() {
			@Override
			public int compare(Project o1, Project o2) {
				if (o1.isBookmark() != o2.isBookmark()) {
					if (o1.isBookmark()) {
						return -1;
					} else {
						return 1;
					}
				} else {
					int minLen = Math.min(o1.getName().length(), o2.getName().length());
					for (int i = 0; i < minLen; i++) {
						if (o1.getName().charAt(i) != o2.getName().charAt(i)) {
							return o1.getName().charAt(i) - o2.getName().charAt(i);
						}
					}
					if (o1.getName().length() > o2.getName().length()) {
						return 1;
					} else {
						return -1;
					}
				}
			}
		});
	}
}
