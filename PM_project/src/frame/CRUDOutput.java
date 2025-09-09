package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
			int confirm = JOptionPane.showConfirmDialog(null, team.getTName() + " 삭제 하시겠습니까?", "팀 삭제 확인", JOptionPane.OK_CANCEL_OPTION);
			if (confirm == 0) {
				for (int i = 0; i < project.getTeams2().size(); i++) {
					if (project.getTeams2().get(i).getTName().equals(team.getTName()) && project.getTeams2().get(i).getDegree() == team.getDegree()) {
						project.getTeams2().remove(team);
						JOptionPane.showMessageDialog(null, "삭제되었습니다.", null, JOptionPane.PLAIN_MESSAGE);
						//화면 다시 그리고 break;
						BasicFrame.getInstance(new showTeamList(project));
						break;
					}
				}
			}
		} //if(delete)
		else {
			// 주제,파일,팀목록(조건부),태그교환,점수(만점),버튼
			JDialog dialog = new JDialog((Frame) null, "새 팀 생성", true);
			int width, height;
			width = 600;
			height = 450;
			dialog.setSize(width, height);
			//			dialog.setLayout(new FlowLayout());
			dialog.setLayout(new GridLayout(0, 1));
			// 생성
			//			if (input == 'c') {
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

			//팀목록(조건부)
			if ((input == CRUDOutput.CREATE_OUTPUT || team == null)) {
				JPanel teamListPanel = new JPanel();
				JLabel teamListLabel = new JLabel("팀 목록");
				teamListPanel.add(teamListLabel);
				//JComboBox 사용 할 것
				JComboBox<Team> teamListCmbx = new JComboBox<Team>();
				for (int i = 0; i < project.getTeams2().size(); i++) {
					if (project.getTeams2().get(i).getOutput() == null) {
						teamListCmbx.addItem(project.getTeams2().get(i));
					}
				}
				teamListPanel.add(teamListCmbx);
				dialog.add(teamListPanel);
			}

			//파일
			JPanel filePanel = new JPanel();
			JLabel fileLabel = new JLabel("파일");
			filePanel.add(fileLabel);
			dialog.add(filePanel);

			//태그 설정
			JPanel tagSettingPanel = new JPanel();
			JLabel tagSettingLabel = new JLabel("태그 설정");
			//JList 2개 넣고 버튼 2개 넣고
			tagSettingPanel.setLayout(new GridLayout(2, 1));
			tagSettingPanel.add(tagSettingLabel);
			JPanel tagListPanel = new JPanel();
			tagListPanel.setLayout(new GridLayout(1, 3));
			DefaultListModel<Tag> model = new DefaultListModel<>();
			model.addAll(TagRepository.getOutputTags());
			JList<Tag> leftJlist = new JList<Tag>(model);
			tagListPanel.add(leftJlist);
			JPanel middlePanel = new JPanel();
			middlePanel.setLayout(new GridLayout(2, 1));
			middlePanel.add(new JButton("추가\n>>>"));
			middlePanel.add(new JButton("제거\n<<<"));
			JList<Tag> rightJlist = new JList<Tag>();
			tagListPanel.add(middlePanel);
			tagListPanel.add(rightJlist);
			dialog.add(tagListPanel);
			dialog.add(tagSettingPanel);

			//점수(만점)
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

			//버튼(확인,취소)
			JPanel btnPanel = new JPanel();
			JButton okBtn = new JButton("확인");
			JButton cancelBtn = new JButton("취소");
			btnPanel.add(okBtn);
			btnPanel.add(cancelBtn);
			okBtn.addActionListener(evok -> {
				//만든거 저장하기
			});
			cancelBtn.addActionListener(evCancel -> {
				dialog.dispose();
			});

			dialog.add(scorePanel);
			dialog.add(btnPanel);
			//			}//if
			//수정
			if (input == 'e') {
			}
			dialog.setLocationRelativeTo(null);
			dialog.revalidate();
			dialog.repaint();
			dialog.setVisible(true);
		}
	}
}
