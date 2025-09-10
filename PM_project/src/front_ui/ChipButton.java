package front_ui;

import javax.swing.*;
import java.awt.*;

// 태그 관리에서 태그 + 버튼 클래스
public class ChipButton extends JButton {
    private final Color bg;
    private final Color fg;

    public ChipButton(String text, Color bg, Color fg, int w, int h) {
        super(text);
        this.bg = bg;
        this.fg = fg;
        setSize(w, h);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setFont(new Font("맑은 고딕", Font.BOLD, 16));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2=(Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w=getWidth(), h=getHeight();
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, h, h);

        // 텍스트 중앙
        g2.setColor(fg);
        FontMetrics fm = g2.getFontMetrics(getFont());
        int tx = (w - fm.stringWidth(getText()))/2;
        int ty = (h - fm.getHeight())/2 + fm.getAscent();
        g2.setFont(getFont());
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }
}
