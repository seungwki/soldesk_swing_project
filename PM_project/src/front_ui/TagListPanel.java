package front_ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.Comparator;
import VO.Tag;
import VO.TagRepository;
import front_util.Theme;

// 실질적으로 태그 관리 화면을 구성하는 클래스
public class TagListPanel extends JPanel implements ContentHeightProvider {
    private final ChipButton addButton;
    private final ChipButton searchButton;
    // =====================================================
    // 여기서 + 위치/간격 조절
    // =====================================================
    private static final int BUTTON_LEFT       = 12; // + 버튼 X
    private static final int BUTTON_TOP        = 15;  // + 버튼 Y (음수 금지, 부모 경계로 잘림)---수정
    private static final int GAP_AFTER_BUTTON  = 15;  // + 버튼 '아래'와 첫 줄 칩 사이 간격
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
        searchButton = new ChipButton("검색", Theme.BLUE, Color.WHITE, TagRow.CHIP_W, TagRow.CHIP_H);
        add(searchButton);

        addButton.addActionListener(e -> {});
        searchButton.addActionListener(e -> {
            // 예시: 새 창 열기 (JDialog)
          /*  SwingUtilities.invokeLater(() -> {
                StudentFilterDialog dialog = new StudentFilterDialog();
                dialog.setVisible(true);
            });*/
        });
    }

    @Override
    public void doLayout() {
        addButton.setBounds(BUTTON_LEFT, BUTTON_TOP, TagRow.CHIP_W, TagRow.CHIP_H);

        int searchLeft = BUTTON_LEFT + TagRow.CHIP_W + 10;
        searchButton.setBounds(searchLeft, BUTTON_TOP, TagRow.CHIP_W, TagRow.CHIP_H);

        
    }

    public TagRow addRow(Tag tag) {
        TagRow row = new TagRow(tag);

        int topInset = (TagRow.ROW_H - TagRow.CHIP_H) / 2;
        int rowTop   = nextChipTop - topInset;               

        row.setBounds(0, rowTop, getWidth(), TagRow.ROW_H);
        add(row);

        nextChipTop += TagRow.ROW_H + ROW_GAP;

        revalidate();
        repaint();
        return row;
    }
    public TagRow addRow(String name, Color color, String remark) {
        Tag tag = new Tag(name, remark, color); // VO.Tag 객체 생성
        return addRow(tag); // 기존 addRow(Tag) 메서드 호출
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
    //태그삭제
    public void removeRow(TagRow row) {
        int removedY = row.getY();
        int removedHeight = row.getHeight() + ROW_GAP;  // 빈 공간 포함 높이

        remove(row);

        // 삭제된 태그 아래에 있는 태그들 위치를 위로 당김
        for (Component comp : getComponents()) {
            if (comp == addButton) continue;
            if (comp.getY() > removedY) {
                comp.setLocation(comp.getX(), comp.getY() - removedHeight);
            }
        }

        nextChipTop -= removedHeight;

        revalidate();
        repaint();
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

    public boolean containsTag(String tagName) {
        for (Component comp : getComponents()) {
            if (comp instanceof TagRow) {
                TagRow row = (TagRow) comp;
                if (row.getTagName().equalsIgnoreCase(tagName)) {
                    return true;
                }
            }
        }
        return false;
    }
 // 태그를 TagRepository에서 불러와서 UI에 표시
    public void loadTagsFromRepository() {
        loadTagsFromRepository(TagRepository.getAllTags());
    }

    public void loadTagsFromRepository(List<Tag> tags) {
        resetRows(); // 기존 태그들 제거

        // 북마크 우선 정렬
        tags.stream()
            .sorted(Comparator.comparing(Tag::isBookmarked).reversed())  // true 먼저
            .forEach(this::addRow); // TagRow 생성 + 패널에 추가 + 위치 계산

        revalidate();
        repaint();
    }
    public void loadStudentTags() {
        loadTagsFromRepository(TagRepository.getStudentTags());
    }

    public void loadOutputTags() {
        loadTagsFromRepository(TagRepository.getOutputTags());
    }

}
