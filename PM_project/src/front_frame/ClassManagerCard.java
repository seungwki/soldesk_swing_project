package front_frame;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.*;

import VO.Project;
import frame.action.ShowTeamListAction;

public class ClassManagerCard extends JPanel {
	private Project project;
	private Image bookmarkOn = new ImageIcon("resource\\image\\bookmark_on.png").getImage().getScaledInstance(30, 60, Image.SCALE_SMOOTH);// 즐찾on

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ClassManagerCard(Project project) {
		this.project = project;

		setOpaque(false); // 배경 투명 처리
		setPreferredSize(new Dimension(450, 450));

		// 우클릭 팝업 메뉴 처리
		addMouseListener(new MouseAdapter() {
			ActionListener action = new ShowTeamListAction(1, project);

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					//팀 목록 보기로 이동
					action.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "clicked"));
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu popupMenu = createPopupMenu();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
					//우클릭 메뉴 띄움
				}
			}
		});//addMouseListener(

	}

	private JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem favoriteItem = new JMenuItem(project.isBookmark() ? "즐겨찾기 해제" : "즐겨찾기");
		favoriteItem.addActionListener(e -> {
			project.setBookmark(!project.isBookmark());
			repaint();
		});
		menu.add(favoriteItem);

		JMenuItem editItem = new JMenuItem("수정");
		editItem.addActionListener(e -> {
			// 수정 기능
			//			JOptionPane.showMessageDialog(this, title + " 수정하기");
		});
		menu.add(editItem);

		JMenuItem deleteItem = new JMenuItem("삭제");
		deleteItem.addActionListener(e -> {
			// 삭제 기능
			int result = JOptionPane.showConfirmDialog(null, project.getPlace() + "을(를) 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				ArrayList<Project> projectList = ClassManager.projectData;
				for (int i = 0; i < projectList.size(); i++) {
					if (projectList.get(i).getName().equals(project.getName())) {
						projectList.remove(project);
					}
				}
				DefaultFrame.getInstance(new ClassManager());
			}
		});
		menu.add(deleteItem);

		return menu;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int arc = 20;
		int w = getWidth();
		int h = getHeight();
		int mid = h / 2;

		Shape cardShape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc);
		g2.setClip(cardShape);

		g2.setColor(new Color(200, 220, 255));
		g2.fill(cardShape);

		int whiteHeight = mid + 30;
		Shape whiteShape = new RoundRectangle2D.Float(0, 0, w - 1, whiteHeight, arc, arc);
		g2.setColor(Color.WHITE);
		g2.fill(whiteShape);

		g2.setClip(null);
		g2.setColor(new Color(180, 180, 180));
		g2.draw(cardShape);

		// 제목
		g2.setColor(Color.BLACK);
		g2.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		FontMetrics fm = g2.getFontMetrics();
		int titleX = (w - fm.stringWidth(project != null ? project.getPlace() : "+")) / 2;
		int titleY = (whiteHeight - fm.getHeight()) / 2 + fm.getAscent() + 5;
		g2.drawString(project != null ? project.getPlace() : "+", titleX, titleY);

		// 부제목
		g2.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		fm = g2.getFontMetrics();
		int blueY = whiteHeight;
		int blueHeight = h - blueY;
		int subX = (w - fm.stringWidth(project != null ? project.getName() : "")) / 2;
		int subY = blueY + (blueHeight - fm.getHeight()) / 2 + fm.getAscent() + 1;
		g2.drawString(project != null ? project.getName() : "", subX, subY);

		// 북마크
		if (project != null && project.isBookmark()) {
			g2.drawImage(bookmarkOn, 10, 1, this);
		}
		g2.dispose();
	}
}