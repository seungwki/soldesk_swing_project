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
		sortProjectList();
		setLayout(new GridLayout(0, 2, 10, 10));
		ArrayList<JPanel> projectPanelList = new ArrayList<JPanel>();
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
			tempPanel.setBackground(Color.WHITE);
			tempPanel.add(tempBookmark);
			tempPanel.addMouseListener(new ProjectPanelClickAction(projectData.get(i)));
			projectPanelList.add(tempPanel);
		}
		// 수업생성 패널 생성
		JPanel projectCreationPanel = new JPanel();
		JLabel projectCreationLabel = new JLabel("+");
		projectCreationPanel.add(projectCreationLabel);
		projectCreationPanel.setBackground(Color.WHITE);
		projectPanelList.add(projectCreationPanel);
		// 패널들 화면에 붙이기
		for (int i = 0; i < projectPanelList.size(); i++) {
			add(projectPanelList.get(i));
		}
		projectCreationPanel.addMouseListener(new projectCreationAction());
	}

	// 정렬
	private static void sortProjectList() {
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
					if (!o1.getName().equals(o2.getName())) {
						return o1.getName().compareTo(o2.getName());
					} else {
						return o1.getPlace().compareTo(o2.getPlace());
					}
				}
			}
		});
	}
}
