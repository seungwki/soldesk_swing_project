package front_ui;

import javax.swing.*;
import java.awt.*;

public class ChipButton extends JButton {
    private Color bg;
    private Color fg;

    public ChipButton(String text, Color bg, Color fg, int w, int h) {
        super(text);
        this.bg = bg;
        this.fg = fg;
        setPreferredSize(new Dimension(w, h)); // ✅ 반드시 필요
        setSize(w, h);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setFont(new Font("맑은 고딕", Font.BOLD, 16));
    }

    @Override
    public void setBackground(Color bg) {
        this.bg = bg;
        repaint();  // 색상 변경되면 다시 그리기
    }

    @Override
    public void setForeground(Color fg) {
        this.fg = fg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        g2.setColor(bg != null ? bg : Color.LIGHT_GRAY);
        g2.fillRoundRect(0, 0, w, h, h, h);

        g2.setColor(fg != null ? fg : Color.WHITE);
        FontMetrics fm = g2.getFontMetrics(getFont());
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g2.setFont(getFont());
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }
}
