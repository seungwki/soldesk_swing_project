package front_ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

import front_util.Theme;

// 태그 관리 화면에 학생, 산출물 이라고 써있는 탭 만드는 클래스
public class FolderTab extends JPanel {
    private final String text;
    private final Color bgColor;
    private final Runnable onClick;

    public FolderTab(String text, Color bgColor, int w, int h, Runnable onClick) {
        super(null);
        this.text = text;
        this.bgColor = bgColor;
        this.onClick = onClick;
        setOpaque(false);
        setSize(w, h);

        JLabel t = new JLabel(text, SwingConstants.CENTER);
        t.setFont(Theme.FONT_14_BOLD);      // 폰트 크기
        t.setForeground(Color.DARK_GRAY);
        t.setBounds(0, 0, w, h);
        add(t);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onClick != null) onClick.run();
            }
        });
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int r = Theme.RADIUS_12;  // 상단 라운드 반경
        g2.setColor(bgColor);

        java.awt.geom.Path2D p = new java.awt.geom.Path2D.Float();
        p.moveTo(0, h);
        p.lineTo(0, r);                // 왼쪽 올라감
        p.quadTo(0, 0, r, 0);          // ↖ 상단-좌 라운드
        p.lineTo(w - r, 0);            // 상단 직선
        p.quadTo(w, 0, w, r);          // ↗ 상단-우 라운드
        p.lineTo(w, h);                // 오른쪽 내려감
        p.closePath();                 // 하단은 각진 채로 닫힘
        g2.fill(p);

        g2.dispose();
    }

}
