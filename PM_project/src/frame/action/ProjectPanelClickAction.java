package frame.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.*;
import java.awt.*;

import VO.*;
import frame.BasicFrame;
import frame.ProjectList;

public class ProjectPanelClickAction implements MouseListener {

	private Project project;
	private JPopupMenu popupMenu = new JPopupMenu();
	// 휴지통 아이콘
	private ImageIcon binIcon = new ImageIcon(new ImageIcon("resource\\image\\bin.png").getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));

	public ProjectPanelClickAction(Project project) {
		this.project = project;

		// 메뉴 아이템 생성
		JMenuItem bookmark = new JMenuItem();
		JMenuItem edit = new JMenuItem("수정");
		JMenuItem delete = new JMenuItem("삭제");
		// 아이템에 기능 연결
		// 북마크 기능 구현
		if (!project.isBookmark()) {
			bookmark.setText("즐겨찾기");
		} else {
			bookmark.setText("즐겨찾기 해제");
		}
		bookmark.addActionListener(e -> {
			ArrayList<Project> projectList = ProjectList.projectData;
			for (int i = 0; i < projectList.size(); i++) {
				if (projectList.get(i).getName().equals(this.project.getName())) {
					projectList.get(i).setBookmark(!projectList.get(i).isBookmark());
				}
			}
			BasicFrame.getInstance(new ProjectList());
		});
		// 수정 기능 구현
		edit.addActionListener(e -> {
			showEditProjectDialog(project);
			BasicFrame.getInstance(new ProjectList());
		});
		// 삭제 기능 구현
		delete.addActionListener(e -> {
			String content = "삭제 하시겠습니까?";
			String title = "삭제 확인";
			int result = JOptionPane.showConfirmDialog(null, content, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, binIcon);
			if (result == JOptionPane.OK_OPTION) {// 대답이 예아라면 삭제하고 화면 다시 불러오기
				ArrayList<Project> projectList = ProjectList.projectData;
				for (int i = 0; i < projectList.size(); i++) {
					if (projectList.get(i).getName().equals(this.project.getName())) {
						projectList.remove(i);
					}
				}
				BasicFrame.getInstance(new ProjectList());
			} // if
		});// delete.addActionListener
			// 메뉴에 아이템 추가
		popupMenu.add(bookmark);
		popupMenu.add(edit);
		popupMenu.add(delete);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// 우클릭이라면 컨텍스트 메뉴 발생
		//		if (e.isPopupTrigger()) {//이기머고
		if (e.getButton() == MouseEvent.BUTTON3) {
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		// 좌클릭이라면 세션+팀리스트 조회
		else {
			//차수(degree에 따른 팀 정렬)
			TreeMap<Integer, ArrayList<Team>> teamMap = new TreeMap<Integer, ArrayList<Team>>();
			for (int i = 1; i < 6; i++) {
				teamMap.put(i, new ArrayList<Team>());
			}
			for (Team team : project.getTeams()) {
				teamMap.get(team.getdegree()).add(team);
			}
			//차수(degree)에 해당하는 팀 길이를 체크하고, size!=0이라면 차수 버튼을 추가
			for (int i = 1; i < 6; i++) {
				if (teamMap.get(i).size() != 0) {
					JButton tempDegreeBtn = new JButton(i + "차");
					((JPanel) e.getSource()).add(tempDegreeBtn);
					//차수 버튼에 "차수에 해당하는 팀 목록들 가져오기" 기능 추가
					tempDegreeBtn.addActionListener(new ShowTeamList(i));
				}
			}
			//차수가 5차 미만이라면 차수 추가 버튼 만들기
			//1~5까지 조회하면서 size 0인 array가 있다면 break하고 +버튼 추가하기
			for (int i = 1; i < 6; i++) {
				if (teamMap.get(i).size() == 0) {
					JButton tempDegreeBtn = new JButton(i + "차");
					((JPanel) e.getSource()).add(tempDegreeBtn);
					//차수 버튼에 "차수에 해당하는 팀 목록들 가져오기" 기능 추가
					tempDegreeBtn.addActionListener(new ShowTeamList(i));
				}
			}
			//모든 차수의 array.size()가 0이 아니라면 1차 버튼을 자동으로 선택하기(doClick 쓰세요)
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	//	public static void showEditProjectDialog(JFrame parent, Project project) {
	public static void showEditProjectDialog(Project project) {
		JTextField nameField = new JTextField(project.getName(), 30);
		JTextField placeField = new JTextField(project.getPlace(), 30);

		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.add(new JLabel("이름 :"));
		panel.add(nameField);
		panel.add(new JLabel("위치 :"));
		panel.add(placeField);

		int result = JOptionPane.showConfirmDialog(null, panel, "프로젝트 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			project.setName(nameField.getText());
			project.setPlace(placeField.getText());
		}
	}//showEditProjectDialog
}
