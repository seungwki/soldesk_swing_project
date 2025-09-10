package front_frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddClassDialog extends JDialog {
    private JTextField titleField;
    private JTextField subtitleField;
    private boolean isConfirmed = false;

    public AddClassDialog(Frame owner) {
        super(owner, "새 수업 추가", true);
        setLayout(new BorderLayout());
        setSize(300, 180);
        setLocationRelativeTo(owner);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("수업 이름 (title):"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("부제목 (subtitle):"));
        subtitleField = new JTextField();
        inputPanel.add(subtitleField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnOk = new JButton("추가");
        JButton btnCancel = new JButton("취소");

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);

        add(btnPanel, BorderLayout.SOUTH);

        // 확인 버튼 눌렀을 때
        btnOk.addActionListener(e -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "수업 이름을 입력하세요.");
                return;
            }
            isConfirmed = true;
            setVisible(false);
        });

        // 취소 버튼 눌렀을 때
        btnCancel.addActionListener(e -> {
            isConfirmed = false;
            setVisible(false);
        });
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public String getTitleText() {
        return titleField.getText().trim();
    }

    public String getSubtitleText() {
        return subtitleField.getText().trim();
    }
}
