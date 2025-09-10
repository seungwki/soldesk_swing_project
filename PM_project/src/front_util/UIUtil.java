package front_util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class UIUtil {

    public static JButton createStyledButton(String text, boolean selected) {
        RoundedButton btn = new RoundedButton(text, 30);
        btn.setFont(new Font("Batang", Font.BOLD, 26));
        btn.setFocusPainted(false);
        Color mainBlue = Color.decode("#4e74de");

        if (selected) {
            btn.setBackground(mainBlue);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder());
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(mainBlue, 2));
        }
        return btn;
    }

    /** 둥근 버튼 */
    private static class RoundedButton extends JButton {
        private final int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
            setFocusPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
            super.paintComponent(g2);
            g2.dispose();
        }
        @Override
        public void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    public static void applySelected(JButton b, boolean selected) {
        Color mainBlue = Color.decode("#4e74de");
        int topPad = 4;   // 위쪽으로 내릴 값
        int leftPad = 3;  // 오른쪽으로 밀 값

        if (selected) {
            b.setBackground(mainBlue);
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createEmptyBorder(topPad, leftPad, 0, 0));
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(Color.BLACK);
            b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainBlue, 2),
                BorderFactory.createEmptyBorder(topPad, leftPad, 0, 0)
            ));
        }
        b.repaint();
    }

}
