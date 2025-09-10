package front_ui;

import java.awt.*;

import javax.swing.JPanel;
//태그 관리 화면용 클래스
public class TagChip extends JPanel {
    private final String text;
    private final Color bg, fg;

    public TagChip(String text, Color bg, Color fg, int w, int h) {
        super(null);
        this.text = text; this.bg = bg; this.fg = fg;
        setOpaque(false);
        setSize(w, h);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.setColor(fg);
        Font f = new Font("맑은 고딕", Font.BOLD, 16);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (getWidth()-fm.stringWidth(text))/2;
        int ty = (getHeight()-fm.getHeight())/2 + fm.getAscent();
        g2.drawString(text, tx, ty);
        g2.dispose();
    }
}
