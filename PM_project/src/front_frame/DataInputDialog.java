package front_frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import VO.Output;
import VO.Project;
import VO.Tag;
import VO.TagRepository;
import VO.Team;

public class DataInputDialog extends JDialog {
	private JTextField txtSubject, txtTeamName, txtScore, txtMaxScore;
	private JComboBox<String> comboTeamList;
	private JTextArea txtFilePath;
	private JList<String> listTagAdd, listTagList;
	//	private DefaultListModel<String> modelTagAdd, modelTagList, fileNameList;
	private JButton btnLoadFile, btnAddTag, btnRemoveTag, btnSave, btnCancel;
	private Project project;
	private Team team;
	private Output output;
	private int degree;
	private static final Dimension DIALOG_SIZE = new Dimension(600, 700);
	private static final Color BG_FRAME = new Color(0xDDE3F4);
	private static final Color CARD_FILL = new Color(0xF1F3FA);
	private static final Color CARD_LINE = new Color(0xC7D1EA);
	private static final Color BLUE = new Color(0x4E74DE);
	private static final Font FONT_LBL = new Font("맑은 고딕", Font.BOLD, 16);
	private static final Font FONT_BTN = new Font("맑은 고딕", Font.BOLD, 15);
	private static final int FILES_H = 80; // 파일 목록
	private static final int TAGS_H = 100; // 태그 리스트
	private static final int FIELD_H = 32; // 텍스트필드/콤보 높이
	private static final int SCORE_H = 22; // 점수칸 높이
	private double newMaxScore, newScore;

	public DataInputDialog(Window parent, Project project) {
		this(parent, project, null, null);
	}

