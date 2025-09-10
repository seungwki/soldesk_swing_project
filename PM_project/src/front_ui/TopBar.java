package front_ui;

import java.awt.*;
import javax.swing.*;

import front_frame.GcProfile;
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
		profilePanel = new GcProfile(80, 80); // 고정 크기(해상도는 나중에 다듬자)
		profilePanel.setBounds(30, 10, 100, 100); // X, Y, W, H
		add(profilePanel);

		// ---------- 내 정보 / 로그 아웃(작은 둥근 버튼) ----------
		btnMyInfo = UIUtil.createStyledButton("내 정보", false);
		btnMyInfo.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		btnMyInfo.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		btnMyInfo.setBounds(10, 90, 60, 25); // 원하는 좌표로 수정 가능
		add(btnMyInfo);
		btnMyInfo.addActionListener(e ->
	    JOptionPane.showMessageDialog(this, "내 정보: 준비 중입니다.")
				);

		btnLogout = UIUtil.createStyledButton("로그아웃", false);
		btnLogout.setFont(new Font("맑은 고딕", Font.BOLD, 13));
		btnLogout.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
		btnLogout.setBounds(75, 90, 65, 25); // 원하는 좌표로 수정 가능
		add(btnLogout);
		btnLogout.addActionListener(e ->
	    JOptionPane.showMessageDialog(this, "로그아웃: 준비 중입니다.")
				);


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
}
