// front_ui/ProjectRow.java
package front_ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Output;
import VO.Tag;
import VO.Team;

public class ProjectRow extends JPanel {
	private Output output;

	// 크기
	private static final int OUTPUT_H = 48;
	private static final int TAG_H = 36;
	private static final int V_GAP = 0;
	private static final int PAD = 8;
	private static final int JO_WIDTH = 180;
	private static final int H_GAP = 0;
	private static final int R = 12;

	// 팔레트: 태그=회색, 조이름=더 어두운 회색
	private static final Color OUTPUT_BG = new Color(0xF7F9FF);
	private static final Color TAG_BG = new Color(0xE5E7EB); // 회색(연)
	private static final Color JO_BG = new Color(0xD1D5DB); // 회색(더 어두움)

	private int leftWidth;
	private Team team;

	private JPanel panelOutput, panelTag, panelJo;
	private JLabel lbOutput, lbJo;

	public ProjectRow() {
		super(null);
		setOpaque(false);
	}

	public ProjectRow(int x, int y, int contentW, Output output) {
		this();
		this.output = output;
		build(contentW);
		setBounds(x, y, contentW, getPreferredHeight());
	}

	public static ProjectRow createDefault(int contentX, int contentY, int contentW) {
		return new ProjectRow(contentX, contentY, contentW, null);
	}

	public int getPreferredHeight() {
		return OUTPUT_H + V_GAP + TAG_H;
	}

	private void build(int contentW) {
		leftWidth = contentW - JO_WIDTH - H_GAP;
		if (leftWidth < 150)
			leftWidth = 150;

		panelOutput = new JPanel(null);
		panelOutput.setOpaque(false);
		panelOutput.setBounds(0, 0, leftWidth, OUTPUT_H);
		lbOutput = new JLabel("", JLabel.LEFT);
		lbOutput.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbOutput.setForeground(Color.DARK_GRAY);
		lbOutput.setBounds(PAD, PAD, leftWidth - PAD * 2, OUTPUT_H - PAD * 2);
		panelOutput.add(lbOutput);
		add(panelOutput);

		panelJo = new JPanel(null);
		panelJo.setOpaque(false);
		panelJo.setBounds(leftWidth + H_GAP, 0, JO_WIDTH, getPreferredHeight());
		lbJo = new JLabel("", JLabel.CENTER);
		lbJo.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbJo.setForeground(Color.DARK_GRAY);
		lbJo.setBounds(0, 0, JO_WIDTH, getPreferredHeight());
		panelJo.add(lbJo);
		add(panelJo);

		panelTag = new JPanel(null);
		panelTag.setOpaque(false);
		panelTag.setBounds(0, OUTPUT_H + V_GAP, leftWidth, TAG_H);
		if (output != null) {
			setTagChips(output.getTagList());
		}
		add(panelTag);

		setSize(contentW, getPreferredHeight());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int h = getPreferredHeight();

		Shape leftShape = new RoundRectangle2D.Float(0, 0, leftWidth, h, R, R);
		g2.setClip(leftShape);
		g2.setColor(TAG_BG);
		g2.fillRect(0, 0, leftWidth, h);
		g2.setColor(OUTPUT_BG);
		g2.fillRect(0, 0, leftWidth, OUTPUT_H);
		g2.setClip(null);
		g2.setColor(TAG_BG);
		g2.fillRect(leftWidth - R, OUTPUT_H, R, h - OUTPUT_H);
		g2.setColor(OUTPUT_BG);
		g2.fillRect(leftWidth - R, 0, R, OUTPUT_H);

		g2.setColor(JO_BG);
		g2.fillRoundRect(leftWidth + H_GAP, 0, JO_WIDTH, h, R, R);
		g2.fillRect(leftWidth + H_GAP, 0, R, h);

		g2.dispose();
	}

	public void setOutputTitle(String title) {
		lbOutput.setText(title == null ? "" : title);
	}

	public void setTeamTitle(String teamTitle) {
		lbJo.setText(teamTitle == null ? "" : teamTitle);
	}

	//	public void setTagChips(List<TagChip> chips) {
	public void setTagChips(List<Tag> tagList) {
		panelTag.removeAll();
		int leftW = panelTag.getWidth();
		final int CHIP_H = 28;
		int cx = PAD, cy = (TAG_H - CHIP_H) / 2, cg = 8;
		if (tagList != null) {
			ArrayList<TagChip> tagchipList = new ArrayList<TagChip>();
			for (int i = 0; i < tagList.size(); i++) {
				Tag tempTag = tagList.get(i);
				String txt = tempTag.getName();
				int w = Math.min(140, 52 + txt.length() * 10);
				tagchipList.add(new TagChip(txt, tempTag.getColor(), w, 28));
			}
			for (TagChip chip : tagchipList) {
				chip.setSize(chip.getWidth(), CHIP_H);
				chip.setLocation(cx, cy);
				panelTag.add(chip);
				cx += chip.getWidth() + cg;
				if (cx + 64 > leftW - PAD)
					break;
			}
		}
		panelTag.revalidate();
		panelTag.repaint();
	}

	//    private List<TagChip> dummyTags(int n) {
	//        List<TagChip> list = new ArrayList<>();
	//        for (int i = 0; i < n; i++) {
	//            String txt = "태그" + (i + 1);
	//            int w = Math.min(140, 52 + txt.length() * 10);
	//            list.add(new TagChip(txt, new Color(0xF7F9FF), new Color(0x324B93), w, 28));
	//        }
	//        return list;
	//    }
	// front_ui/ProjectRow.java  (마지막 버전에 아래만 추가)
	private Runnable onClick;

	public void setOnClick(Runnable r) {
		this.onClick = r;
		this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
		if (getMouseListeners().length == 0) {
			addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (onClick != null && e.getButton() == 1)
						onClick.run();
				}
			});
		}
	}

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
