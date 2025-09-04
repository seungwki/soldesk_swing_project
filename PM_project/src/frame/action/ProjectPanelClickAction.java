package frame.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

import VO.Project;
import frame.BasicFrame;
import frame.ProjectList;

public class ProjectPanelClickAction implements MouseListener {

	private Project project;
	private JPopupMenu popupMenu = new JPopupMenu();
	// 휴지통 아이콘
	private ImageIcon binIcon = new ImageIcon(
			new ImageIcon("resource\\image\\bin.png").getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));

	public ProjectPanelClickAction(Project project) {
		this.project = project;

		// 메뉴 아이템 생성
		JMenuItem bookmark = new JMenuItem("즐겨찾기");
		JMenuItem edit = new JMenuItem("수정");
		JMenuItem delete = new JMenuItem("삭제");
		// 아이템에 기능 연결
		// 북마크 기능 구현
		bookmark.addActionListener(e -> {
			ArrayList<Project> projectList = ProjectList.projectData;
			for (int i = 0; i < projectList.size(); i++) {
				if (projectList.get(i).getName().equals(this.project.getName())) {
					projectList.get(i).setBookmark(!projectList.get(i).isBookmark());
				}
			}
			BasicFrame.getInstance(new ProjectList());
		});
		edit.addActionListener(e -> {
		});
		delete.addActionListener(e -> {
			String content = "삭제 하시겠습니까?";
			String title = "삭제 확인";
			int result = JOptionPane.showConfirmDialog(null, content, title, JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, binIcon);
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
			System.out.println("TODO : 팀 리스트 가져와서 세션들 슥슥이");
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

}
