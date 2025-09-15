package front_ui;

import java.awt.Dimension;

import javax.swing.JPanel;

//태그 관리 화면용 클래스
public class TabsBar extends JPanel {
	public interface OnChange {
		void onChange(int selectedIndex);
	}

	private final FolderTab[] tabs;
	private int selectedIndex = 0;
	private OnChange onChange;
	private TabSpec[] tabSpecs; // 이미 존재할 가능성 높음

	public TabsBar(TabSpec[] specs, int tabW, int tabH) {
		super(null);
		setOpaque(false);

		this.tabSpecs = specs;
		tabs = new FolderTab[specs.length];
		for (int i = 0; i < specs.length; i++) {
			int idx = i;
			tabs[i] = new FolderTab(specs[i].title, specs[i].color, tabW, tabH, () -> setSelectedIndex(idx, true));
			add(tabs[i]);
		}
		setSize(tabW * specs.length, tabH);

		// 초기 선택 상태 반영
		setSelectedIndex(0, false);
	}

	// TabsBar 클래스 내부에 추가 , 0910 승민쓰 추가 코드
	public int getTabCount() {
		return tabs.length; // 또는 tabs.size() 등 내부 자료구조에 맞게 변경
	}

	public void setTabLocation(int index, int x, int y) {
		FolderTab t = tabs[index];
		Dimension d = t.getPreferredSize();
		t.setBounds(x, y, d.width, d.height); // ← 위치+크기 동시 지정
	}

	public void setOnChange(OnChange cb) {
		this.onChange = cb;
	}

	public void setSelectedIndex(int index, boolean notify) {
		if (index < 0 || index >= tabs.length)
			return;
		selectedIndex = index;
		if (notify && onChange != null)
			onChange.onChange(selectedIndex);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public FolderTab getTab(int index) {
		return tabs[index];
	}
}
