package front_ui;

import java.awt.Color;

import javax.swing.JPanel;

// 모든 화면에 기본이 되는 뒷 파랑 배경
public class BlueContentPanel extends JPanel {

    public BlueContentPanel() {
        setLayout(null);                       
        setBackground(Color.decode("#4e74de"));  
        setOpaque(true);                         
    }
}
