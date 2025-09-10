package front_ui;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;

import front_util.UIUtil;
//태그 관리 화면 클래스
public class TagToolbar extends JPanel {
    private final JButton button;
    private int pad = 8;

    public TagToolbar(String text) {
        super(null);
        setOpaque(false);
        button = UIUtil.createStyledButton(text, false);
        button.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        add(button);
    }

    public JButton getButton(){ return button; }
    public void setPadding(int pad){ this.pad = pad; revalidate(); repaint(); }

    @Override public void doLayout() {
        int btnW=80, btnH=28;
        button.setBounds(getWidth()-btnW-pad, pad, btnW, btnH);
    }
}
