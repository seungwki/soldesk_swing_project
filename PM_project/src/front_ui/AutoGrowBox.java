package front_ui;

import javax.swing.*;
import java.awt.*;

// 내용물 증가에 따라 스크롤바 증가 클래스
public class AutoGrowBox extends ContentBox {
	private int bottomPadding = 16;

	public void setBottomPadding(int px) {
		bottomPadding = Math.max(0, px);
	}

	public void autoGrow() {
		int maxBottom = 0;
		for (Component c : getComponents()) {
			if (!c.isVisible())
				continue;

			int childBottom;
			if (c instanceof ContentHeightProvider) {
				childBottom = c.getY() + ((ContentHeightProvider) c).getContentHeight();
			} else {
				childBottom = c.getY() + c.getHeight();
			}
			maxBottom = Math.max(maxBottom, childBottom);
		}
		int needed = maxBottom + bottomPadding;
		if (needed > getHeight()) {
			setBounds(getX(), getY(), getWidth(), needed);
			revalidate();
			repaint();
		}
	}
}
