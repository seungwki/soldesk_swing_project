package front_frame;

import javax.swing.*;

import VO.Project;

import java.awt.*;

public class DataInputDialog extends JDialog {
	private JTextField txtSubject, txtTeamName, txtScore;
	private JComboBox<String> comboTeamList;
	private JTextArea txtFilePath;
	private JList<String> listTagAdd, listTagList;
	private DefaultListModel<String> modelTagAdd, modelTagList;
	private JButton btnLoadFile, btnAddTag, btnRemoveTag, btnSave, btnCancel;
	private Project project;

	public DataInputDialog(Window parent, Project project) {
		super(parent, "새 데이터 입력", ModalityType.APPLICATION_MODAL);
		if (parent == null) {

		}
		this.project = project;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 주제
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(new JLabel("주제"), gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		txtSubject = new JTextField(20);
		add(txtSubject, gbc);
		gbc.gridwidth = 1;

		// 팀 목록 & 팀 이름
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(new JLabel("팀 목록"), gbc);
		gbc.gridx = 1;
		comboTeamList = new JComboBox<>();
		add(comboTeamList, gbc);
		gbc.gridx = 2;
		add(new JLabel("팀 이름"), gbc);
		gbc.gridx = 3;
		txtTeamName = new JTextField(10);
		add(txtTeamName, gbc);

		// 파일 추가
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("파일 추가"), gbc);
		gbc.gridx = 1;
		btnLoadFile = new JButton("가져오기");
		add(btnLoadFile, gbc);
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		txtFilePath = new JTextArea("%가져온 파일 경로%");
		txtFilePath.setRows(2);
		txtFilePath.setLineWrap(true);
		add(new JScrollPane(txtFilePath), gbc);
		gbc.gridwidth = 1;

		// 태그 추가/목록
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("태그 추가하기"), gbc);
		gbc.gridx = 2;
		add(new JLabel("태그 목록"), gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		modelTagAdd = new DefaultListModel<>();
		listTagAdd = new JList<>(modelTagAdd);
		add(new JScrollPane(listTagAdd), gbc);

		gbc.gridx = 1;
		JPanel panelBtn = new JPanel(new GridLayout(2, 1, 5, 5));
		btnAddTag = new JButton("추가 ▶");
		btnRemoveTag = new JButton("◀ 제거");
		panelBtn.add(btnAddTag);
		panelBtn.add(btnRemoveTag);
		add(panelBtn, gbc);

		gbc.gridx = 2;
		modelTagList = new DefaultListModel<>();
		listTagList = new JList<>(modelTagList);
		add(new JScrollPane(listTagList), gbc);

		// 점수
		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new JLabel("점수"), gbc);
		gbc.gridx = 1;
		txtScore = new JTextField(5);
		add(txtScore, gbc);

		// 저장 / 취소 버튼
		gbc.gridx = 2;
		gbc.gridy = 6;
		btnSave = new JButton("저장");
		add(btnSave, gbc);
		gbc.gridx = 3;
		btnCancel = new JButton("취소");
		add(btnCancel, gbc);

		pack();
		setLocationRelativeTo(parent);
	}
}
