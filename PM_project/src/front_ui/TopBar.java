package front_ui;

import java.awt.*;
import javax.swing.*;

import front_frame.ClassManager;
import front_frame.DefaultFrame;
import front_frame.FrameBegin;
import front_frame.FrameMyInfo;
import front_frame.GcProfile;
import front_frame.UserManager;
import front_util.UIUtil;

// 흰색 TopBar 구성 화면
public class TopBar extends JPanel {
	public interface OnMenuClick {
		void onClass();

		void onStudent();

		void onTag();
	}

	private OnMenuClick menuHandler;
	private final GcProfile profilePanel;
	private final JButton btnMyInfo;
	private final JButton btnLogout;
	private final JButton btnClass;
	private final JButton btnStudent;
	private final JButton btnTag;
	private final JLabel teamLogo;

	public TopBar() {
		setLayout(null);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(800, 120));

		// ---------- 프로필(고정 좌표/크기) ----------
		profilePanel = new GcProfile(80, 80);
		profilePanel.setBounds(30, 10, 80, 80); // ← GcProfile 생성 크기와 일치

		// ▼▼ 추가: 초기 로드 + 변경 알림 연결
		refreshProfile(); // 처음 그릴 때
		UserManager.getInstance().addProfileListener(this::refreshProfile);

		// ---------- 내 정보 / 로그 아웃(작은 둥근 버튼) ----------
		btnMyInfo = UIUtil.createStyledButton("내 정보", false);
		btnMyInfo.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		btnMyInfo.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		btnMyInfo.setBounds(10, 90, 60, 25); // 원하는 좌표로 수정 가능
		add(btnMyInfo);
		btnMyInfo.addActionListener(e -> {
			DefaultFrame.getInstance(new FrameMyInfo());
		});

		btnLogout = UIUtil.createStyledButton("로그아웃", false);
		btnLogout.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		btnLogout.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		btnLogout.setBounds(75, 90, 65, 25); // 원하는 좌표로 수정 가능
		add(btnLogout);

		btnLogout.addActionListener(e -> {
			DefaultFrame.getInstance(new FrameBegin());
		});

		// ---------- 중앙 메뉴(라운드 버튼 3개) ----------
		btnClass = UIUtil.createStyledButton("수업 관리", true);
		btnStudent = UIUtil.createStyledButton("학생 관리", false);
		btnTag = UIUtil.createStyledButton("태그 관리", false);

		// 겹침 피하려면 여기 좌표만 조정하면 됨
		btnClass.setBounds(155, 25, 160, 70);
		btnStudent.setBounds(335, 25, 160, 70);
		btnTag.setBounds(515, 25, 160, 70);

		btnClass.addActionListener(e -> {
			selectOnly("class");
			if (menuHandler != null)
				menuHandler.onClass();
		});
		btnStudent.addActionListener(e -> {
			selectOnly("student");
			if (menuHandler != null)
				menuHandler.onStudent();
		});
		btnTag.addActionListener(e -> {
			selectOnly("tag");
			if (menuHandler != null)
				menuHandler.onTag();
		});

		add(btnClass);
		add(btnStudent);
		add(btnTag);

		// ---------- 우측 팀 로고 ----------
		teamLogo = new JLabel();
		ImageIcon logoSrc = new ImageIcon("logo_temp.png");
		Image logoScaled = logoSrc.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		teamLogo.setIcon(new ImageIcon(logoScaled));
		teamLogo.setBounds(705, 28, 64, 64); // X, Y, W, H
		add(teamLogo);

		// ---------- Z-순서: 프로필을 맨 위로 (겹침 방지) ----------
		setComponentZOrder(profilePanel, 0);
		revalidate();
		repaint();
	}

	/** 눌린 메뉴만 파란색 표시 */
	public void selectOnly(String key) {
		UIUtil.applySelected(btnClass, "class".equalsIgnoreCase(key));
		UIUtil.applySelected(btnStudent, "student".equalsIgnoreCase(key));
		UIUtil.applySelected(btnTag, "tag".equalsIgnoreCase(key));
	}

	public void setMenuHandler(OnMenuClick handler) {
		this.menuHandler = handler;
	}

	// 외부에서 액션 연결할 때 사용
	public JButton getBtnClass() {
		return btnClass;
	}

	public JButton getBtnStudent() {
		return btnStudent;
	}

	public JButton getBtnTag() {
		return btnTag;
	}

	public JButton getBtnMyInfo() {
		return btnMyInfo;
	}

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JLabel getTeamLogo() {
		return teamLogo;
	}

	public GcProfile getProfilePanel() {
		return profilePanel;
	}

	private void refreshProfile() {
		String file = normalizeFS(UserManager.getInstance().getCurrentProfileImagePath());
		ImageIcon icon = loadIconFromFS(file, 80, 80);
		profilePanel.setImage(icon == null ? null : icon.getImage());
		profilePanel.repaint();
	}

	private String normalizeFS(String p) {
		if (p == null)
			return null;
		// 클래스패스 경로처럼 앞에 '/'가 온 경우(예: "/img/xxx.png")를 파일시스템 상대경로로 맞춤
		if (p.startsWith("/"))
			p = p.substring(1);
		return p;
	}

	private ImageIcon loadIconFromFS(String filename, int w, int h) {
		try {
			if (filename == null || filename.isEmpty())
				return null;

			// 절대경로면 그대로 시도
			java.nio.file.Path p;
			java.io.File f = new java.io.File(filename);
			if (f.isAbsolute()) {
				p = f.toPath();
			} else {
				// 상대경로면 여러 후보 경로 시도
				java.nio.file.Path base = java.nio.file.Paths.get(System.getProperty("user.dir")).toAbsolutePath();
				java.nio.file.Path p1 = base.resolve(filename); // ./filename
				java.nio.file.Path p2 = base.resolve("img").resolve(filename); // ./img/filename
				java.nio.file.Path p3 = base.getParent() == null ? null : base.getParent().resolve(filename); // ../filename
				java.nio.file.Path p4 = base.getParent() == null ? null : base.getParent().resolve("img").resolve(filename); // ../img/filename

				java.util.List<java.nio.file.Path> candidates = java.util.Arrays.asList(p1, p2, p3, p4);
				p = null;
				for (java.nio.file.Path c : candidates) {
					if (c != null && java.nio.file.Files.exists(c)) {
						p = c;
						break;
					}
				}
				if (p == null) {
					System.out.println("[TopBar] 이미지 못 찾음. user.dir=" + base);
					System.out.println("  시도경로:");
					for (java.nio.file.Path c : candidates)
						if (c != null)
							System.out.println("   - " + c);
					return null;
				}
			}

			System.out.println("[TopBar] 이미지 로드: " + p.toAbsolutePath());
			ImageIcon icon = new ImageIcon(p.toString());
			Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
			return new ImageIcon(img);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
