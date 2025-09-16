package front_frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.*;
import javax.swing.*;

import java.awt.geom.Ellipse2D;

public class GcProfile extends JPanel {
	private final int targetW;
	private final int targetH;
	private Image image;

	// 프로필 크기
	public GcProfile() {
		this(100, 100);
	}

	public GcProfile(int width, int height) {
		this.targetW = width;
		this.targetH = height;
		setOpaque(false);
		setPreferredSize(new Dimension(targetW, targetH));
		setLayout(null);

	}

	public void setImage(Image img) {
		this.image = img;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int w = getWidth();
		int h = getHeight();
		int d = Math.min(w, h);

		// ← 테두리 굵기
		float stroke = 2f;

		// ← 하프픽셀 문제 방지: 선 굵기의 절반만큼 안쪽으로 밀어 그림
		float inset = stroke / 2f;
		float x = (w - d) / 2f + inset;
		float y = (h - d) / 2f + inset;
		float dd = d - stroke; // 실제 원 지름(테두리 고려)

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		java.awt.geom.Ellipse2D circle = new java.awt.geom.Ellipse2D.Float(x, y, dd, dd);

		// 이미지 원형 마스킹
		if (image != null) {
			java.awt.Shape old = g2.getClip();
			g2.setClip(circle);
			// float 좌표 그대로 사용 (하프픽셀 유지)
			g2.drawImage(image, (int) Math.floor(x), (int) Math.floor(y), (int) Math.ceil(x + dd), (int) Math.ceil(y + dd), 0, 0, image.getWidth(null), image.getHeight(null), null);
			g2.setClip(old);
		} else {
			g2.setColor(new Color(230, 230, 230));
			g2.fill(circle);
		}

		// 테두리(검정) — 이제 네 면이 균일하게 보임
		g2.setColor(Color.black);
		g2.setStroke(new java.awt.BasicStroke(stroke, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
		g2.draw(circle);

		g2.dispose();
	}

}
