package front_frame;

import javax.swing.*;
import java.awt.*;

public class TeamPopup extends JPanel {
    public TeamPopup() {
        setLayout(null);

        // 1. 이름
        JLabel nameLabel = new JLabel("이름 :");
        nameLabel.setBounds(10, 10, 50, 20);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(70, 10, 90, 25);
        add(nameField);

        JButton addCsvButton = new JButton("csv로 추가");
        addCsvButton.setBounds(165, 10, 100, 25);
        addCsvButton.setBackground(new Color(0x4e74de)); // 글자색상 적용
        add(addCsvButton);
        
        addCsvButton.setForeground(Color.WHITE);      // 글자 흰색
        addCsvButton.setFocusPainted(false);          // 눌렀을 때 파란 테두리 제거
        addCsvButton.setBorderPainted(false);         // 외곽선 제거



        // 2. 전화번호
        JLabel telLabel = new JLabel("tel :");
        telLabel.setBounds(10, 45, 50, 25);
        add(telLabel);

        JTextField telField = new JTextField();
        telField.setBounds(70, 45, 120, 25);
        add(telField);

        // 3. 태그
        JLabel tagLabel = new JLabel("태그");
        tagLabel.setBounds(10, 80, 50, 25);
        add(tagLabel);

        JButton tagButton1 = new JButton("s태그");
        tagButton1.setBackground(new Color(102, 204, 102)); // 초록색
        tagButton1.setBounds(10, 110, 70, 25);
        add(tagButton1);

        JButton tagButton2 = new JButton("s태그");
        tagButton2.setBackground(new Color(204, 204, 102)); // 노랑-연두
        tagButton2.setBounds(85, 110, 70, 25);
        add(tagButton2);

        JButton tagButton3 = new JButton("s태그");
        tagButton3.setBackground(new Color(255, 102, 102)); // 빨강
        tagButton3.setBounds(160, 110, 70, 25);
        add(tagButton3);

        JButton addTagButton = new JButton("+");
        addTagButton.setBounds(235,110, 50, 25);
        addTagButton.setBackground(new Color(0x4e74de)); // 파란색 (#4e74de)
        addTagButton.setForeground(Color.WHITE);        // 글자는 흰색으로
        addTagButton.setFocusPainted(false);            // 클릭시 테두리 강조 제거
        addTagButton.setBorderPainted(false);           // 외곽선 제거
        add(addTagButton);


        // 4. 메모
        JLabel memoLabel = new JLabel("메모");
        memoLabel.setBounds(10, 165, 50, 25);
        add(memoLabel);

        JTextArea memoArea = new JTextArea();
        JScrollPane memoScroll = new JScrollPane(memoArea);
        memoScroll.setBounds(10, 190, 350, 120);
        add(memoScroll);

     // 5. 저장 버튼
        JButton saveButton = new JButton("저장");
        saveButton.setBounds(140, 320, 100, 30);

        // 👉 색상 & 스타일 적용
        saveButton.setBackground(new Color(0x4e74de)); // 파란색 (#4e74de)
        saveButton.setForeground(Color.WHITE);         // 글자는 흰색
        saveButton.setFocusPainted(false);             // 클릭시 테두리 제거
        saveButton.setBorderPainted(false);            // 외곽선 제거

        add(saveButton);


        // ✅ 스크롤용 크기 지정
        setPreferredSize(new Dimension(400, 330));
    }
}
