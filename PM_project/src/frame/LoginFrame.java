package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import front_frame.ClassManager;
import front_frame.DefaultFrame;

public class LoginFrame extends JPanel {
	public LoginFrame() {
		// TODO : 로그인 버튼 추가
		// 생성
		JButton login = new JButton("로그인");
		// 부착
		add(login);
		// TODO : 클릭 시 수업 관리 화면으로 이동
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultFrame.getInstance(new ClassManager());
			}
		});
	}
}