// front_frame/ProjectDetailPage.java
package front_frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;
import front_ui.AutoGrowBox;
import front_ui.ChipsLine;
import front_ui.FileListPanel;
import front_ui.FolderTab;
import front_ui.ProjectInfo;
import front_ui.TabSpec;
import front_ui.TabsBar;
import front_ui.TopBar;
import front_util.Theme;

public class TeamDetailViewer extends BasePage {
	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24, boxDrop = 12, boxY = boxBaseY + boxDrop;
	final int contentX = 24, contentW = boxW - contentX * 2;

	private final AutoGrowBox box = new AutoGrowBox();
	private final TabsBar tabs;

	private static final Color SELECT_COLOR = new Color(0xAFC2F5);
	private static final Color UNSELECT_COLOR = Color.WHITE;
	private int selectedTab;
	private Team team;
	private Project project;
	private TabSpec[] tabSpecArr;

	public TeamDetailViewer(Team team, Project project, TabSpec[] tabSpecArr) {
		super(new TopBar.OnMenuClick() {
			@Override
			public void onClass() {
				DefaultFrame.getInstance(new ClassManager());
			}

			@Override
			public void onStudent() {
				DefaultFrame.getInstance(new StudentManager());
			}

			@Override
			public void onTag() {
				DefaultFrame.getInstance(new TagManager());
			}
		});
		getTopBar().selectOnly("class");
		this.team = team;
		this.project = project;
		this.tabSpecArr = tabSpecArr;

		// 내용 상자: 콘텐츠 패널에 추가
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(SELECT_COLOR);
		getContentPanel().add(box);

		// 탭: 콘텐츠 패널에 추가(탭이 위로 오도록 Z-순서 지정)
		TabSpec[] specs = this.tabSpecArr;
		tabs = new TabsBar(specs, 100, 28);
		int gap = 110, tabY = boxBaseY + Theme.BORDER_THICK - 28;
		for (int i = 0; i < specs.length; i++)
			tabs.setTabLocation(i, boxX + i * gap, tabY);
		tabs.setBounds(0, 0, boxX + (specs.length - 1) * gap + 100, tabY + 28);
		getContentPanel().add(tabs);

		// ===== 본문 =====
		int y = 20;

		JLabel title = new JLabel("주제 : " + team.getOutput().getTitle());
		title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		title.setBounds(contentX, y, contentW, 28);
		box.add(title);
		y += 34;

		JPanel hr = new JPanel();
		hr.setBackground(Theme.TAB_OFF_LINE);
		hr.setBounds(contentX, y, contentW, 2);
		box.add(hr);
		y += 10;

		JLabel teamLabel = new JLabel("팀 명 : " + team.getTName());
		teamLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		teamLabel.setBounds(contentX, y, contentW, 24);
		box.add(teamLabel);
		y += 32;

		ChipsLine membersLine = new ChipsLine();
		membersLine.setBounds(contentX, y, contentW, 40);
		//		membersLine.setChips(team.getMembers2(), new Color(0xE5E7EB), new Color(0x334155), 28, 8);
		box.add(membersLine);
		y += 48;

		JLabel filesLabel = new JLabel("파일 목록");
		filesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		filesLabel.setBounds(contentX, y, contentW, 24);
		box.add(filesLabel);
		y += 28;

		FileListPanel files = new FileListPanel(contentW, 32, 8);
		//		files.setBounds(contentX, y, contentW, files.getPreferredHeight(info.files.size()));
		//		files.setFiles(info.files);
		box.add(files);
		y += files.getHeight() + 18;

		JLabel tagTitle = new JLabel("<태그>");
		tagTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tagTitle.setBounds(contentX, y, contentW, 24);
		box.add(tagTitle);
		y += 28;

		ChipsLine tags = new ChipsLine();
		tags.setBounds(contentX, y, contentW, 40);
		//		tags.setChips(info.tags, new Color(0xE5E7EB), new Color(0x1F2937), 28, 8);
		box.add(tags);

		box.autoGrow();
		refreshScroll();

		tabs.setOnChange(idx -> {
			this.selectedTab = idx;
			applyTabSelection();
		});
		tabs.setSelectedIndex(this.selectedTab, false);
		applyTabSelection();
	}

	private void applyTabSelection() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			FolderTab t = tabs.getTab(i);
			boolean sel = (i == selectedTab);
			t.setTabColors(sel ? SELECT_COLOR : null, !sel ? UNSELECT_COLOR : null, SELECT_COLOR, null);
			t.setSelected(sel);
		}
		tabs.repaint();
	}

	// 추가: 공통 탭 팩토리
	private TabsBar makePhaseTabs(int x, int y) {
		TabSpec[] specs = { new TabSpec("1차", Color.WHITE), new TabSpec("2차", Color.WHITE), new TabSpec("3차", Color.WHITE), new TabSpec("4차", Color.WHITE), new TabSpec("5차", Color.WHITE) };
		TabsBar tb = new TabsBar(specs, 100, 28);
		int gap = 110;
		for (int i = 0; i < specs.length; i++)
			tb.setTabLocation(i, x + i * gap, y);
		tb.setBounds(0, 0, x + (specs.length - 1) * gap + 100, y + 28);
		return tb;
	}
	// 사용처: tabs = makePhaseTabs(boxX, boxBaseY + Theme.BORDER_THICK - 28);

}
