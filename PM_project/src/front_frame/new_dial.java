package front_frame;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class new_dial extends JDialog {
	private JTextField txtSubject, txtTeamName, txtScore;
	private JComboBox<String> comboTeamList;
	private JTextArea txtFilePath;
	private JList<String> listTagAdd, listTagList;
	private DefaultListModel<String> modelTagAdd, modelTagList;
	private JButton btnLoadFile, btnAddTag, btnRemoveTag, btnSave, btnCancel;
	// 고정 크기
	private static final Dimension DIALOG_SIZE = new Dimension(600, 700);
	// 팔레트
	private static final Color BG_FRAME = new Color(0xDDE3F4);
	private static final Color CARD_FILL = new Color(0xF1F3FA);
	private static final Color CARD_LINE = new Color(0xC7D1EA);
	private static final Color BLUE = new Color(0x4E74DE);
	private static final Font FONT_LBL = new Font("맑은 고딕", Font.BOLD, 16);
	private static final Font FONT_BTN = new Font("맑은 고딕", Font.BOLD, 15);
	// 높이(짤림 방지로 조금 더 줄임)
	private static final int FILES_H = 80; // 파일 목록
	private static final int TAGS_H = 100; // 태그 리스트
	private static final int FIELD_H = 32; // 텍스트필드/콤보 높이
	private static final int SCORE_H = 22; // 점수칸 높이

	public new_dial(Window parent) {
		super(parent, "새 데이터 입력", ModalityType.APPLICATION_MODAL);

		setPreferredSize(DIALOG_SIZE);
		setMinimumSize(DIALOG_SIZE);
		setSize(DIALOG_SIZE);
		getContentPane().setBackground(BG_FRAME);
		getContentPane().setLayout(new BorderLayout());

		// 중앙 카드
		JPanel card = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int r = 12;
				g2.setColor(CARD_FILL);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
				g2.setColor(CARD_LINE);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, r, r);
				g2.dispose();
			}
		};
		card.setOpaque(false);
		// 좌우 여백 살짝 줄여 가용 폭 확보
		card.setBorder(new EmptyBorder(12, 10, 12, 10));
		getContentPane().add(card, BorderLayout.CENTER);

		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(6, 6, 6, 6);
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;

		// 주제
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(label("주제"), c);
		txtSubject = new JTextField();
		txtSubject.setPreferredSize(new Dimension(0, FIELD_H)); // ← 높이 키움
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.weightx = 1;
		card.add(txtSubject, c);

		// 팀 목록 / 팀 이름
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(label("팀 목록"), c);

		comboTeamList = new JComboBox<>();
		comboTeamList.setPreferredSize(new Dimension(160, FIELD_H)); // ← 높이 32
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.4;
		card.add(comboTeamList, c);

		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(label("팀 이름"), c);

		txtTeamName = new JTextField();
		txtTeamName.setPreferredSize(new Dimension(0, FIELD_H)); // ← 높이 키움
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0.6;
		card.add(txtTeamName, c);

		// 파일 추가
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(label("파일 추가"), c);

		btnLoadFile = primaryButton("가져오기", 100, 30);
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(btnLoadFile, c);

		txtFilePath = new JTextArea("%가져온 파일 경로%");
		txtFilePath.setLineWrap(true);
		txtFilePath.setWrapStyleWord(true);
		txtFilePath.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		JScrollPane spPath = new JScrollPane(txtFilePath);
		styleScrollBorder(spPath, "파일 경로");
		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 2;
		c.weightx = 1;
		card.add(spPath, c);

		// 파일 목록
		JTextArea taFiles = new JTextArea();
		taFiles.setEditable(false);
		taFiles.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		JScrollPane spFiles = new JScrollPane(taFiles);
		spFiles.setPreferredSize(new Dimension(300, FILES_H));
		styleScrollBorder(spFiles, "파일 목록");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0.23;
		c.fill = GridBagConstraints.BOTH;
		card.add(spFiles, c);

		// 태그 타이틀
		JPanel tagTitleRow = new JPanel(new GridLayout(1, 2));
		tagTitleRow.setOpaque(false);
		tagTitleRow.add(label("태그 추가하기"));
		tagTitleRow.add(label("태그 목록"));
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		card.add(tagTitleRow, c);

		// 태그 영역
		JPanel tagRow = new JPanel(new GridBagLayout());
		tagRow.setOpaque(false);
		GridBagConstraints tc = new GridBagConstraints();
		tc.insets = new Insets(4, 4, 4, 4);

		DefaultListModel<String> mdlL = new DefaultListModel<>();
		for (int i = 1; i <= 6; i++)
			mdlL.addElement("새 태그 " + i);
		listTagAdd = new JList<>(mdlL);
		JScrollPane spLeft = new JScrollPane(listTagAdd);
		spLeft.setPreferredSize(new Dimension(240, TAGS_H));
		stylePlainBorder(spLeft);
		tc.gridx = 0;
		tc.gridy = 0;
		tc.weightx = 0.45;
		tc.weighty = 1;
		tc.fill = GridBagConstraints.BOTH;
		tagRow.add(spLeft, tc);

		JPanel midBtns = new JPanel(new GridLayout(2, 1, 6, 6));
		midBtns.setOpaque(false);
		btnAddTag = primaryButton("추가 ▶", 90, 32);
		btnRemoveTag = primaryButton("◀ 제거", 90, 32);
		midBtns.add(btnAddTag);
		midBtns.add(btnRemoveTag);
		tc.gridx = 1;
		tc.gridy = 0;
		tc.weightx = 0.1;
		tc.fill = GridBagConstraints.NONE;
		tc.anchor = GridBagConstraints.CENTER;
		tagRow.add(midBtns, tc);

		DefaultListModel<String> mdlR = new DefaultListModel<>();
		for (int i = 1; i <= 4; i++)
			mdlR.addElement("기존 태그 " + i);
		listTagList = new JList<>(mdlR);
		JScrollPane spRight = new JScrollPane(listTagList);
		spRight.setPreferredSize(new Dimension(240, TAGS_H));
		stylePlainBorder(spRight);
		tc.gridx = 2;
		tc.gridy = 0;
		tc.weightx = 0.45;
		tc.weighty = 1;
		tc.fill = GridBagConstraints.BOTH;
		tagRow.add(spRight, tc);

		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0.42;
		c.fill = GridBagConstraints.BOTH;
		card.add(tagRow, c);

		// 점수(가운데 정렬)
		JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		scoreRow.setOpaque(false);
		JLabel lbScore = label("점수");
		JTextField scoreLeft = new JTextField();
		JTextField scoreRight = new JTextField();
		Dimension scoreSize = new Dimension(80, SCORE_H);
		scoreLeft.setPreferredSize(scoreSize);
		scoreRight.setPreferredSize(scoreSize);
		JLabel slash = new JLabel("/");
		slash.setFont(FONT_LBL);

		scoreRow.add(lbScore);
		scoreRow.add(scoreLeft);
		scoreRow.add(slash);
		scoreRow.add(scoreRight);

		txtScore = new JTextField(); // 호환용
		txtScore.setVisible(false);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		card.add(scoreRow, c);

		// 하단 버튼(중앙)
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
		bottom.setOpaque(false);
		btnSave = primaryButton("저장", 100, 24);
		btnCancel = primaryButton("취소", 100, 24);
		bottom.add(btnSave);
		bottom.add(btnCancel);

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(8, 6, 6, 6); // 아래 여백 확보
		card.add(bottom, c);

		setLocationRelativeTo(parent);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private JLabel label(String t) {
		JLabel l = new JLabel(t);
		l.setFont(FONT_LBL);
		return l;
	}

	private JButton primaryButton(String text, int w, int h) {
		JButton b = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
				super.paintComponent(g);
				g2.dispose();
			}

			@Override
			protected void paintBorder(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getBackground().darker());
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
				g2.dispose();
			}

			@Override
			public boolean isOpaque() {
				return false;
			}
		};
		b.setPreferredSize(new Dimension(w, h));
		b.setBackground(BLUE);
		b.setForeground(Color.WHITE);
		b.setFont(FONT_BTN);
		b.setFocusPainted(false);
		b.setBorderPainted(false);
		return b;
	}

	private void styleScrollBorder(JScrollPane sp, String title) {
		TitledBorder tb = BorderFactory.createTitledBorder(new LineBorder(CARD_LINE), title, TitledBorder.LEFT, TitledBorder.TOP, FONT_LBL, Color.DARK_GRAY);
		sp.setBorder(tb);
	}

	private void stylePlainBorder(JScrollPane sp) {
		sp.setBorder(new LineBorder(CARD_LINE));
	}
}