package front_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ProjectAddRow extends JPanel {
    private static final Color CARD_BG      = Color.WHITE;
    private static final Color GROUP_BOX_BG = new Color(0xE6E9F3);

    public ProjectAddRow() {
        setLayout(null);
        setOpaque(false);
        int H = 120; int rightW = 240; int leftW = 752 - 16*2 - rightW - 16;
        setSize(752 - 16*2, H);

        JPanel left = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),14,14));
                g2.dispose();
            }
        };
        left.setOpaque(false);
        left.setBounds(0, 0, leftW, H);
        JLabel plus = new JLabel("+", SwingConstants.CENTER) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xE6ECF6));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),16,16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        plus.setBounds(16, 12, 56, 28);
        left.add(plus);
        add(left);

        JPanel right = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GROUP_BOX_BG);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),14,14));
                g2.dispose();
            }
        };
        right.setOpaque(false);
        right.setBounds(leftW + 16, 0, rightW, H);
        JLabel dots = new JLabel("…", SwingConstants.CENTER);
        dots.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        dots.setBounds(0,0,rightW,H);
        right.add(dots);
        add(right);
    }
}
