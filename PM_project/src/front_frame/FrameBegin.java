// front_frame/FrameBegin.java
package front_frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import VO.Data;

public class FrameBegin extends JPanel {
	// ───────── 화면 크기 ─────────
	private static final int FRAME_W = 800, FRAME_H = 600; // ← 전체 프레임 크기(내용 영역 기준)

	// ───────── 하드코딩 좌표/크기 (원하는 값으로 수정) ─────────
	// 로고
	private static final int LOGO_X = 80; // ← 로고 좌측 위치
	private static final int LOGO_Y = 165; // ← 로고 상단 위치
	private static final int LOGO_W = 200; // ← 로고 가로 크기
	private static final int LOGO_H = LOGO_W; // ← 로고 세로 크기

	// ID 라벨/필드/로그인 버튼
	private static final int ID_L_X = 400; // ← "ID" 라벨 X
	private static final int ID_L_Y = 210; // ← "ID" 라벨 Y
	private static final int ID_F_X = ID_L_X + 30; // ← ID 입력칸 X
	private static final int ID_F_Y = ID_L_Y - 5; // ← ID 입력칸 Y
	private static final int ID_F_W = 200; // ← ID 입력칸 폭
	private static final int ID_F_H = 30; // ← ID 입력칸 높이

	private static final int PW_L_X = ID_L_X; // ← "PW" 라벨 X
	private static final int PW_L_Y = ID_L_Y + 40; // ← "PW" 라벨 Y
	private static final int PW_F_X = ID_F_X; // ← PW 입력칸 X
	private static final int PW_F_Y = ID_F_Y + 40; // ← PW 입력칸 Y
	private static final int PW_F_W = ID_F_W; // ← PW 입력칸 폭
	private static final int PW_F_H = ID_F_H; // ← PW 입력칸 높이

	private static final int LOGIN_X = ID_F_X + ID_F_W + 10; // ← 로그인 버튼 X
	private static final int LOGIN_Y = ID_F_Y; // ← 로그인 버튼 Y (ID~PW 두 줄 높이에 맞춰 배치)
	private static final int LOGIN_W = 96; // ← 로그인 버튼 가로
	private static final int LOGIN_H = ID_F_H + PW_F_H + 10; // ← 로그인 버튼 세로

	// 하단(자동 로그인/회원가입/id/pw 찾기) – 폭을 ID 시작~로그인 버튼 끝과 맞추고 싶다면 X/W 조절
	private static final int AUTO_X = ID_L_X - 4; // ← "자동 로그인 사용" 체크박스 X
	private static final int AUTO_Y = PW_L_Y + 40; // ← "자동 로그인 사용" 체크박스 Y

	private static final int JOIN_X = ID_F_X + ID_F_W - LOGIN_W; // ← "회원가입" 버튼 X
	private static final int JOIN_Y = PW_L_Y + 35; // ← "회원가입" 버튼 Y
	private static final int JOIN_W = LOGIN_W; // ← "회원가입" 버튼 가로
	private static final int JOIN_H = 28; // ← "회원가입" 버튼 세로

	private static final int FIND_X = LOGIN_X; // ← "id/pw 찾기" 버튼 X
	private static final int FIND_Y = JOIN_Y; // ← "id/pw 찾기" 버튼 Y
	private static final int FIND_W = JOIN_W; // ← "id/pw 찾기" 버튼 가로
	private static final int FIND_H = JOIN_H; // ← "id/pw 찾기" 버튼 세로

	// ───────── 필드/버튼 ─────────
	private JTextField tfId;
	private JPasswordField tfPw;
	private JButton btnLogin, btnPost, btnFind;
	private JCheckBox cbAuto;

	public FrameBegin() {
		setPreferredSize(new Dimension(FRAME_W, FRAME_H));
		setBackground(Color.WHITE);
		setLayout(null); // ← 절대좌표 배치(하드코딩)

		// ── 로고 ──
		ImageIcon logoIcon = new ImageIcon(scale(load("logo_temp.png"), LOGO_W, LOGO_H));
		JLabel logo = new JLabel(logoIcon);
		logo.setBounds(LOGO_X, LOGO_Y, LOGO_W, LOGO_H); // ← 로고 위치/크기
		add(logo);

		// ── ID / PW 라벨 ──
		JLabel lbId = new JLabel("ID");
		lbId.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbId.setBounds(ID_L_X, ID_L_Y, 40, 20); // ← "ID" 라벨 위치/크기
		add(lbId);

		JLabel lbPw = new JLabel("PW");
		lbPw.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbPw.setBounds(PW_L_X, PW_L_Y, 40, 20); // ← "PW" 라벨 위치/크기
		add(lbPw);

		// ── 입력 필드 ──
		tfId = new JTextField();
		tfId.setBounds(ID_F_X, ID_F_Y, ID_F_W, ID_F_H); // ← ID 필드 위치/크기
		tfId.setBorder(new LineBorder(Color.BLACK, 1));
		add(tfId);

		tfPw = new JPasswordField();
		tfPw.setBounds(PW_F_X, PW_F_Y, PW_F_W, PW_F_H); // ← PW 필드 위치/크기
		tfPw.setBorder(new LineBorder(Color.BLACK, 1));
		add(tfPw);

		// ── 로그인 버튼 ──
		btnLogin = blueButton("로그인");
		btnLogin.setBounds(LOGIN_X, LOGIN_Y, LOGIN_W, LOGIN_H); // ← 로그인 버튼 위치/크기
		add(btnLogin);

		// ── 하단 컨트롤 ──
		cbAuto = new JCheckBox("자동 로그인 사용");
		cbAuto.setBackground(Color.WHITE);
		cbAuto.setBounds(AUTO_X, AUTO_Y, 115, 20); // ← 자동 로그인 체크 위치/크기
		add(cbAuto);

		btnPost = blueButton("회원가입");
		btnPost.setBounds(JOIN_X, JOIN_Y, JOIN_W, JOIN_H); // ← 회원가입 버튼 위치/크기
		add(btnPost);

		btnFind = blueButton("id/pw 찾기");
		btnFind.setBounds(FIND_X, FIND_Y, FIND_W, FIND_H); // ← id/pw 찾기 버튼 위치/크기
		add(btnFind);

		// 엔터로 로그인
		tfPw.addActionListener(e -> btnLogin.doClick());

		// ===== 아래는 “구성 화면 아님” 그대로 사용 =====
		btnLogin.addActionListener(e -> {
			String id = tfId.getText();
			String pw = new String(tfPw.getPassword());
			boolean success = UserManager.getInstance().login(id, pw);
			if (success) {
				JOptionPane.showMessageDialog(this, "로그인 성공");
				DefaultFrame.getInstance(new ClassManager());
			} else {
				JOptionPane.showMessageDialog(this, "아이디나 비밀번호가 틀렸습니다.");
			}
		});
		btnPost.addActionListener(e -> DefaultFrame.getInstance(new SignUpPage()));
	}

	// ───────── 유틸 ─────────
	private JButton blueButton(String t) {
		JButton b = new JButton(t);
		b.setBackground(new Color(0x4e74de));
		b.setForeground(Color.WHITE);
		b.setFocusPainted(false);
		b.setBorder(new LineBorder(new Color(0x4e74de), 1));
		return b;
	}

	private BufferedImage load(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (Exception e) {
			return new BufferedImage(LOGO_W, LOGO_H, BufferedImage.TYPE_INT_ARGB);
		}
	}

	private Image scale(BufferedImage src, int w, int h) {
		BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dst.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.drawImage(src, 0, 0, w, h, null);
		g2.dispose();
		return dst;
	}
}
