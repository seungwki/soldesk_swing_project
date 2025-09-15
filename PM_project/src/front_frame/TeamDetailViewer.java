package front_frame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import front_ui.*;
import front_util.Theme;

import VO.Team;
import VO.Student;
import VO.Output;
import VO.Tag;

public class TeamDetailViewer extends BasePage {
	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24, boxDrop = 12, boxY = boxBaseY + boxDrop;
	final int contentX = 24, contentW = boxW - contentX * 2;

	private final AutoGrowBox box;
	private final TabsBar tabs;

	private static final Color SELECT_COLOR = new Color(0xAFC2F5);
	private static final Color UNSELECT_COLOR = Color.WHITE;
	private int selectedTab;

	public TeamDetailViewer(Team team, int initialTab) {
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
		this.selectedTab = Math.max(0, Math.min(4, initialTab));

		// 내용 상자: 콘텐츠 패널에 추가
		box = new AutoGrowBox();
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(SELECT_COLOR);
		getContentPanel().add(box);

		// 탭: 콘텐츠 패널에 추가(탭이 위로 오도록 Z-순서 지정)
		TabSpec[] specs = { new TabSpec("1차", UNSELECT_COLOR), new TabSpec("2차", UNSELECT_COLOR), new TabSpec("3차", UNSELECT_COLOR), new TabSpec("4차", UNSELECT_COLOR), new TabSpec("5차", UNSELECT_COLOR) };
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
		ArrayList<String> StudentNameList = new ArrayList<>();
		for (int i = 0; i < team.getMembers2().size(); i++) {
			StudentNameList.add(team.getMembers2().get(i).getsName());
		}
		membersLine.setChips(StudentNameList, new Color(0xE5E7EB), new Color(0x334155), 28, 8);
		box.add(membersLine);
		y += 48;

		JLabel filesLabel = new JLabel("파일 목록");
		filesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		filesLabel.setBounds(contentX, y, contentW, 24);
		box.add(filesLabel);
		y += 28;

		FileListPanel files = new FileListPanel(contentW, 32, 8);
		if (team.getOutput().getFile() != null) {
			files.setBounds(contentX, y, contentW, files.getPreferredHeight(team.getOutput().getFile().size()));
			files.setFiles(team.getOutput().getFile());
			box.add(files);
		}
		y += files.getHeight() + 18;

		JLabel tagTitle = new JLabel("<태그>");
		tagTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tagTitle.setBounds(contentX, y, contentW, 24);
		box.add(tagTitle);
		y += 28;

		ChipsLine tags = new ChipsLine();
		tags.setBounds(contentX, y, contentW, 40);
		ArrayList<String> newTagnameList = new ArrayList<String>();
		for (int i = 0; i < team.getOutput().getTagList().size(); i++) {
			newTagnameList.add(team.getOutput().getTagList().get(i).getName());
		}
		tags.setChips(newTagnameList, new Color(0xE5E7EB), new Color(0x1F2937), 28, 8);
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