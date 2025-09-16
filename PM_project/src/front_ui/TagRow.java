package front_ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Tag;
import VO.TagRepository; 

public class TagRow extends JPanel {
    public static final int CHIP_W = 100;
    public static final int CHIP_H = 30;
    public static final int ROW_H  = 40;

    private final ChipButton chipButton;
    private final JLabel remarkLabel;
    private final JLabel bookmarkIcon;
    private Runnable onBookmarkClick;

    private Tag tagData; 

    private Runnable onLeftClick;
    private Runnable onRightClick;

    public TagRow(Tag tag) {
        super(null); // absolute layout
        this.tagData = tag;

        String tagName = tag.getName();
        String remark = tag.getRemark();
        Color chipBg = tag.getColor();
        bookmarkIcon = new JLabel();
        bookmarkIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bookmarkIcon.setBounds(480, (ROW_H - 24) / 2, 24, 24); // 위치 조정
        updateBookmarkIcon(); // ⭐ 상태 반영
        add(bookmarkIcon);
        chipButton = new ChipButton(tagName, chipBg, Color.WHITE, CHIP_W, CHIP_H);
        chipButton.setToolTipText(remark);
        chipButton.setBounds(12, (ROW_H - CHIP_H) / 2, CHIP_W, CHIP_H);
        add(chipButton);

        remarkLabel = new JLabel(":" + remark);
        remarkLabel.setFont(chipButton.getFont());
        remarkLabel.setForeground(Color.DARK_GRAY);
        int labelX = 12 + CHIP_W + 16;
        int labelY = (ROW_H - CHIP_H) / 2 + 5;
        remarkLabel.setBounds(labelX, labelY, 300, 20);
        add(remarkLabel);

        chipButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (onLeftClick != null) onLeftClick.run();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (onRightClick != null) onRightClick.run();
                }
            }
        });
        bookmarkIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tagData.setBookmarked(!tagData.isBookmarked()); // 상태 토글
                TagRepository.setBookmark(tagData.getName(), tagData.isBookmarked());
                updateBookmarkIcon();

                if (onBookmarkClick != null) onBookmarkClick.run();
            }
        });
        setOpaque(false);
        setSize(500, ROW_H);
    }

    // ─────────── Getter / Setter 는 VO.Tag 를 기준으로 ───────────

    public Tag getTagData() {
        return tagData;
    }

    public String getTagName() {
        return tagData.getName();
    }

    public String getRemark() {
        return tagData.getRemark();
    }

    public Color getTagColor() {
        return tagData.getColor();
    }

    public void setTagName(String newName) {
        tagData.setName(newName);
        chipButton.setText(newName);
    }

    public void setRemark(String newRemark) {
        tagData.setRemark(newRemark);
        chipButton.setToolTipText(newRemark);
        remarkLabel.setText(": " + newRemark);
        remarkLabel.repaint();
    }

    public void setTagColor(Color color) {
        tagData.setColor(color);
        chipButton.setBackground(color);
        chipButton.repaint();
    }

    // 클릭 이벤트 설정
    public void setOnLeftClick(Runnable r) {
        this.onLeftClick = r;
    }

    public void setOnRightClick(Runnable r) {
        this.onRightClick = r;
    }
 // 상태에 따라 아이콘 갱신
    private void updateBookmarkIcon() {
        String icon = tagData.isBookmarked() ? "★" : "☆"; // 유니코드 별
        bookmarkIcon.setText(icon);
    }

    // 외부에서 이벤트 핸들러 설정 가능하게
    public void setOnBookmarkClick(Runnable r) {
        this.onBookmarkClick = r;
    }
}
