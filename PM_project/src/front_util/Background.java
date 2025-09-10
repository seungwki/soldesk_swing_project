package front_util;

import java.awt.*;

import javax.swing.*;

// 여기 클래스는 배경 2단 색으로 나누고 동그란 버튼 기능 클래스
public class Background extends JPanel {

    private Color topColor = Color.decode("#4e74de");     // 위쪽 색 (Andrea Blue)
    private Color bottomColor = Color.WHITE;              // 아래쪽 색
    private int topHeight = 300;                           // 위쪽 영역 높이 (px 기준)

    public Background() {
        setLayout(null); // 자유 위치 지정 (필요 없다면 생략 가능)
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        // 위쪽 배경
        g.setColor(topColor);
        g.fillRect(0, 0, width, topHeight);

        // 아래쪽 배경
        g.setColor(bottomColor);
        g.fillRect(0, topHeight, width, height - topHeight);
    }

    // 색상 변경 메서드
    public void setTopColor(Color color) {
        this.topColor = color;
        repaint();
    }

    public void setBottomColor(Color color) {
        this.bottomColor = color;
        repaint();
    }

    public void setTopHeight(int height) {
        this.topHeight = height;
        repaint();
    }

    /**
     * 버튼 생성 유틸리티 (selected=true면 파란 배경, 흰 글씨, 둥근 버튼)
     */
    public JButton createStyledButton(String text, boolean selected) {
        RoundedButton btn = new RoundedButton(text, 30); // 둥근 정도 30
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 25));
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

    /**
     * 둥근 모서리 버튼 클래스 / 모서리 부분이 깔끔하지않아 많은 코드 추가됨 ft.GPT
     */
    private static class RoundedButton extends JButton {
        private final int radius;
// 버튼 모양
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
            setFocusPainted(false);
            setBorder(null);
        }
// 모서리 코드 / 깔끔하지 않은 모서리 안티앨리어싱으로 커버
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
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
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }




}
