package front_ui;

import javax.swing.JPanel;
//태그 관리 화면용 클래스
public class TabsBar extends JPanel {
    public interface OnChange { void onChange(int selectedIndex); }

    private final FolderTab[] tabs;
    private int selectedIndex = 0;
    private OnChange onChange;

    public TabsBar(TabSpec[] specs, int tabW, int tabH) {
        super(null);
        setOpaque(false);
        tabs = new FolderTab[specs.length];
        for (int i = 0; i < specs.length; i++) {
            int idx = i;
            tabs[i] = new FolderTab(specs[i].title, specs[i].color, tabW, tabH,
                () -> setSelectedIndex(idx, true));
            add(tabs[i]);
        }
        setSize(tabW * specs.length, tabH);
    }

    public void setTabLocation(int index, int x, int y) { tabs[index].setLocation(x, y); }
    public void setOnChange(OnChange cb) { this.onChange = cb; }

    public void setSelectedIndex(int index, boolean notify) {
        if (index < 0 || index >= tabs.length) return;
        selectedIndex = index;
        if (notify && onChange != null) onChange.onChange(selectedIndex);
    }

    public int getSelectedIndex() { return selectedIndex; }
    public FolderTab getTab(int index) { return tabs[index]; }
}
