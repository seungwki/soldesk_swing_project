package front_frame;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI; // import 압축하면 작동 안함, 조심할것!!

import front_ui.*;

// 화면의 기본 골조, 파랑색 바탕에서 TopBar 가져온다
public class BasePage extends JPanel {

	public static final int WIDTH = 800, HEIGHT = 600;

	private TopBar topBar;
	private BlueContentPanel content;
	private JScrollPane scrollPane;

	private static final int CONTENT_BOTTOM_PADDING = 24;

	public BasePage() {
		setLayout(null);
		setSize(WIDTH, HEIGHT);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		topBar = new TopBar();
		add(topBar);
		int topH = topBar.getPreferredSize().height;
		topBar.setBounds(0, 0, WIDTH, topH);

		content = new BlueContentPanel();
		content.setLayout(null);

		scrollPane = new JScrollPane(content, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, topH, WIDTH, HEIGHT - topH);
		scrollPane.setBorder(null);
		scrollPane.setOpaque(true);
		scrollPane.getViewport().setOpaque(true);

		JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL);
		vbar.setPreferredSize(new Dimension(24, Integer.MAX_VALUE));
		vbar.setUnitIncrement(50);
		vbar.setBlockIncrement(300);
		vbar.setOpaque(true);
		vbar.setUI(new VisibleScrollBarUI());
		scrollPane.setVerticalScrollBar(vbar);

		scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JPanel());
		add(scrollPane);

		int vpH = scrollPane.getViewport().getExtentSize().height;
		int initialH = Math.max(vpH + 1, (HEIGHT - topH) * 3);
		content.setPreferredSize(new Dimension(WIDTH, initialH));
		content.revalidate();
	}

	public BasePage(TopBar.OnMenuClick menuHandler) {
		this();
		topBar.setMenuHandler(menuHandler);
	}

	public TopBar getTopBar() {
		return topBar;
	}

	public JPanel getContentPanel() {
		return content;
	}

	public void refreshScroll() {
		int maxY = 0, maxW = 0;
		for (Component c : content.getComponents()) {
			if (!c.isVisible())
				continue;
			maxY = Math.max(maxY, c.getY() + c.getHeight());
			maxW = Math.max(maxW, c.getX() + c.getWidth());
		}
		int viewportH = scrollPane.getViewport().getExtentSize().height;
		int viewportW = scrollPane.getViewport().getExtentSize().width;

//		int bottomPadding = 100;
		int newH = Math.max(viewportH + 1, maxY + CONTENT_BOTTOM_PADDING);
		int newW = Math.max(viewportW, maxW);

		content.setPreferredSize(new Dimension(newW, newH));
		content.revalidate();
		content.repaint();
	}

	// 0909 승민이가 추가한 코드
	public void setScrollBarVisible(boolean visible) {
		if (visible) {
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		} else {
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
	}

	// 스크롤바 코드
	private static class VisibleScrollBarUI extends BasicScrollBarUI {
		private static final Color TRACK = new Color(0xE8, 0xEB, 0xF2);
		private static final Color THUMB = new Color(0x2E, 0x4C, 0x9A);
		private static final int R = 8;
		private static final int GAP = 8;
		private static final int PAD = 2;
		private static final int MINH = 60;

		@Override
		protected Dimension getMinimumThumbSize() {
			return new Dimension(24, MINH);
		}

		@Override
		protected JButton createDecreaseButton(int o) {
			return zero();
		}

		@Override
		protected JButton createIncreaseButton(int o) {
			return zero();
		}

		private JButton zero() {
			JButton b = new JButton();
			b.setPreferredSize(new Dimension(0, 0));
			b.setOpaque(false);
			b.setBorder(null);
			b.setContentAreaFilled(false);
			return b;
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int x = r.x + PAD, w = Math.max(0, r.width - PAD * 2);
			int y = r.y + GAP, h = Math.max(0, r.height - GAP * 2);
			g2.setColor(TRACK);
			g2.fillRoundRect(x, y, w, h, R, R);
			g2.dispose();
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
			if (r.isEmpty() || !c.isEnabled())
				return;
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int x = r.x + PAD, w = Math.max(0, r.width - PAD * 2);
			int y = r.y, h = Math.max(MINH, r.height);
			g2.setColor(THUMB);
			g2.fillRoundRect(x, y, w, h, R, R);
			g2.dispose();
		}//paintThumb
	}
	// 💡 화면 전환용 정적 메서드 추가 // 0910 승민쓰 추가코드
	public static void changePage(JPanel newPage) {
		// 현재 Swing 트리에서 최상위 JFrame을 찾아 contentPane을 교체
		Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
		if (window instanceof JFrame) {
			JFrame frame = (JFrame) window;
			frame.setContentPane(newPage);
			frame.revalidate();
			frame.repaint();
		} else {
			System.err.println("changePage 오류: 현재 활성 윈도우가 JFrame이 아닙니다.");
		}
	}

}
