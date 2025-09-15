package front_frame;

import javax.swing.*;

// 기본 화면 틀
public class DefaultFrame extends JFrame {
	private static DefaultFrame instance;

	public DefaultFrame(JPanel e) {
		setTitle("TeachCloud");
		getContentPane().setLayout(null);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);

		e.setBounds(0, 0, 800, 600);
		getContentPane().add(e);

		getContentPane().validate();
		validate();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void getInstance(JPanel e) {
		if (instance == null) {
			instance = new DefaultFrame(e);
			return;
		}
		instance.getContentPane().removeAll();

		e.setBounds(0, 0, 800, 600);
		instance.getContentPane().add(e);

		instance.getContentPane().validate();
		instance.validate();
		instance.repaint();
	}
}
