package front_ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import VO.Output;
import VO.Team;
import front_util.Theme;

public class ProjectRow extends JPanel {
	private Output output;
	private Team team;

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	// layout
	private static final int OUTPUT_H = 72;
	private static final int TAG_H = 40;
	//	private static final int V_GAP = 8;
	private static final int V_GAP = 0;
	private static final int PAD = 10;
	private static final int JO_WIDTH = 180;

	// 붙여달라는 요청 → 0으로 고정
	private static final int H_GAP = 0;

	// colors (태그가 더 어둡고, 조가 그보다 더 어둡게)
	private static final Color TAG_PANEL_BG = new Color(0xB7C7FF);
	private static final Color JO_PANEL_BG = new Color(0x9FB3FF);
	private static final Color CHIP_BG = new Color(0xEAF0FF);
	private static final Color CHIP_FG = new Color(0x31409F);

	// child refs
	private JPanel panelOutput;
	private JPanel panelTag;
	private JPanel panelJo;
	private JLabel lbOutput;
	private JLabel lbJo;

	public ProjectRow() {
		super(null);
		setLayout(null);
		setOpaque(false);
	}

	// 배치용 ctor: new ProjectRow(contentX, rowY, contentW)
	public ProjectRow(int x, int y, int contentW, Output output) {
		setLayout(null);
		setOpaque(false);
		this.output = output;
		build(contentW);
		setBounds(x, y, contentW, getPreferredHeight());
	}

	//	public static ProjectRow createDefault(int contentX, int contentY, int contentW) {
	//		return new ProjectRow(contentX, contentY, contentW);
	//	}

	public int getPreferredHeight() {
		return OUTPUT_H + V_GAP + TAG_H;
	}

	private void build(int contentW) {
		int leftWidth = contentW - JO_WIDTH - H_GAP;
		if (leftWidth < 150)
			leftWidth = 150;

		Border roundBorder = new RoundedLineBorder(Theme.LIGHT_BORDER, Theme.RADIUS_12);

		// 산출물 패널 (흰색 X, 테두리색과 일치하도록 배경을 Theme.TAB_SELECTED로)
		panelOutput = new JPanel(null);
		panelOutput.setOpaque(true);
		panelOutput.setBackground(Theme.TAB_STUDENT_BG); // 선택 탭과 동일 톤
		panelOutput.setBounds(0, 0, leftWidth, OUTPUT_H);
		panelOutput.setBorder(roundBorder);
		lbOutput = new JLabel("산출물 이름", JLabel.LEFT);
		lbOutput.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lbOutput.setBounds(PAD, PAD, leftWidth - PAD * 2, OUTPUT_H - PAD * 2);
		panelOutput.add(lbOutput);
		add(panelOutput);

		// 조 패널 (산출물 패널 우측에 딱 붙음)
		panelJo = new JPanel(null);
		panelJo.setOpaque(true);
		panelJo.setBackground(JO_PANEL_BG);
		panelJo.setBounds(leftWidth + H_GAP, 0, JO_WIDTH, OUTPUT_H + V_GAP + TAG_H);
		panelJo.setBorder(roundBorder);
		lbJo = new JLabel("", JLabel.CENTER);
		lbJo.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		lbJo.setBounds(0, 0, JO_WIDTH, panelJo.getHeight());
		panelJo.add(lbJo);
		add(panelJo);

		// 태그 패널 (폭을 산출물 패널과 동일)
		panelTag = new JPanel(null);
		panelTag.setOpaque(true);
		panelTag.setBackground(TAG_PANEL_BG);
		panelTag.setBounds(0, OUTPUT_H + V_GAP, leftWidth, TAG_H);
		panelTag.setBorder(roundBorder);
		//		setTagChips(dummyTags(5)); // 기본 더미
		add(panelTag);

		setSize(contentW, getPreferredHeight());
	}

	// ====== 외부에서 쓰던 세터들 ======
	public void setOutputTitle(String title) {
		lbOutput.setText(title == null ? "" : title);
		lbOutput.revalidate();
		lbOutput.repaint();
	}

	public void setTeamTitle(String teamTitle) {
		lbJo.setText(teamTitle == null ? "" : teamTitle);
		lbJo.revalidate();
		lbJo.repaint();
	}

	public void setTagChips(List<TagChip> chips) {
		panelTag.removeAll();

		int leftWidth = panelTag.getWidth();
		int cx = PAD, cy = (TAG_H - 28) / 2;
		int cg = 8;

		if (chips != null) {
			for (int i = 0; i < chips.size(); i++) {
				TagChip chip = chips.get(i);
				chip.setLocation(cx, cy);
				panelTag.add(chip);
				cx += chip.getWidth() + cg;
				if (cx + 80 > leftWidth - PAD)
					break; // 한 줄 넘치면 컷
			}
		}
		panelTag.revalidate();
		panelTag.repaint();
	}

	// ====== 더미 태그 생성 ======
	private List<TagChip> dummyTags(int n) {
		List<TagChip> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			String txt = "태그" + (i + 1);
			int w = Math.min(140, 56 + txt.length() * 14);
			list.add(new TagChip(txt, CHIP_BG, CHIP_FG, w, 28));
		}
		return list;
	}

	// ====== 내부 라운드 라인 보더 (Theme에 의존 안 함) ======
	private static class RoundedLineBorder extends AbstractBorder {
		private final Color color;
		private final int radius;
		private final int thickness = 1;

		RoundedLineBorder(Color color, int radius) {
			this.color = color;
			this.radius = radius;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(color);
			int w = width - 1, h = height - 1;
			g2.drawRoundRect(x, y, w, h, radius, radius);
			g2.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(thickness, thickness, thickness, thickness);
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.left = insets.right = insets.top = insets.bottom = thickness;
			return insets;
		}
	}
}
