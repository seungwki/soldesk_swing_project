// front_frame/FrameMyInfo.java
package front_frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import VO.User;

public class FrameMyInfo extends JPanel {

	private final JLabel lblPhoto = new JLabel("", SwingConstants.CENTER);
	private final JLabel lblNameV = new JLabel();
	private final JLabel lblEmailV = new JLabel();
	private final JLabel title = new JLabel("내 정보", SwingConstants.CENTER);

	// 라벨을 필드로(도Layout에서 직접 배치)
	private final JLabel lName = new JLabel("이름");
	private final JLabel lEmail = new JLabel("이메일");
	private final JLabel lGender = new JLabel("성별");

	private final JRadioButton rbM = new JRadioButton("남");
	private final JRadioButton rbF = new JRadioButton("여");
	private final JRadioButton rbN = new JRadioButton("비공개");
	private final ButtonGroup bg = new ButtonGroup();

	private final RoundedButton btnSave = new RoundedButton("저장");
	private final RoundedButton btnBack = new RoundedButton("뒤로가기");
	// 프로필+정보 블록 좌/우 미세 이동(px).  +면 오른쪽, -면 왼쪽으로 한 번에 통째 이동
	private static final int BLOCK_SHIFT_X = 55; // 필요하면 예: 12, -6 등으로 조절

	public FrameMyInfo() {
		setLayout(null);
		setBackground(new Color(0xF4, 0xF6, 0xF9)); // 살짝 어둡게

		// 제목
		title.setFont(new Font("Dialog", Font.BOLD, 28));
		title.setForeground(new Color(0x1F2937));
		add(title);

		// 프로필
		lblPhoto.setOpaque(true);
		lblPhoto.setBackground(new Color(0xF1F3F5));
		lblPhoto.setBorder(BorderFactory.createLineBorder(new Color(0xE5E7EB)));
		add(lblPhoto);

		// 텍스트 스타일
		Font kLabel = new Font("Dialog", Font.PLAIN, 16);
		Font kValue = new Font("Dialog", Font.BOLD, 16);
		lName.setFont(kLabel);
		lEmail.setFont(kLabel);
		lGender.setFont(kLabel);
		lblNameV.setFont(kValue);
		lblEmailV.setFont(kValue);
		rbM.setFont(kLabel);
		rbF.setFont(kLabel);
		rbN.setFont(kLabel);

		// 추가
		add(lName);
		add(lblNameV);
		add(lEmail);
		add(lblEmailV);
		add(lGender);
		add(rbM);
		add(rbF);
		add(rbN);
		add(btnSave);
		add(btnBack);

		// 데이터 바인딩(기능 그대로)
		User me = UserManager.getInstance().getCurrentUser();
		if (me != null) {
			lblNameV.setText(me.getName() == null ? "" : me.getName());
			lblEmailV.setText(me.getEmail() == null ? "" : me.getEmail());
			String g = me.getGender();
			if ("남".equals(g))
				rbM.setSelected(true);
			else if ("여".equals(g))
				rbF.setSelected(true);
			else
				rbN.setSelected(true);
			updatePhoto(currentGender());
		} else {
			rbN.setSelected(true);
			updatePhoto("비공개");
		}

		// 성별 변경 시 사진 반영
		java.awt.event.ActionListener onGender = e -> updatePhoto(currentGender());
		rbM.addActionListener(onGender);
		rbF.addActionListener(onGender);
		rbN.addActionListener(onGender);

		// 버튼 동작(그대로)
		btnSave.addActionListener(e -> {
			User u = UserManager.getInstance().getCurrentUser();
			if (u == null) {
				JOptionPane.showMessageDialog(this, "로그인/회원정보를 찾을 수 없습니다.");
				return;
			}
			u.setGender(currentGender());
			UserManager.getInstance().notifyProfileChanged();
			JOptionPane.showMessageDialog(this, "저장되었습니다.");
			DefaultFrame.getInstance(new ClassManager());
		});
		btnBack.addActionListener(e -> DefaultFrame.getInstance(new ClassManager()));

		bg.add(rbM);
		bg.add(rbF);
		bg.add(rbN);
	}

