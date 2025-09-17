package front_frame;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import javax.management.modelmbean.ModelMBean;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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

public class CRUD implements ActionListener {
	public final static int CREATE_OUTPUT = (int) 'c';
	public final static int EDIT_OUTPUT = (int) 'e';
	public final static int DELETE_OUTPUT = (int) 'd';
	private int input;
	private Project project;
	private Team team;
	private Output output;

	public CRUD(int input, Project project, Team team, Output output) {
		this.input = input;
		this.project = project;
		this.team = team;
		this.output = output;
	}//생성자

	@Override
	public void actionPerformed(ActionEvent e) {
		// 삭제
		if (input == 'd') {//아무튼 삭제됨
			// TODO : 확인창 팝업 띄우고, yes라면 팀 삭제 진행
			int confirm = JOptionPane.showConfirmDialog(null, team.getTName() + " 삭제 하시겠습니까?", "팀 삭제 확인", JOptionPane.OK_CANCEL_OPTION);
			if (confirm == 0) {
				for (int i = 0; i < project.getTeams2().size(); i++) {
					if (project.getTeams2().get(i).getTName().equals(team.getTName()) && project.getTeams2().get(i).getDegree() == team.getDegree()) {
						project.getTeams2().remove(team);
						JOptionPane.showMessageDialog(null, "삭제되었습니다.", null, JOptionPane.PLAIN_MESSAGE);
						// 화면 다시 그리고 break;
						BasicFrame.getInstance(new showTeamList(project));
						break;
					}
				}
			}
		} // if(delete)
		else {
			// 팀 이름, 주제, 파일, 팀목록(조건부), 태그 교환, 점수(만점), 버튼
			JDialog dialog = new JDialog((Frame) null, "새 팀 생성", true);
			int width, height;
			width = 600;
			height = 450;
			dialog.setSize(width, height);
			dialog.setLayout(new GridLayout(0, 1));
			// 팀 이름, 주제
			JPanel titlePanel = new JPanel();
			JLabel teamNameLabel = new JLabel("팀 이름");
			JTextField teamNameContent = new JTextField(10);
			JLabel subjectLabel = new JLabel("주제");
			JTextField subjectContent = new JTextField(30);
			titlePanel.add(teamNameLabel);
			titlePanel.add(teamNameContent);
			titlePanel.add(subjectLabel);
			titlePanel.add(subjectContent);
			if (output != null) {
				subjectContent.setText(output.getTitle());

			}
			if (team != null) {
				teamNameContent.setText(team.getTName());
			}
			dialog.add(titlePanel);

			// 파일
			JPanel filePanel = new JPanel();
			filePanel.setLayout(new GridLayout(0, 1));
			JLabel fileLabel = new JLabel("파일");
			JLabel fileName = new JLabel("선택하기");
			JButton removeFile = new JButton("선택 파일 제거");
			JButton browseFile = new JButton("찾아보기");
			ArrayList<File> fileList;
			DefaultListModel<String> fileNameModel = new DefaultListModel<String>();
			JList<String> fileNameList = new JList<String>(fileNameModel);
			if (output != null && output.getFile() != null) {
				fileList = output.getFile();
				for (int i = 0; i < fileList.size(); i++) {
					fileNameModel.addElement(fileList.get(i).getName());
				}
			} else {
				fileList = new ArrayList<>();
			}
			filePanel.add(fileLabel);
			filePanel.add(fileName);
			filePanel.add(browseFile);
			filePanel.add(fileNameList);
			filePanel.add(removeFile);
			//파일 등록
			browseFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileDialog fileDialog = new FileDialog((Frame) null, "산출물 선택", FileDialog.LOAD);
					fileDialog.setVisible(true);
					File selectedFile = new File(fileDialog.getFile());
					if (selectedFile != null) {
						fileList.add(selectedFile);
						fileList.sort(new Comparator<File>() {
							@Override
							public int compare(File o1, File o2) {
								return o1.getName().compareTo(o2.getName());
							}
						});
						//파일 순서 정렬
						fileNameModel.removeAllElements();
						for (int i = 0; i < fileList.size(); i++) {
							fileNameModel.addElement(fileList.get(i).getName());
						}
					}

				}
			});
			//파일 제거
			removeFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (fileNameList.getSelectedValue() != null) {
						int index = fileNameList.getSelectedIndex();
						String removeFileName = fileNameModel.get(index);
						fileNameModel.remove(index);
						for (int i = 0; i < fileList.size(); i++) {
							if (fileList.get(i).getName().equals(removeFileName)) {
								fileList.remove(i);
								break;
							}
						}
					}
				}
			});
			dialog.add(filePanel);
			// 팀목록
			JPanel teamListPanel = new JPanel();
			JLabel teamListLabel = new JLabel("팀 목록");
			JComboBox<Team> teamListCmbx = new JComboBox<Team>();
			for (int i = 0; i < project.getTeams2().size(); i++) {
				if (project.getTeams2().get(i).getOutput() == null) {
					teamListCmbx.addItem(project.getTeams2().get(i));
				}
			}
			JCheckBox teamDegree0 = new JCheckBox();
			teamDegree0.setVisible(false);
			teamListCmbx.setEnabled(false);
			teamListCmbx.addPropertyChangeListener(evt -> {
				teamDegree0.setSelected(false);
			});
			//			teamDegree0.addChangeListener(evt -> {
			//				teamListCmbx.setEnabled(!teamDegree0.isSelected());
			//			});
			if (team != null && team.getDegree() == 0) {
				teamListCmbx.setEnabled(true);
				teamDegree0.setVisible(true);
				teamDegree0.setSelected(true);
			} else if (team == null) {
				teamDegree0.setVisible(true);
				//				teamDegree0.setSelected(true);
				teamListCmbx.setEnabled(true);
				teamListPanel.add(new JLabel("팀 없이 생성"));
				teamListPanel.add(teamDegree0);
			} else if (team != null) {
				teamListCmbx.setSelectedItem(team);
			}
			teamListPanel.add(teamListLabel);
			teamListPanel.add(teamListCmbx);
			dialog.add(teamListPanel);

			// 태그 설정
			JPanel tagSettingPanel = new JPanel();
			JLabel tagSettingLabel = new JLabel("태그 설정");
			// JList 2개 넣고 버튼 2개 넣고
			tagSettingPanel.setLayout(new GridLayout(2, 1));
			JPanel tagListPanel = new JPanel();
			tagListPanel.setLayout(new GridLayout(1, 3));
			DefaultListModel<Tag> modelLeft = new DefaultListModel<>();
			DefaultListModel<Tag> modelRight = new DefaultListModel<>();
			// DefaultListModel<Tag> model = new DefaultListModel<>();
			ArrayList<Tag> notContainTagList = new ArrayList<Tag>();
			notContainTagList.addAll(TagRepository.getOutputTags());
			if (input == CRUD.EDIT_OUTPUT) {// 수정이라면 대상이 가진 태그 목록을 가져와서 제외하고 leftJList에 채울 것
				for (int i = 0; i < output.getTagList().size(); i++) {
					if (notContainTagList.contains(output.getTagList().get(i))) {
						notContainTagList.remove(output.getTagList().get(i));
					}
				}
			}
			modelLeft.addAll(notContainTagList);
			JList<Tag> leftJlist = new JList<Tag>(modelLeft);
			JList<Tag> rightJlist = new JList<Tag>(modelRight);
			leftJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			rightJlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JPanel middlePanel = new JPanel();
			middlePanel.setLayout(new GridLayout(2, 1));
			JButton moveTag = new JButton("<html>추가<br>→</html>");
			moveTag.setName("add");
			boolean moveToRight;
			//			JButton removeTag = new JButton("제거\n←");
			leftJlist.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					moveTag.setText("<html>추가<br>→</html>");
					moveTag.setName("add");
					rightJlist.clearSelection();
				}
			});
			rightJlist.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					moveTag.setText("<html>제거<br>←</html>");
					moveTag.setName("remove");
					leftJlist.clearSelection();
				}
			});
			// 추가 버튼 기능 구현
			moveTag.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Tag selectedTag;
					if (moveTag.getName().equals("add")) {
						selectedTag = leftJlist.getSelectedValue();
						modelRight.addElement(selectedTag);
						modelLeft.removeElement(selectedTag);
						// 정렬
						ArrayList<Tag> rightList = new ArrayList<Tag>();
						for (int i = 0; i < modelRight.size(); i++) {
							rightList.add(modelRight.get(i));
						} //for
						rightList.sort(new Comparator<Tag>() {
							@Override
							public int compare(Tag o1, Tag o2) {
								return o2.getName().compareTo(o1.getName());
							}
						});//sort
						modelRight.removeAllElements();
						modelRight.addAll(rightList);
					} else {
						selectedTag = rightJlist.getSelectedValue();
						modelLeft.addElement(selectedTag);
						modelRight.removeElement(selectedTag);
						// 정렬
						ArrayList<Tag> leftList = new ArrayList<Tag>();
						for (int i = 0; i < modelLeft.size(); i++) {
							leftList.add(modelLeft.get(i));
						} //for
						leftList.sort(new Comparator<Tag>() {
							@Override
							public int compare(Tag o1, Tag o2) {
								return o2.getName().compareTo(o1.getName());
							}
						});//sort
						modelLeft.removeAllElements();
						modelLeft.addAll(leftList);
					}
					leftJlist.revalidate();
					leftJlist.repaint();
					rightJlist.revalidate();
					rightJlist.repaint();
				}// actionPerformed
			});// addActionListener
				// 제거 버튼 기능 구현
				//			removeTag.addActionListener(new ActionListener() {
				//				@Override
				//				public void actionPerformed(ActionEvent e) {
				//					Tag selectedTag = rightJlist.getSelectedValue();
				//					modelLeft.addElement(selectedTag);
				//					modelRight.removeElement(selectedTag);
				//					// 정렬
				//					ArrayList<Tag> leftList = new ArrayList<Tag>();
				//					for (int i = 0; i < modelLeft.size(); i++) {
				//						leftList.add(modelLeft.get(i));
				//					}
				//					leftList.sort(new Comparator<Tag>() {
				//						@Override
				//						public int compare(Tag o1, Tag o2) {
				//							return o2.getName().compareTo(o1.getName());
				//						}
				//					});
				//					modelLeft.removeAllElements();
				//					modelLeft.addAll(leftList);
				//					leftJlist.revalidate();
				//					leftJlist.repaint();
				//					rightJlist.revalidate();
				//					rightJlist.repaint();
				//				}// actionPerformed
				//			});// addActionListener
			JScrollPane scrollLeft = new JScrollPane(leftJlist);
			JScrollPane scrollRight = new JScrollPane(rightJlist);
			scrollLeft.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			middlePanel.add(moveTag);
			//			middlePanel.add(removeTag);
			tagSettingPanel.add(tagSettingLabel);
			tagListPanel.add(scrollLeft);
			tagListPanel.add(middlePanel);
			tagListPanel.add(scrollRight);
			tagSettingPanel.add(tagListPanel);
			dialog.add(tagSettingPanel);

			// 점수(만점)
			JPanel scorePanel = new JPanel();
			JLabel scoreLabel = new JLabel("점수");
			JTextField score = new JTextField(3);
			scorePanel.add(scoreLabel);
			scorePanel.add(score);
			scoreLabel = new JLabel("/");
			JTextField maxScore = new JTextField(3);
			JLabel scoreText = new JLabel("점");
			((AbstractDocument) score.getDocument()).setDocumentFilter(new DocumentFilter() {
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
			((AbstractDocument) maxScore.getDocument()).setDocumentFilter(new DocumentFilter() {
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
			scorePanel.add(scoreLabel);
			scorePanel.add(maxScore);
			scorePanel.add(scoreText);
			// 버튼(확인,취소)
			JPanel btnPanel = new JPanel();
			JButton okBtn = new JButton("생성");
			if (input == 'e') {
				okBtn.setText("저장");
			}
			JButton cancelBtn = new JButton("취소");
			cancelBtn.addActionListener(evCancel -> {
				dialog.dispose();
			});
			btnPanel.add(okBtn);
			btnPanel.add(cancelBtn);
			okBtn.addActionListener(evok -> {// 만든거 저장하기
				//팀 이름(nullable), 주제(O),파일(O), 팀, 태그 교환, 점수+만점(o)
				if (output == null) {
					Output newOutput = new Output(fileList);
					output = newOutput;
				}
				//팀
				int degree;
				if (teamDegree0.isSelected()) {
					degree = 0;
				} else {
					degree = team.getDegree();
				}
				//팀 이름
				String teamName = teamNameContent.getText();
				if (team == null) {
					Team newTeam = new Team(teamName, degree);
					team = newTeam;
				}
				//주제
				String subject = subjectContent.getText();
				//주제가 없다면 파일 이름으로 대체하면서 생성
				if (subject.equals("")) {
					if (fileList.size() != 0) {
						subject = fileList.get(0).getName();
					}
					subject = "";
				}
				//파일
				if (output != null) {
					output = new Output(fileList);
				}
				//태그 교환
				if (output != null) {
					output.setTagList(notContainTagList);
				}
				//점수
				//최대점수는 음수가 될 수 없음.
				double newMaxScore = 0;
				if (!maxScore.getText().equals("")) {
					newMaxScore = Double.parseDouble(maxScore.getText());
				}
				output.setMaxScore(newMaxScore);
				//점수는 최대점수 이상이 될 수 없음.
				double newScore = 0;
				if (!maxScore.getText().equals("")) {
					newScore = Double.parseDouble(score.getText());
				}
				if (newScore > newMaxScore) {
					JOptionPane.showMessageDialog(null, "점수는 최대 점수보다 클 수 없습니다.", "잘못 된 입력", JOptionPane.PLAIN_MESSAGE);
					return;
				} else {
					output.setScore(newScore);
				}
				team.setOutput(output);
				dialog.dispose();
			});//addActionListener(저장)
			dialog.add(scorePanel);
			dialog.add(btnPanel);
			// }//if
			// 수정
			dialog.setLocationRelativeTo(null);
			dialog.revalidate();
			dialog.repaint();
			dialog.setVisible(true);
			BasicFrame.getInstance(new showTeamList(project));
		} //생성, 수정창
	}//actionPerformed
}