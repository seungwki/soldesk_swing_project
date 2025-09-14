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
	private JLabel titleLabel;
	private JLabel subLabel;
	private Image bookmarkOn = new ImageIcon("resource\\image\\bookmark_on.png").getImage().getScaledInstance(30, 60, Image.SCALE_SMOOTH);

	public ClassManagerCard(Project project) {
		this.project = project;

		setOpaque(false); // 배경 직접 그림
		setPreferredSize(new Dimension(200, 120));
		setLayout(new BorderLayout());

		// 제목 라벨
		titleLabel = new JLabel(project != null ? project.getPlace() : "+", SwingConstants.CENTER);
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 25));
		titleLabel.setOpaque(false);
		titleLabel.setName("title");
		add(titleLabel, BorderLayout.CENTER);

		// 부제목 라벨
		subLabel = new JLabel(project != null ? project.getName() : "", SwingConstants.CENTER);
		subLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		subLabel.setOpaque(false);
		subLabel.setName("subtitle");
		subLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
		add(subLabel, BorderLayout.SOUTH);

		// 마우스 이벤트
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					BasePage.changePage(new ClassManagerCardViewer(project));
				}
				if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu popupMenu = createPopupMenu();
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
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
			JTextField newTitle = new JTextField(20);
			JTextField newSubtitle = new JTextField(10);
			JPanel pane = new JPanel();
			pane.add(new JLabel("이름 : "));
			pane.add(newTitle);
			pane.add(new JLabel("위치 : "));
			pane.add(newSubtitle);
			int result = JOptionPane.showConfirmDialog(null, pane, "수정", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				if (!newSubtitle.getText().equals("")) {
					titleLabel.setText(newSubtitle.getText());
				}
				if (!newTitle.getText().equals("")) {
					subLabel.setText(newTitle.getText());
				}
			}
			newTitle.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
			newTitle.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
			newSubtitle.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
			newSubtitle.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		});
		menu.add(editItem);

		JMenuItem deleteItem = new JMenuItem("삭제");
		deleteItem.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null, project.getPlace() + "을(를) 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				ClassManager.projectData.remove(project);
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

		if (project != null && project.isBookmark()) {
			g2.drawImage(bookmarkOn, 10, 1, this);
		}
		g2.dispose();
	}
}
