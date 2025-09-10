package front_frame;

import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JPanel {
    public StudentPanel() {
        setLayout(new FlowLayout());

        JButton detailBtn = new JButton("학생 상세보기");
        add(detailBtn);

        // 버튼 클릭 시 팝업 띄우기
        detailBtn.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "학생 상세정보", true);
            dialog.setSize(350, 400);
            dialog.setLocationRelativeTo(this);

            // 팝업 안에 StudentPopup 붙이기
            dialog.add(new StudentPopup());

            dialog.setVisible(true);
        });
    }
}
