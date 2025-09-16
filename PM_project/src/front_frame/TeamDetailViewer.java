package front_frame;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Project;
import VO.Team;
import front_ui.AutoGrowBox;
import front_ui.ChipsLine;
import front_ui.FileListPanel;
import front_ui.FolderTab;
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
	private TabSpec[] tabSpecArr;
	private int thisOutputDegree;

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
		this.tabSpecArr = tabSpecArr;
		this.thisOutputDegree = team.getDegree();

		// 내용 상자: 콘텐츠 패널에 추가
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(SELECT_COLOR);
		getContentPanel().add(box);

		// 탭: 콘텐츠 패널에 추가(탭이 위로 오도록 Z-순서 지정)
		TabSpec[] specs = this.tabSpecArr;
		tabs = new TabsBar(specs, 100, 28);
		int gap = 110, tabY = boxBaseY + Theme.BORDER_THICK - 28;
		for (int i = 0; i < specs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
			tabs.getTab(i).setName(String.valueOf(i + 1));
		}
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
		ArrayList<String> stdNameList = new ArrayList<String>();
		for (int i = 0; i < team.getMembers2().size(); i++) {
			stdNameList.add(team.getMembers2().get(i).getsName());
		}
		membersLine.setChips(stdNameList, new Color(0xE5E7EB), new Color(0x334155), 28, 8);
		box.add(membersLine);
		y += 48;

		JLabel filesLabel = new JLabel("파일 목록");
		filesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		filesLabel.setBounds(contentX, y, contentW, 24);
		box.add(filesLabel);
		y += 28;

		FileListPanel files = new FileListPanel(contentW, 32, 8);
		ArrayList<String> fileNameList = new ArrayList<String>();
		if (team.getOutput().getFile() != null) {
			files.setBounds(contentX, y, contentW, files.getPreferredHeight(team.getOutput().getFile().size()));
			for (int i = 0; i < specs.length; i++) {
				fileNameList.add(team.getOutput().getFile().get(i).getName());
			}
		}
		files.setFiles(fileNameList);
		box.add(files);
		y += files.getHeight() + 18;

		JLabel tagTitle = new JLabel("<태그>");
		tagTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tagTitle.setBounds(contentX, y, contentW, 24);
		box.add(tagTitle);
		y += 28;

		ChipsLine tags = new ChipsLine();
		tags.setBounds(contentX, y, contentW, 40);
		ArrayList<String> tagNameList = new ArrayList<String>();
		if (team.getOutput().getTagList() != null) {
			for (int i = 0; i < team.getOutput().getTagList().size(); i++) {
				tagNameList.add(team.getOutput().getTagList().get(i).getName());
			}
		}
		tags.setChips(team.getOutput().getTagList(), 28, 8);
		box.add(tags);
		
		// 점수 라벨 추가
		JLabel scoreLabel = new JLabel("점수 : " + team.getOutput().getScore() + " / " + team.getOutput().getMaxScore() + " 점");
		scoreLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));
		// y 좌표를 조정하여 적절한 위치에 배치합니다.
		scoreLabel.setBounds(contentX, y + 40, contentW, 24);
		box.add(scoreLabel);
		// 버튼 추가
		final int totalButtonWidth = 100 + 10 + 100;
		final int startX = (boxW - totalButtonWidth) / 2;
		JButton modifyButton = new JButton("수정");
		JButton deleteButton = new JButton("삭제");
		modifyButton.setBounds(startX, y + 80, 100, 32);
		modifyButton.setBorderPainted(false);
		modifyButton.setFocusPainted(false);
		modifyButton.setBackground(new Color(0xAFC2F5)); // 밝은 파란색
		modifyButton.setForeground(Color.WHITE);
		deleteButton.setBounds(startX + 100 + 10, y + 80, 100, 32);
		deleteButton.setBorderPainted(false);
		deleteButton.setFocusPainted(false);
		deleteButton.setBackground(new Color(0xAFC2F5));
		deleteButton.setForeground(Color.WHITE);
		box.add(modifyButton);
		box.add(deleteButton);
		
		//수정 버튼 기능 추가
		modifyButton.addActionListener(e -> {
			System.out.println("수정");
		});
		//삭제 버튼 기능 추가
		deleteButton.addActionListener(e -> {
			System.out.println("삭제");
		});
		
		box.autoGrow();
		refreshScroll();

		tabs.setOnChange(idx -> {
			this.selectedTab = idx;
			applyTabSelection();
			String degreeStr = tabs.getTab(idx).getName();
			if (degreeStr == null || degreeStr.equals("+"))
				return;
			try {
				int degree = Integer.parseInt(degreeStr);
				// 선택한 차수의 팀 목록 화면으로 완전히 이동
				BasePage.changePage(new ClassManagerCardViewer(project, idx));
			} catch (NumberFormatException ex) {
				return;
			}
		});

		tabs.setSelectedIndex(this.selectedTab, false);
		applyTabSelection();
	}

	private void applyTabSelection() {
		for (int i = 0; i < tabs.getTabCount(); i++) {
			FolderTab t = tabs.getTab(i);
			boolean sel = (i == thisOutputDegree - 1);
			t.setTabColors(sel ? SELECT_COLOR : null, !sel ? UNSELECT_COLOR : null, SELECT_COLOR, null);
			t.setSelected(sel);
		}
		tabs.repaint();
	}
}