	public DataInputDialog(Window parent, Project project, Team team, Integer degree) {
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
		//======= BACK ========
		this.project = project;
		//		if (team == null) {
		//			this.team = new Team(null, degree);
		//		} else {
		this.team = team;
		//		}
		this.degree = degree;
		Comparator<Tag> tagComparator = new Comparator<Tag>() {
			@Override
			public int compare(Tag o1, Tag o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};
		ArrayList<File> fileList = new ArrayList<>();
		if (team != null && team.getOutput() != null) {
			this.output = team.getOutput();
			//		} else {
			//			Output newOutput = new Output(fileList);
			//			team.setOutput(newOutput);
		}
		//주제(title), 팀 목록, 팀 이름, 파일, 태그<<이거 좀 골때림, 점수, 최대 점수
		String title;
		DefaultListModel<String> teamListModel = new DefaultListModel<>();
		ArrayList<Team> teamList = new ArrayList<>();
		String teamName;
		DefaultListModel<String> fileListModel = new DefaultListModel<>();
		DefaultListModel<String> leftTagListModel = new DefaultListModel<>();
		DefaultListModel<String> rightTagListModel = new DefaultListModel<>();
		ArrayList<Tag> leftTagList = new ArrayList<Tag>();
		leftTagList.addAll(TagRepository.getOutputTags());
		ArrayList<Tag> rightTagList = new ArrayList<Tag>();
		double score;
		double maxScore;
		for (int i = 0; i < project.getTeams2().size(); i++) {
			if (project.getTeams2().get(i).getOutput() != null && project.getTeams2().get(i).getDegree() == degree) {
				teamListModel.addElement(project.getTeams2().get(i).getTName());
				teamList.add(project.getTeams2().get(i));
			}
		}

		if (output != null) {
			title = output.getTitle();
			score = output.getScore();
			maxScore = output.getMaxScore();
			//fileList
			fileList.sort(new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			for (File file : fileList) {
				fileListModel.addElement(file.getName());
			}
			//tagList
			rightTagList.addAll(team.getOutput().getTagList());
			for (int i = 0; i < rightTagList.size(); i++) {
				if (leftTagList.contains(rightTagList.get(i))) {
					leftTagList.remove(rightTagList.get(i));
				}
			}
		}
		for (int i = 0; i < leftTagList.size(); i++) {
			leftTagListModel.addElement(leftTagList.get(i).getName());
		}
		for (int i = 0; i < rightTagList.size(); i++) {
			rightTagListModel.addElement(rightTagList.get(i).getName());
		}
		if (team != null) {
			teamName = team.getTName();
		}
		//======= BACK ========

		// 주제
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 0;
		card.add(label("주제"), c);
		txtSubject = new JTextField();
		if (team != null) {
			if (team.getOutput() != null) {
				txtSubject.setText(team.getOutput().getTitle());
			}
		}
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
		if (team != null) {
			comboTeamList.setEnabled(false);
		}
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
		if (team != null) {
			txtTeamName.setText(team.getTName());
			txtTeamName.setEditable(false);
		}
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

		txtFilePath = new JTextArea();
		txtFilePath.setEditable(false);//비활성화
		txtFilePath.setBackground(new Color(0xEEEEEE));//색 변경
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
		JList<String> fileJlist = new JList<>(fileListModel);
		fileJlist.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				int index = fileJlist.locationToIndex(e.getPoint()); // 클릭한 위치의 인덱스
				if (index != -1) {
					if (e.getClickCount() == 2) {
						fileListModel.remove(index);
						fileList.remove(index);
					}
				}
			}
		});
		fileJlist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		JScrollPane spFiles = new JScrollPane(fileJlist);
		spFiles.setPreferredSize(new Dimension(300, FILES_H));
		styleScrollBorder(spFiles, "파일 목록(제거하려면 더블클릭)");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 4;
		c.weightx = 1;
		c.weighty = 0.23;
		c.fill = GridBagConstraints.BOTH;
		card.add(spFiles, c);
		btnLoadFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog((Frame) null, "산출물 선택", FileDialog.LOAD);
				fileDialog.setVisible(true);
				File selectedFile = new File(fileDialog.getFile());
				if (selectedFile != null) {
					fileList.add(selectedFile);
					fileListModel.addElement(selectedFile.getName());
					txtFilePath.setText(selectedFile.getAbsolutePath());
				}
			}
		});

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

		listTagAdd = new JList<>(leftTagListModel);
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

		listTagList = new JList<>(rightTagListModel);
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
		//선택하면 반대쪽을 해제
		listTagAdd.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				listTagList.clearSelection();
			}
		});
		listTagList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				listTagAdd.clearSelection();
			}
		});
		//추가, 제거 버튼 기능 구현
		//		btnAddTag.addActionListener(new ActionListener() {//추가 버튼
		//			@Override
		//			public void actionPerformed(ActionEvent e) {
		//				if (listTagList.getSelectedValue() != null) {//뭔가가 선택됐을 때 추가하시오
		//					String addTagname = listTagList.getSelectedValue();
		//					Tag tempTag = null;//선택한 태그 그 자체를 찾음
		//					for (int i = 0; i < leftTagList.size(); i++) {
		//						if (leftTagList.get(i).getName().equals(addTagname)) {
		//							tempTag = leftTagList.get(i);
		//							break;
		//						}
		//					}
		//					//right model에 추가하기
		//					rightTagListModel.addElement(addTagname);
		//					//right list에 추가하기
		//					rightTagList.add(tempTag);
		//					//left model에 제거하기
		//					leftTagListModel.removeElement(addTagname);
		//					//left list에 제거하기
		//					leftTagList.remove(tempTag);
		//				}
		//			}
		//		});
		btnAddTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listTagAdd.getSelectedValue() != null) { // 왼쪽 리스트에서 선택된 항목
					String addTagname = listTagAdd.getSelectedValue();
					Tag tempTag = null;
					for (int i = 0; i < leftTagList.size(); i++) {
						if (leftTagList.get(i).getName().equals(addTagname)) {
							tempTag = leftTagList.get(i);
							break;
						}
					}
					if (tempTag != null) {
						// 오른쪽으로 이동
						rightTagListModel.addElement(addTagname);
						rightTagList.add(tempTag);
						leftTagListModel.removeElement(addTagname);
						leftTagList.remove(tempTag);
					}
				}
			}
		});

		btnRemoveTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listTagList.getSelectedValue() != null) { // 오른쪽 리스트에서 선택된 항목
					String removeTagname = listTagList.getSelectedValue();
					Tag tempTag = null;
					for (int i = 0; i < rightTagList.size(); i++) {
						if (rightTagList.get(i).getName().equals(removeTagname)) {
							tempTag = rightTagList.get(i);
							break;
						}
					}
					if (tempTag != null) {
						// 왼쪽으로 이동
						leftTagListModel.addElement(removeTagname);
						leftTagList.add(tempTag);
						rightTagListModel.removeElement(removeTagname);
						rightTagList.remove(tempTag);
					}
				}
			}
		});

		// 점수(가운데 정렬)
		JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
		scoreRow.setOpaque(false);
		JLabel lbScore = label("점수");
		JTextField scoreLeft = new JTextField();
		JTextField scoreRight = new JTextField();
		if (output != null) {
			scoreLeft.setText(String.valueOf(output.getScore()));
			scoreRight.setText(String.valueOf(output.getMaxScore()));
		}
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

		//=========== 점수 =================
		//최대점수는 음수가 될 수 없음.
		//점수 칸에 양수만
		((AbstractDocument) scoreLeft.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (string.matches("\\d+")) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (text.matches("\\d+")) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		((AbstractDocument) scoreRight.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (string.matches("\\d+")) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (text.matches("\\d+")) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
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
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!scoreRight.getText().equals("")) {
					newMaxScore = Double.parseDouble(scoreRight.getText());
				} else {
					newMaxScore = 0;
				}
				if (!scoreLeft.getText().equals("")) {
					newScore = Double.parseDouble(scoreLeft.getText());
				} else {
					newScore = 0;
				}

				// 팀이 없으면 새로 생성
				boolean isNewTeam = false;
				if (DataInputDialog.this.team == null) {
					if (!txtTeamName.getText().equals("")) {
						DataInputDialog.this.team = new Team(txtTeamName.getText(), DataInputDialog.this.degree);
					} else {
						DataInputDialog.this.team = new Team(null, DataInputDialog.this.degree);
					}
					isNewTeam = true; // 새 팀이라는 표시
				}

				// Output 없으면 새로 생성
				if (DataInputDialog.this.output == null) {
					DataInputDialog.this.output = new Output(fileList);
					DataInputDialog.this.team.setOutput(DataInputDialog.this.output);
				}

				if (newScore > newMaxScore) {
					JOptionPane.showMessageDialog(null, "점수는 최대 점수보다 클 수 없습니다.", "잘못 된 입력", JOptionPane.PLAIN_MESSAGE);
					return;
				}

				// Output 값 갱신
				if (!txtSubject.getText().equals("")) {
					output.setTitle(txtSubject.getText());
				} else {
					if (fileList.size() != 0) {
						output.setTitle(fileList.get(0).getName());
					}
				}
				output.setScore(newScore);
				output.setMaxScore(newMaxScore);
				output.setFile(fileList);
				output.setTagList(rightTagList);

				// 새 팀일 때만 추가
				if (isNewTeam) {
					project.addTeam(DataInputDialog.this.team);
				}

				JOptionPane.showMessageDialog(null, "저장되었습니다.", "완료", JOptionPane.PLAIN_MESSAGE);
				DefaultFrame.getInstance(new ClassManagerCardViewer(project, degree - 1));
				dispose();
			}
		});

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