	@Override
	public void doLayout() {
		int w = getWidth(), h = getHeight();

		// ① 내용 가로 중앙 + ② 내용만 아래로 20px
		int contentOffsetY = 20;

		// 제목(상단 약간 위)
		int titleW = 220, titleH = 40;
		int titleY = Math.max(30, (int) (h * 0.14)) - 36;
		title.setBounds((w - titleW) / 2, titleY, titleW, titleH);

		// 사진 + 폼 블록
		int photoSize = 220;
		int gap = 36;
		int formW = 360;
		int blockW = photoSize + gap + formW;
		int blockX = (w - blockW) / 2 + BLOCK_SHIFT_X;
		int blockTop = Math.max(40, (int) (h * 0.18)) + contentOffsetY;

		// 사진
		lblPhoto.setBounds(blockX, blockTop, photoSize, photoSize);

		// 폼(라벨/값)
		int fx = blockX + photoSize + gap;
		int lineH = 32;
		int labW = 48;
		int valW = formW - labW;
		int y = blockTop + 10;

		lName.setBounds(fx, y, labW, lineH);
		lblNameV.setBounds(fx + labW + 14, y, valW - 14, lineH);

		y += 44;
		lEmail.setBounds(fx, y, labW, lineH);
		lblEmailV.setBounds(fx + labW + 14, y, valW - 14, lineH);

		y += 44;
		lGender.setBounds(fx, y, labW, lineH);
		int radiosX = fx + labW + 14;
		rbM.setBounds(radiosX, y, 60, lineH);
		rbF.setBounds(radiosX + 55, y, 60, lineH);
		rbN.setBounds(radiosX + 110, y, 80, lineH);

		// 하단 중앙 둥근 버튼
		int btnW = 120, btnH = 44, btnGap = 18;
		int total = btnW * 2 + btnGap;
		int bx = (w - total) / 2;
		int by = Math.max(blockTop + photoSize + 40, h - 90);
		btnSave.setBounds(bx, by, btnW, btnH);
		btnBack.setBounds(bx + btnW + btnGap, by, btnW, btnH);
	}

	private String currentGender() {
		if (rbM.isSelected())
			return "남";
		if (rbF.isSelected())
			return "여";
		return "비공개";
	}

	private void updatePhoto(String gender) {
		String file = "profile_secret.png";
		if ("남".equals(gender))
			file = "profile_male.png";
		else if ("여".equals(gender))
			file = "profile_female.png";
		lblPhoto.setIcon(loadIconFromFS(file, 160, 160));
		lblPhoto.setText(lblPhoto.getIcon() == null ? "이미지 없음" : "");
	}

	private ImageIcon loadIconFromFS(String filename, int w, int h) {
		java.nio.file.Path p = java.nio.file.Paths.get(System.getProperty("user.dir"), filename);
		if (!java.nio.file.Files.exists(p))
			p = java.nio.file.Paths.get(System.getProperty("user.dir"), "img", filename);
		if (!java.nio.file.Files.exists(p))
			return null;
		ImageIcon icon = new ImageIcon(p.toString());
		Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	/* 둥근 버튼: 기본 Border/Focus 페인팅 제거 */
	static class RoundedButton extends JButton {
		private static final int R = 12;
		private static final Color PRIMARY = new Color(0x4E5BD5);

		RoundedButton(String text) {
			super(text);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false); // ← 각진 기본 테두리 제거
			setFocusPainted(false); // ← 포커스 링 제거
			setForeground(Color.WHITE);
			setFont(getFont().deriveFont(Font.BOLD, 15f));
			setMargin(new Insets(0, 0, 0, 0));
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(PRIMARY);
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), R * 2, R * 2);
			g2.dispose();
			super.paintComponent(g); // 텍스트만 그리게 됨
		}

		@Override
		protected void paintBorder(Graphics g) {
			/* no-op */}
	}
}
