package front_ui;

import javax.swing.JPanel;
import java.awt.Color;

import front_util.Theme;

// 실질적으로 태그 관리 화면을 구성하는 클래스
public class TagListPanel extends JPanel implements ContentHeightProvider {
    private final ChipButton addButton;

    // =====================================================
    // 여기서 + 위치/간격 조절
    // =====================================================
    private static final int BUTTON_LEFT       = 12; // + 버튼 X
    private static final int BUTTON_TOP        = 0;  // + 버튼 Y (음수 금지, 부모 경계로 잘림)
    private static final int GAP_AFTER_BUTTON  = 10;  // + 버튼 '아래'와 첫 줄 칩 사이 간격
    private static final int ROW_GAP           = 10;  // 줄과 줄(칩과 칩) 사이 간격
    // =====================================================

    private int nextChipTop;

    public TagListPanel() {
        super(null);
        setOpaque(false);

        addButton = new ChipButton("+", Theme.BLUE, Color.WHITE, TagRow.CHIP_W, TagRow.CHIP_H);
        add(addButton);

        // 첫 줄 칩 시작 위치
        nextChipTop = BUTTON_TOP + TagRow.CHIP_H + GAP_AFTER_BUTTON;


        addButton.addActionListener(e -> {});
    }

    @Override
    public void doLayout() {
        addButton.setBounds(BUTTON_LEFT, BUTTON_TOP, TagRow.CHIP_W, TagRow.CHIP_H);
    }

    public TagRow addRow(String chipText, Color chipBg, String remarkText) {
        TagRow row = new TagRow(chipText, chipBg, remarkText);

        int topInset = (TagRow.ROW_H - TagRow.CHIP_H) / 2;
        int rowTop   = nextChipTop - topInset;               

        row.setBounds(0, rowTop, getWidth(), TagRow.ROW_H);
        add(row);

        nextChipTop += TagRow.CHIP_H + ROW_GAP;

        revalidate();
        repaint();
        return row;
    }

    public void resetRows() {
        for (int i = getComponentCount() - 1; i >= 0; i--) {
            if (getComponent(i) != addButton) remove(i);
        }
        nextChipTop = BUTTON_TOP + TagRow.CHIP_H + GAP_AFTER_BUTTON;
        revalidate();
        repaint();
    }

    public void setOnAdd(Runnable onAdd) {
        var listeners = addButton.getActionListeners();
        for (var l : listeners) addButton.removeActionListener(l);
        addButton.addActionListener(e -> { if (onAdd != null) onAdd.run(); });
    }

    @Override
    public int getContentHeight() {
        int maxBottom = BUTTON_TOP + TagRow.CHIP_H; 
        for (int i = 0; i < getComponentCount(); i++) {
            var c = getComponent(i);
            if (c == addButton) continue;
            int bottom = c.getY() + c.getHeight();
            if (bottom > maxBottom) maxBottom = bottom;
        }
        return maxBottom;
    }
}
