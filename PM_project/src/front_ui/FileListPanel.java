package front_ui;

import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FileListPanel extends JPanel {
	private final int rowH, vgap, panelWidth;

	public FileListPanel(int width, int rowH, int vgap) {
		super(null);
		this.panelWidth = width;
		this.rowH = rowH;
		this.vgap = vgap;
		setOpaque(false);
	}

	public int getPreferredHeight(int count) {
		return count * rowH + Math.max(0, count - 1) * vgap;
	}

	public void setFiles(List<File> files) {
		removeAll();
		int y = 0;
		for (File f : files) {
			JLabel lb = new JLabel("• " + f, SwingConstants.LEFT);
			lb.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 폰트 키움
			lb.setToolTipText(f.getName());
			lb.setBounds(0, y, panelWidth, rowH);
			lb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lb.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						javax.swing.JOptionPane.showMessageDialog(FileListPanel.this, "열기: " + f);
					}
				}
			});
			add(lb);
			y += rowH + vgap;
		}
		setSize(panelWidth, getPreferredHeight(files.size()));
		revalidate();
		repaint();
	}
}
