package front_frame;

import javax.swing.*;
import java.awt.*;

public class StudentPopup extends JPanel {
	public StudentPopup() {
		setLayout(null);

		// 1. 이름
		JLabel nameLabel = new JLabel("이름 : 대상혁");
		nameLabel.setBounds(10, 10, 200, 20);
		add(nameLabel);

		// 2. 조1
		JLabel jo1Label = new JLabel("조1 : skt t1");
		jo1Label.setBounds(10, 40, 200, 20);
		add(jo1Label);

		// 3. 조2
		JLabel jo2Label = new JLabel("조2 : 한화생명");
		jo2Label.setBounds(10, 70, 200, 20);
		add(jo2Label);

		// 4. 조3
		JLabel jo3Label = new JLabel("조3 : 젠지");
		jo3Label.setBounds(10, 100, 200, 20);
		add(jo3Label);

		// 5. tel
		JLabel telLabel = new JLabel("tel : 010-1234-5678");
		telLabel.setBounds(10, 130, 200, 20);
		add(telLabel);

		// 6. 태그 제목
		JLabel tagTitle = new JLabel("태그 : ");
		tagTitle.setBounds(10, 160, 50, 20);
		add(tagTitle);

		// 7. 태그
		JButton tagButton1 = new JButton("s태그");
		tagButton1.setBackground(new Color(102, 204, 102)); // 초록색
		tagButton1.setBounds(50, 160, 70, 25);
		add(tagButton1);

		JButton tagButton2 = new JButton("s태그");
		tagButton2.setBackground(new Color(204, 204, 102)); // 노랑-연두
		tagButton2.setBounds(125, 160, 70, 25);
		add(tagButton2);

		JButton tagButton3 = new JButton("s태그");
		tagButton3.setBackground(new Color(255, 102, 102)); // 빨강
		tagButton3.setBounds(200, 160, 70, 25);
		add(tagButton3);

		JButton addTagButton = new JButton("+");
		addTagButton.setBounds(275, 160, 50, 25);
		addTagButton.setBackground(new Color(0x4e74de)); // 파란색 (#4e74de)
		addTagButton.setForeground(Color.WHITE); // 글자는 흰색으로
		addTagButton.setFocusPainted(false); // 클릭시 테두리 강조 제거
		addTagButton.setBorderPainted(false); // 외곽선 제거
		add(addTagButton);

		// 8. 메모 제목
		JLabel memoTitle = new JLabel("메모");
		memoTitle.setBounds(10, 200, 50, 20);
		add(memoTitle);

		// 9. 메모 (텍스트 작성 가능 + 스크롤)
		JTextArea memoArea = new JTextArea();
		JScrollPane memoScroll = new JScrollPane(memoArea);
		memoScroll.setBounds(10, 230, 350, 190);
		add(memoScroll);

		// 10. 저장 버튼
		JButton saveButton = new JButton("수정");
		saveButton.setBounds(140, 440, 100, 30);

		// 색상 & 스타일 적용
		saveButton.setBackground(new Color(0x4e74de)); // 파란색 (#4e74de)
		saveButton.setForeground(Color.WHITE); // 글자는 흰색
		saveButton.setFocusPainted(false); // 클릭시 테두리 제거
		saveButton.setBorderPainted(false); // 외곽선 제거

		add(saveButton);

		setPreferredSize(new Dimension(400, 490)); // 패널이 실제로 이 정도 크기라고 알려줌

	}
}
