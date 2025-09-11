package front_frame;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TabContentPanel extends JPanel {
    public TabContentPanel() {
        setLayout(null);
        setOpaque(true);
        setBackground(new Color(0xE0E6F5)); // 연한 회색, Theme.TAB_OFF_BG 써도 무방
        setBorder(BorderFactory.createLineBorder(new Color(0xCED7EC), 1)); // 연한 테두리 추가
    }
}
