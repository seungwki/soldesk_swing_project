package front_ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import front_util.Theme;

public class OutputRowPanel extends JPanel {
    private final String title;
    private final String rightText;
    private final java.util.List<String> tags;
    private final boolean isPlusRow;

    public static OutputRowPanel of(String title, String rightText, List<String> tags) {
        return new OutputRowPanel(title, rightText, tags, false);
    }
    public static OutputRowPanel plusRow() {
        return new OutputRowPanel("+", "…", Collections.emptyList(), true);
    }

    private OutputRowPanel(String title, String rightText, List<String> tags, boolean isPlusRow) {
        this.title = title;
        this.rightText = rightText;
        this.tags = tags == null ? Collections.emptyList() : tags;
        this.isPlusRow = isPlusRow;
        setOpaque(false);
        setLayout(null);
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int arc = 18;
        int rightW = 220;

        // 왼쪽(흰 카드)
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w - rightW - 8, h, arc, arc);

        // 오른쪽(연한 회/하늘 톤 박스)
        g2.setColor(Theme.TAB_OFF_BG);
        g2.fillRoundRect(w - rightW, 0, rightW, h, arc, arc);

        // 제목 / + 텍스트
        g2.setColor(new Color(0x333333));
        g2.setFont(Theme.FONT_16);
        g2.drawString(title == null ? "" : title, 16, 22);

        // 태그 칩
        if (!isPlusRow && !tags.isEmpty()) {
            int x = 16, y = 32, gap = 12, r = 18;
            for (String t : tags) {
                String s = t == null ? "" : t;
                FontMetrics fm = g2.getFontMetrics();
                int cw = fm.stringWidth(s) + 20;
                int ch = fm.getHeight() + 6;
                g2.setColor(new Color(0xE6ECF6));
                g2.fillRoundRect(x, y, cw, ch, r, r);
                g2.setColor(new Color(0x2B4C9A));
                g2.drawString(s, x + 10, y + ch - 8);
                x += cw + gap;
            }
        }

        // 오른쪽 텍스트
        String rtxt = rightText == null ? "" : rightText;
        FontMetrics fm = g2.getFontMetrics();
        int rx = w - rightW + (rightW - fm.stringWidth(rtxt)) / 2;
        int ry = (h + fm.getAscent()) / 2 - 2;
        g2.setColor(new Color(0x333333));
        g2.drawString(rtxt, rx, ry);

        g2.dispose();
    }
}
