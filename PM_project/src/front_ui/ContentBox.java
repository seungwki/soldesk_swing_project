package front_ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import front_util.Theme;

// 태그 관리 화면에서 테두리가 아닌 내용물 클래스
public class ContentBox extends JPanel {
    private Color borderColor = Theme.LIGHT_BORDER;

    public ContentBox() {
        super(null);
        setBackground(Color.WHITE);
        setOpaque(true);
        applyBorder();
    }

    public void setBorderColor(Color c) {
        this.borderColor = c;
        applyBorder();
        repaint();
    }

    public Color getBorderColor(){ return borderColor; }

    private void applyBorder() {
        Border b = BorderFactory.createLineBorder(borderColor, Theme.BORDER_THICK);
        setBorder(b);
    }
}
