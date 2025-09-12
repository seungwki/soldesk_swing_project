package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import VO.Output;
import VO.Project;
import VO.Tag;
import VO.TagRepository;
import VO.Team;

public class CRUDOutput implements ActionListener {
	public final static int CREATE_OUTPUT = (int) 'c';
	public final static int EDIT_OUTPUT = (int) 'e';
	public final static int DELETE_OUTPUT = (int) 'd';
	private int input;
	private Project project;
	private Team team;
	private Output output;

	public CRUDOutput(int input, Project project, Team team, Output output) {
		this.input = input;
		this.project = project;
		this.team = team;
		this.output = output;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 삭제
		if (input == 'd') {
			// TODO : 확인창 팝업 띄우고, yes라면 팀 삭제 진행
			int confirm = JOptionPane.showConfirmDialog(null, team.getTName() + " 삭제 하시겠습니까?", "팀 삭제 확인",
					JOptionPane.OK_CANCEL_OPTION);
			if (confirm == 0) {
				for (int i = 0; i < project.getTeams2().size(); i++) {
					if (project.getTeams2().get(i).getTName().equals(team.getTName())
							&& project.getTeams2().get(i).getDegree() == team.getDegree()) {
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
			// 주제,파일,팀목록(조건부),태그교환,점수(만점),버튼
			JDialog dialog = new JDialog((Frame) null, "새 팀 생성", true);
			int width, height;
			width = 600;
			height = 450;
			dialog.setSize(width, height);
			dialog.setLayout(new GridLayout(0, 1));
			// 주제
			JPanel titlePanel = new JPanel();
			JLabel titleLabel = new JLabel("주제");
			JTextField titleContent = new JTextField(30);
			if (input == 'e' && output != null && output.getTitle() != null) {
				titleContent.setText(output.getTitle());
			}
			titlePanel.add(titleLabel);
			titlePanel.add(titleContent);
			dialog.add(titlePanel);

			// 팀목록(조건부)
			JPanel teamListPanel = new JPanel();
			JLabel teamListLabel = new JLabel("팀 목록");
			teamListPanel.add(teamListLabel);
			// JComboBox 사용 할 것
			JComboBox<Team> teamListCmbx = new JComboBox<Team>();
			for (int i = 0; i < project.getTeams2().size(); i++) {
				if (project.getTeams2().get(i).getOutput() == null) {
					teamListCmbx.addItem(project.getTeams2().get(i));
				}
			}
			if (team == null) {
				JCheckBox teamDegree0 = new JCheckBox();
				teamDegree0.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (teamDegree0.isSelected()) {
							teamListCmbx.setEnabled(!teamDegree0.isSelected());
						} else {
							teamListCmbx.setEnabled(!teamDegree0.isSelected());
						}
					}
				});
				teamListPanel.add(new JLabel("팀 없이 생성"));
				teamListPanel.add(teamDegree0);
			}
			teamListPanel.add(teamListCmbx);
			if (team != null && input == EDIT_OUTPUT) {
				teamListCmbx.setEnabled(false);
			}
			if (team != null) {
				teamListCmbx.setSelectedItem(team);
			}
			dialog.add(teamListPanel);

			// 파일
			JPanel filePanel = new JPanel();
			filePanel.setLayout(new GridLayout(0, 1));
			JLabel fileLabel = new JLabel("파일");
			JLabel fileName = new JLabel("선택하기");
			JButton removeFile = new JButton("제거");
			JButton browseFile = new JButton("찾아보기");
			DefaultListModel<String> fileNameModel = new DefaultListModel<String>();
			ArrayList<File> fileList = new ArrayList<>();
			JList<String> fileNameList = new JList<String>(fileNameModel);
			filePanel.add(fileLabel);
			filePanel.add(fileName);
			filePanel.add(browseFile);
			filePanel.add(fileNameList);
			filePanel.add(removeFile);
			browseFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FileDialog fileDialog = new FileDialog((Frame) null, "산출물 선택", FileDialog.LOAD);
					fileDialog.setVisible(true);
					File selectedFile;
					selectedFile = new File(fileDialog.getFile());
					fileList.add(selectedFile);
					fileNameModel.addElement(selectedFile.getName());
				}
			});
			removeFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (fileNameList.getSelectedValue() != null) {
						int index = fileNameList.getSelectedIndex();
						fileNameModel.remove(index);
						for (int i = 0; i < fileList.size(); i++) {
							if (fileList.get(i).getName().equals(fileNameList.getSelectedValue())) {
								fileList.remove(i);
								break;
							}
						}
					}
				}
			});
			dialog.add(filePanel);

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
			if (input == CRUDOutput.EDIT_OUTPUT) {// 수정이라면 대상이 가진 태그 목록을 가져와서 제외하고 leftJList에 채울 것
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
			JButton addTag = new JButton("추가\n→");
			JButton removeTag = new JButton("제거\n←");
			// 추가 버튼 기능 구현
			addTag.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Tag selectedTag = leftJlist.getSelectedValue();
					modelRight.addElement(selectedTag);
					modelLeft.removeElement(selectedTag);
					// 정렬
					ArrayList<Tag> rightList = new ArrayList<Tag>();
					for (int i = 0; i < modelRight.size(); i++) {
						rightList.add(modelRight.get(i));
					}
					rightList.sort(new Comparator<Tag>() {
						@Override
						public int compare(Tag o1, Tag o2) {
							return o2.getName().compareTo(o1.getName());
						}
					});
					modelRight.removeAllElements();
					modelRight.addAll(rightList);
					leftJlist.revalidate();
					leftJlist.repaint();
					rightJlist.revalidate();
					rightJlist.repaint();
				}// actionPerformed
			});// addActionListener
				// 제거 버튼 기능 구현
			removeTag.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Tag selectedTag = rightJlist.getSelectedValue();
					modelLeft.addElement(selectedTag);
					modelRight.removeElement(selectedTag);
					// 정렬
					ArrayList<Tag> leftList = new ArrayList<Tag>();
					for (int i = 0; i < modelLeft.size(); i++) {
						leftList.add(modelLeft.get(i));
					}
					leftList.sort(new Comparator<Tag>() {
						@Override
						public int compare(Tag o1, Tag o2) {
							return o2.getName().compareTo(o1.getName());
						}
					});
					modelLeft.removeAllElements();
					modelLeft.addAll(leftList);
					leftJlist.revalidate();
					leftJlist.repaint();
					rightJlist.revalidate();
					rightJlist.repaint();
				}// actionPerformed
			});// addActionListener
			JScrollPane scrollLeft = new JScrollPane(leftJlist);
			JScrollPane scrollRight = new JScrollPane(rightJlist);
			scrollLeft.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollRight.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			middlePanel.add(addTag);
			middlePanel.add(removeTag);
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
			btnPanel.add(okBtn);
			btnPanel.add(cancelBtn);
			okBtn.addActionListener(evok -> {
				// 만든거 저장하기

			});
			cancelBtn.addActionListener(evCancel -> {
				dialog.dispose();
			});

			dialog.add(scorePanel);
			dialog.add(btnPanel);
			// }//if
			// 수정
			dialog.setLocationRelativeTo(null);
			dialog.revalidate();
			dialog.repaint();
			dialog.setVisible(true);
		}
	}
}
