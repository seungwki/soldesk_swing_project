package front_frame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import VO.User;

public class SignUpPage extends JPanel {
	public SignUpPage() {
		setLayout(null); // 현재 패널에서 직접 구성
		setPreferredSize(new Dimension(800, 600)); // 보이도록 설정

		setBackground(Color.WHITE);

		ImageIcon icon = new ImageIcon("logo_temp.png");

		// 원하는 크기로 축소 (예: 100x60)
		Image img = icon.getImage().getScaledInstance(60, 50, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(img);

		JLabel imageLabel = new JLabel(scaledIcon);
		imageLabel.setBounds(380, 30, 60, 50); // 반드시 이미지 크기와 동일하게!

		add(imageLabel);

		JLabel labelId = new JLabel("ID");
		JTextField tfId = new JTextField();
		labelId.setBounds(275, 98, 100, 30);
		tfId.setBounds(312, 100, 200, 30);

		JLabel labelPw1 = new JLabel("PW");
		JPasswordField tfPw1 = new JPasswordField();
		JLabel labelpw = new JLabel("영문/숫자/특수문자 포함 8~16자");
		labelPw1.setBounds(270, 148, 100, 30);
		labelpw.setBounds(310, 180, 260, 20);
		tfPw1.setBounds(312, 150, 200, 30);

		JLabel labelPw2 = new JLabel("PW 확인");
		JPasswordField tfPw2 = new JPasswordField();
		labelPw2.setBounds(245, 197, 100, 30);
		tfPw2.setBounds(312, 200, 200, 30);

		JLabel labelName = new JLabel("이름");
		JTextField tfName = new JTextField();
		labelName.setBounds(265, 247, 100, 30);
		tfName.setBounds(312, 250, 200, 30);

		JLabel labelEmail = new JLabel("E-MAIL");
		JTextField tfEmail = new JTextField();
		labelEmail.setBounds(250, 297, 100, 30);
		tfEmail.setBounds(312, 300, 200, 30);

		JLabel labelSex = new JLabel("성별");
		JCheckBox tfSex1 = new JCheckBox("남");
		JCheckBox tfSex2 = new JCheckBox("여");
		JCheckBox tfSex3 = new JCheckBox("비공개", true);
		ButtonGroup bgSex = new ButtonGroup();

		bgSex.add(tfSex1);
		bgSex.add(tfSex2);
		bgSex.add(tfSex3);

		labelSex.setBounds(265, 350, 100, 30);
		tfSex1.setBounds(320, 350, 60, 30);
		tfSex2.setBounds(380, 350, 60, 30);
		tfSex3.setBounds(440, 350, 80, 30);

		JLabel labelAgree = new JLabel("개인정보 수집 동의");
		JCheckBox tfa1 = new JCheckBox("동의");
		labelAgree.setBounds(263, 400, 150, 30);
		tfa1.setBounds(450, 400, 80, 30);

		for (JCheckBox cb : new JCheckBox[] { tfSex1, tfSex2, tfSex3, tfa1 }) {
			cb.setOpaque(false);
		}

		// 보기 (하이퍼링크)
		JLabel dummyDisagree = new JLabel("<html><u>보기</u></html>");
		dummyDisagree.setForeground(Color.BLUE); // 파란색으로 하이퍼링크처럼
		dummyDisagree.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스를 손모양으로
		dummyDisagree.setBounds(395, 399, 60, 30); // 위치와 크기 설정

		// 클릭해도 아무 일도 일어나지 않음 (더미라서)
		dummyDisagree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 아무 동작도 하지 않음
			}
		});

		add(dummyDisagree);

		// -- 가입, 뒤로가기 버튼 색상,폰트 --
		JButton btnSubmit = new JButton("가입하기");
		JButton btnBack = new JButton("뒤로가기");

		btnSubmit.setBounds(300, 460, 100, 40);
		btnBack.setBounds(420, 460, 100, 40);

		// 스타일 적용
		Color mainBlue = new Color(0x4e74de);

		btnSubmit.setBackground(mainBlue);
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setFocusPainted(false);
		btnSubmit.setBorderPainted(false);

		btnBack.setBackground(mainBlue);
		btnBack.setForeground(Color.WHITE);
		btnBack.setFocusPainted(false);
		btnBack.setBorderPainted(false);

		// 필요하면 폰트도 추가
		btnSubmit.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		btnBack.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		// --- 여기까지 ----

		// 컴포넌트 추가
		add(labelId);
		add(tfId);
		add(labelPw1);
		add(tfPw1);
		add(labelPw2);
		add(tfPw2);
		add(labelName);
		add(tfName);
		add(labelEmail);
		add(tfEmail);
		add(labelSex);
		add(tfSex1);
		add(tfSex2);
		add(tfSex3);
		add(labelAgree);
		add(tfa1);
		add(btnSubmit);
		add(btnBack);
		add(labelpw);

		// 버튼 동작
		btnSubmit.addActionListener(e -> {
			String id = tfId.getText();
			String pw1 = new String(tfPw1.getPassword());
			String pw2 = new String(tfPw2.getPassword());
			String name = tfName.getText();
			String email = tfEmail.getText();
			boolean agreed = tfa1.isSelected();

			String gender = null;
			if (tfSex1.isSelected())
				gender = "남";
			else if (tfSex2.isSelected())
				gender = "여";
			else if (tfSex3.isSelected())
				gender = "비공개";

			if (id.isEmpty() || pw1.isEmpty() || name.isEmpty() || gender == null || email.isEmpty()) {
				JOptionPane.showMessageDialog(this, "모든 항목을 입력해주세요.");
				return;
			}

			if (!checkVaildPassword(pw1)) {
				JOptionPane.showMessageDialog(this, "비밀번호는 8~16자, 영문/숫자/특수문자 포함이어야 합니다.");
				return;
			}

			if (!pw1.equals(pw2)) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
				return;
			}

			if (!isValidEmail(email)) { // (옵션) 이메일 형식 체크
				JOptionPane.showMessageDialog(this, "이메일 형식이 올바르지 않습니다.");
				return;
			}

			if (!agreed) {
				JOptionPane.showMessageDialog(this, "개인정보 수집에 동의해주세요.");
				return;
			}

			if (UserManager.getInstance().isDuplicate(id)) {
				JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
				return;
			}

			User user = new User(id, pw1, name, gender, email, agreed);
			UserManager.getInstance().addUser(user);
			UserManager.getInstance().setCurrentUser(user.getId());

			JOptionPane.showMessageDialog(this, "회원가입 완료!");
			DefaultFrame.getInstance(new FrameBegin());
		});

		btnBack.addActionListener(e -> {
			DefaultFrame.getInstance(new FrameBegin());
		});
	}

	public static boolean checkVaildPassword(String userPw) {
		boolean result = false;

		Pattern passwordPtn = Pattern.compile("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,16}$");
		Matcher passMatcher = passwordPtn.matcher(userPw);

		if (passMatcher.find()) {
			result = true;
			return result;
		} else {
			return result;
		}
	}

	private static boolean isValidEmail(String email) {
		// 매우 간단한 수준의 형식 체크 (필요시 강화)
		return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
	}
}