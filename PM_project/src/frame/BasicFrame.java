package frame;

import javax.swing.*;
import java.awt.*;

public class BasicFrame extends JFrame {
	// 싱글톤
	private static JFrame instance;

//	private JPanel contentPane;

	private BasicFrame(JPanel jPanel) {
		int frameHeight = 600;
		int frameWidth = 800;
		// 프레임 닫기 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 창 위치를 중앙으로 설정하기 위함
		Toolkit tk = Toolkit.getDefaultToolkit();
		int monitorWidth = tk.getScreenSize().width;
		int monitorHeight = tk.getScreenSize().height;
		setBounds((monitorWidth - frameWidth) / 2, (monitorHeight - frameHeight) / 2, frameHeight, frameWidth);
		setSize(frameWidth, frameHeight);
		setLocationRelativeTo(null);
		setResizable(false);
		add(jPanel);
	}

	public static JFrame getInstance(JPanel jPanel) {
		if (instance == null) {
			instance = new BasicFrame(jPanel);
		} // null일 때만 new instance 생성
		else {
			instance.getContentPane().removeAll();// 프레임 내부의 패널들을 삭제
			instance.getContentPane().add(jPanel);// 새 패널을 프레임 위에 얹음
			// 컨테이너 배치 관리자에게 자식 컴포넌트를 재배치하도록 지시.
			instance.revalidate();
			instance.repaint();
		}
		return instance;
	}

}
