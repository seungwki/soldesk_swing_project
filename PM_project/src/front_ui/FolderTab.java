package front_ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import front_util.Theme;

// 태그 관리 화면에 학생, 산출물 이라고 써있는 탭 만드는 클래스
public class FolderTab extends JPanel {
	private final String text;
	private final Color bgColor;
	private final Runnable onClick;
	private boolean selected = false; // 0911 추가코드

	public FolderTab(String text, Color bgColor, int w, int h, Runnable onClick) {
		super(null);
		this.text = text;
		this.bgColor = bgColor;
		this.onClick = onClick;

		setOpaque(false);
		setSize(w, h);

		JLabel t = new JLabel(text, SwingConstants.CENTER);
		t.setFont(Theme.FONT_14_BOLD); // 폰트 크기
		t.setForeground(Color.DARK_GRAY);
		t.setBounds(0, 0, w, h);
		add(t);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (onClick != null)
					onClick.run();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		//super.paintComponent(g); // 0910 일단 생략으로 수정
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth(), h = getHeight();
		int r = Theme.RADIUS_12; // 상단 라운드 반경

		Color fillColor = selected ? Theme.ACCENT_OUTPUT : Theme.TAB_OFF_BG; // 선택된 탭은 노란색, 아닌 건 연한 회색
		g2.setColor(fillColor); // 0911 변경

		java.awt.geom.Path2D p = new java.awt.geom.Path2D.Float();
		p.moveTo(0, h);
		p.lineTo(0, r); // 왼쪽 올라감
		p.quadTo(0, 0, r, 0); // ↖ 상단-좌 라운드
		p.lineTo(w - r, 0); // 상단 직선
		p.quadTo(w, 0, w, r); // ↗ 상단-우 라운드
		p.lineTo(w, h); // 오른쪽 내려감
		p.closePath(); // 하단은 각진 채로 닫힘
		g2.fill(p);

		g2.dispose();

		super.paintComponent(g);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
}
