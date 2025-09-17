package front_ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import front_util.Theme;

// 태그 관리 화면에서 테두리가 아닌 내용물 클래스
public class ContentBox extends JPanel {
    private Color borderColor = Theme.LIGHT_BORDER;

    public ContentBox() {
        super(); // 기본 레이아웃 사용 (예: BoxLayout)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 세로로 컴포넌트 쌓기---수정
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
    // 내부에 컴포넌트를 추가하고 자동으로 크기 조정------------------수정
    public void addContent(Component comp) {
        add(comp);
        updateSizeByContent();
    }

    // 내부 내용물 높이 합산으로 preferredSize 높이 자동 설정
    private void updateSizeByContent() {
        int width = getWidth() > 0 ? getWidth() : 400; // 기본 너비, 필요 시 조절
        int totalHeight = 0;
        for (Component comp : getComponents()) {
            Dimension pref = comp.getPreferredSize();
            totalHeight += pref.height;
        }
        Dimension newSize = new Dimension(width, totalHeight);
        setPreferredSize(newSize);
        revalidate();
        repaint();
    }
    
}
