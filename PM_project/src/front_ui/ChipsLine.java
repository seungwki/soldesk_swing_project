package front_ui;

import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import VO.Tag;

public class ChipsLine extends JPanel {
	public ChipsLine() {
		super(null);
		setOpaque(false);
	}

	public void setChips(List<String> labels, Color bg, Color fg, int chipH, int gap) {
		removeAll();
		int h = Math.max(getHeight(), chipH); // 컨테이너 높이 유지
		int x = 0;
		for (String s : labels) {
			int w = Math.min(160, 52 + s.length() * 10);
			TagChip c = new TagChip(s, bg, fg, w, chipH);
			c.setLocation(x, (h - chipH) / 2);
			add(c);
			x += w + gap;
		}
		// 컨테이너 크기 변경하지 않음. 세로 중앙 정렬만 수행.
		revalidate();
		repaint();
	}

	public void setChips(List<Tag> tagList, int chipH, int gap) {
		removeAll();
		int h = Math.max(getHeight(), chipH); // 컨테이너 높이 유지
		int x = 0;
		for (int i = 0; i < tagList.size(); i++) {
			int w = Math.min(160, 52 + tagList.get(i).getName().length() * 10);
			TagChip c = new TagChip(tagList.get(i).getName(), tagList.get(i).getColor(), w, chipH);
			c.setLocation(x, (h - chipH) / 2);
			add(c);
			x += w + gap;
		}
		// 컨테이너 크기 변경하지 않음. 세로 중앙 정렬만 수행.
		revalidate();
		repaint();
	}
}
