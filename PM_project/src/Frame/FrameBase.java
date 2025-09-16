package Frame;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameBase extends JFrame {
    private static FrameBase instance;

    private FrameBase(JPanel e) {
        setTitle("로그인");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(null); // 유지
        setBounds( // 화면 중앙 배치 직접 계산
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - 400),
            (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - 300),
            800, 600
        );

      
        e.setBounds(0, 0, getWidth(), getHeight());
        add(e);

        setVisible(true);
    }

    public static void getInstance(JPanel e) {
        if (instance == null) {
            instance = new FrameBase(e);
        } else {
            instance.getContentPane().removeAll();
            e.setBounds(0, 0, instance.getWidth(), instance.getHeight()); // ★
            instance.getContentPane().add(e);
            instance.revalidate();
            instance.repaint();
        }
    }

	
}

