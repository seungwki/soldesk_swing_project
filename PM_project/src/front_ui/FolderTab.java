package front_ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

import javax.swing.JComponent;
import front_util.Theme;

public class FolderTab extends JComponent {
	private String text;
	private final Color bgColor;
	private final Runnable onClick;
	private String degree;

	private boolean selected = false;

	private Color selectedBg = new Color(0x3F62C9);
	private Color unselectedBg = Color.WHITE;
	private Color borderColor = new Color(0xAFC2F5);
	private Color textColor = Color.DARK_GRAY;

	private static final int ARC = 18;

	// 기존 간단 생성자도 유지
	public FolderTab(String text, int w, int h) {
		this(text, Color.WHITE, w, h, null);
	}

	// ★ TabsBar가 요구하는 생성자 시그니처
	public FolderTab(String text, Color baseColor, int w, int h, Runnable onClick) {
		this.text = text;
		this.unselectedBg = (baseColor != null) ? baseColor : Color.WHITE;
		this.bgColor = baseColor;
		this.onClick = onClick;

		setPreferredSize(new Dimension(w, h));
		setOpaque(false);
		setSize(w, h);

		JLabel t = new JLabel(text, SwingConstants.CENTER);
		t.setFont(Theme.FONT_14_BOLD); // 폰트 크기
		t.setForeground(Color.DARK_GRAY);
		t.setBounds(0, 0, w, h);
		add(t);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (onClick != null)
					onClick.run();
			}
		});
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
		repaint();
	}

	/** 선택/비선택/테두리/텍스트 색 외부 지정 (필요 없으면 null 전달) */
	public void setTabColors(Color selectedBg, Color unselectedBg, Color borderColor, Color textColor) {
		if (selectedBg != null)
			this.selectedBg = selectedBg;
		if (unselectedBg != null)
			this.unselectedBg = unselectedBg;
		if (borderColor != null)
			this.borderColor = borderColor;
		if (textColor != null)
			this.textColor = textColor;
		repaint();
	}

	// paintComponent 교체
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int w = getWidth(), h = getHeight(), arc = 18;

		// 상단만 둥근 탭 모양
		Path2D p = new Path2D.Double();
		p.moveTo(0, h);
		p.lineTo(0, arc);
		p.quadTo(0, 0, arc, 0);
		p.lineTo(w - arc, 0);
		p.quadTo(w, 0, w, arc);
		p.lineTo(w, h);
		p.closePath();
		//		int r = Theme.RADIUS_12; // 상단 라운드 반경
		g2.setColor(bgColor);
		//		p.moveTo(0, h);
		//		p.lineTo(0, r); // 왼쪽 올라감
		//		p.quadTo(0, 0, r, 0); // ↖ 상단-좌 라운드
		//		p.lineTo(w - r, 0); // 상단 직선
		//		p.quadTo(w, 0, w, r); // ↗ 상단-우 라운드
		//		p.lineTo(w, h); // 오른쪽 내려감
		//		p.closePath(); // 하단은 각진 채로 닫힘
		g2.setColor(selected ? selectedBg : unselectedBg);
		g2.fill(p);

		g2.setColor(borderColor);
		g2.draw(p);

		g2.setColor(textColor);
		FontMetrics fm = g2.getFontMetrics(getFont() != null ? getFont() : new Font("맑은 고딕", Font.BOLD, 14));
		g2.setFont(getFont() != null ? getFont() : new Font("맑은 고딕", Font.BOLD, 14));
		int tx = (w - fm.stringWidth(text)) / 2;
		int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
//		g2.drawString(text, tx, ty);
		g2.dispose();
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
}