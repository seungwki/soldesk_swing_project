package frame;

import javax.swing.JButton;
import javax.swing.JPanel;

import frame.action.LoginAction;

public class LoginFrame extends JPanel {
	public LoginFrame() {
		// TODO : 로그인 버튼 추가
		// 생성
		JButton login = new JButton("로그인");
		// 부착
		add(login);
		// TODO : 클릭 시 수업 관리 화면으로 이동
		login.addActionListener(new LoginAction());
	}
}