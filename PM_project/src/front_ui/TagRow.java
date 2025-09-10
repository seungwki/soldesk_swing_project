package front_ui;

import javax.swing.*;
import java.awt.*;

import front_util.Theme;
//태그 관리 화면 클래스
public class TagRow extends JPanel {
    public static final int ROW_H  = 56;
    public static final int CHIP_W = 70, CHIP_H = 32;
    private static final int LEFT_PAD = 12;
    private static final int GAP = 12;

    private final TagChip chip;
    private final JLabel  label;

    public TagRow(String chipText, Color chipBg, String remarkText) {
        super(null);
        setOpaque(false);

        chip = new TagChip(chipText, chipBg, Color.WHITE, CHIP_W, CHIP_H);
        add(chip);

        label = new JLabel(": " + remarkText);
        label.setFont(Theme.FONT_16);
        label.setForeground(Color.BLACK);
        add(label);
    }

    @Override public void doLayout() {
        int yChip = (ROW_H - CHIP_H) / 2;
        chip.setBounds(LEFT_PAD, yChip, CHIP_W, CHIP_H);

        int labelH = 40;
        int yLabel = (ROW_H - labelH) / 2 - 4;
        int xLabel = LEFT_PAD + CHIP_W + GAP;
        label.setBounds(xLabel, yLabel, Math.max(0, getWidth() - xLabel - LEFT_PAD), labelH);
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(300, ROW_H);
    }
}
