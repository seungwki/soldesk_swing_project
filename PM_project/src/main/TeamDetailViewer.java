// front_frame/TeamDetailViewer.java
package main;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

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
	// 바깥 흰 박스
	final int boxX = 19, boxW = 752, boxH = 460;
	final int boxBaseY = 24, boxDrop = 12, boxY = boxBaseY + boxDrop;

	// 레이아웃 상수(필요하면 숫자만 바꿔서 미세조정)
	private static final int SIDE_PAD = 24; // AutoGrowBox 안쪽 좌우 여백(랩퍼(body)까지)
	private static final int TOP_PAD = 16; // AutoGrowBox 안쪽 상단 여백
	private static final int INNER_LPAD = 24; // ← 실제 내용이 시작되는 왼쪽 여백(확실히 띄움)
	private static final int INNER_RPAD = 32; // 실제 내용 오른쪽 여백
	private static final int GAP_Y = 16; // 세로 간격
	private static final int BTN_W = 120;
	private static final int BTN_H = 40;
	private static final int BTN_GAP = 16;

	private final AutoGrowBox box = new AutoGrowBox(); // 바깥 컨테이너
	private final JPanel body = new JPanel(null); // 내용 래퍼(절대좌표는 여기서만 사용)
	private final TabsBar tabs;

	private static final Color SELECT_COLOR = new Color(0xAFC2F5);
	private static final Color UNSELECT_COLOR = Color.WHITE;

	private int selectedTab;
	private final TabSpec[] tabSpecArr;
	private final int thisOutputDegree;

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

		// 바깥 박스
		box.setBottomPadding(0);
		box.setBounds(boxX, boxY, boxW, boxH - 60);
		box.setBorderColor(SELECT_COLOR);
		getContentPanel().add(box);

		// 내용 래퍼(body) — AutoGrowBox가 자식 x를 만져도, 내부에서 한 번 더 여백을 주어 확실히 띄움
		final int bodyW = boxW - SIDE_PAD * 2;
		final int bodyH = boxH - 100; // 대략치(아래 autoGrow에서 실제 높이로 보정)
		body.setOpaque(false);
		body.setBounds(SIDE_PAD, TOP_PAD, bodyW, bodyH);
		box.add(body);

		// 탭바
		TabSpec[] specs = this.tabSpecArr;
		tabs = new TabsBar(specs, 100, 28);
		int gap = 110, tabY = boxBaseY + Theme.BORDER_THICK - 28;
		for (int i = 0; i < specs.length; i++) {
			tabs.setTabLocation(i, boxX + i * gap, tabY);
			tabs.getTab(i).setName(String.valueOf(i + 1));
		}
		tabs.setBounds(0, 0, boxX + (specs.length - 1) * gap + 100, tabY + 28);
		getContentPanel().add(tabs);

		// ─── 본문(전부 body 안에만 add) ───
		int y = 0;
		int innerW = bodyW - INNER_LPAD - INNER_RPAD; // 실제 컨텐츠 영역 폭
		int x0 = INNER_LPAD; // 실제 컨텐츠 시작 X

		JLabel title = new JLabel("주제 : " + team.getOutput().getTitle());
		title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		title.setBounds(x0, y, innerW, 28);
		body.add(title);
		y += 28 + GAP_Y;

		JLabel teamLabel = new JLabel("팀 명 : " + team.getTName());
		teamLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		teamLabel.setBounds(x0, y, innerW, 24);
		body.add(teamLabel);
		y += 24 + GAP_Y;

		ChipsLine membersLine = new ChipsLine();
		membersLine.setBounds(x0, y, innerW, 40);
		ArrayList<String> stdNameList = new ArrayList<>();
		for (int i = 0; i < team.getMembers2().size(); i++) {
			stdNameList.add(team.getMembers2().get(i).getsName());
		}
		membersLine.setChips(stdNameList, new Color(0xE5E7EB), new Color(0x334155), 28, 8);
		body.add(membersLine);
		y += 40 + GAP_Y + 8;

		JLabel filesLabel = new JLabel("파일 목록");
		filesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		filesLabel.setBounds(x0, y, innerW, 24);
		body.add(filesLabel);
		y += 24 + GAP_Y;

		FileListPanel files = new FileListPanel(innerW, 32, 8);
		ArrayList<String> fileNameList = new ArrayList<>();
		if (team.getOutput().getFile() != null) {
			int ph = files.getPreferredHeight(team.getOutput().getFile().size());
			files.setBounds(x0, y, innerW, ph);
			for (int i = 0; i < team.getOutput().getFile().size(); i++) {
				fileNameList.add(team.getOutput().getFile().get(i).getName());
			}
		} else {
			files.setBounds(x0, y, innerW, files.getPreferredHeight(0));
		}
		files.setFiles(fileNameList);
		body.add(files);
		y += files.getHeight() + GAP_Y + 8;

		JLabel tagTitle = new JLabel("<태그>");
		tagTitle.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		tagTitle.setBounds(x0, y, innerW, 24);
		body.add(tagTitle);
		y += 24 + GAP_Y;

		ChipsLine tags = new ChipsLine();
		tags.setBounds(x0, y, innerW, 40);
		if (team.getOutput().getTagList() != null) {
			tags.setChips(team.getOutput().getTagList(), 28, 8);
		} else {
			tags.setChips(new ArrayList<>(), 28, 8);
		}
		body.add(tags);
		y += 40 + GAP_Y + 12;

		JLabel scoreLabel = new JLabel("점수 : " + team.getOutput().getScore() + " / " + team.getOutput().getMaxScore() + " 점");
		scoreLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 22));
		scoreLabel.setBounds(x0, y, innerW, 28);
		body.add(scoreLabel);
		y += 28 + GAP_Y + 8;

		// 중앙 정렬 버튼(컨텐츠 영역 기준)
		int totalBtnW = BTN_W * 2 + BTN_GAP;
		int startX = x0 + (innerW - totalBtnW) / 2;

		RoundedButton modifyButton = new RoundedButton("수정");
		RoundedButton deleteButton = new RoundedButton("삭제");
		modifyButton.setBounds(startX, y, BTN_W, BTN_H);
		deleteButton.setBounds(startX + BTN_W + BTN_GAP, y, BTN_W, BTN_H);
		body.add(modifyButton);
		body.add(deleteButton);
		y += BTN_H + GAP_Y;

		// 기능(그대로 유지)
		modifyButton.addActionListener(e -> {
			DataInputDialog dialog = new DataInputDialog(null, project, team, team.getDegree());
			dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
			dialog.setVisible(true);
		});
		deleteButton.addActionListener(e -> {
			int delete = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?", "삭제 확인", JOptionPane.OK_CANCEL_OPTION);
			if (delete == JOptionPane.OK_OPTION) {
				project.getTeams2().remove(team);
				BasePage.changePage(new ClassManagerCardViewer(project, thisOutputDegree - 1));
			}
		});

		// 실제 높이 반영
		body.setSize(bodyW, y);
		box.autoGrow();
		refreshScroll();

		JScrollPane sp = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, getContentPanel());

		if (sp != null) {
			sp.setBorder(null);
			sp.setViewportBorder(null);

			Runnable fitWidth = () -> {
				int vw = sp.getViewport().getExtentSize().width; // 스크롤바 제외한 가로
				int bw = Math.max(0, vw - boxX * 2); // 좌우 여백(boxX)만큼 뺌
				box.setBounds(boxX, boxY, bw, box.getHeight()); // 오른쪽 여백이 왼쪽과 동일해짐
				box.revalidate();
				box.repaint();
			};

			fitWidth.run(); // 초기 1회
			sp.getViewport().addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					fitWidth.run();
				}
			});
		}

		// 탭 전환(그대로)
		tabs.setOnChange(idx -> {
			this.selectedTab = idx;
			applyTabSelection();
			String degreeStr = tabs.getTab(idx).getName();
			if (degreeStr == null || degreeStr.equals("+"))
				return;
			try {
				Integer.parseInt(degreeStr);
				BasePage.changePage(new ClassManagerCardViewer(project, idx));
			} catch (NumberFormatException ignored) {
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

	// 둥근 파란 버튼(텍스트는 기본 렌더)
	static class RoundedButton extends JButton {
		private static final int R = 14;
		private static final Color BG = new Color(0x4E74DE);

		RoundedButton(String text) {
			super(text);
			setOpaque(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setForeground(Color.WHITE);
			setFont(getFont().deriveFont(Font.BOLD, 16f));
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(BG);
			g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), R * 2, R * 2));
			g2.dispose();
			super.paintComponent(g); // 텍스트
		}
	}
}
