package frame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import VO.Output;
import VO.Project;
import VO.Team;

public class OutputDetail extends JPanel {
	Project project;
	Team team;
	Output output;

	public OutputDetail(Project project, Team team) {
		this.project = project;
		this.team = team;
		this.output = team.getOutput();
		setBackground(new Color(0x78FFEE));
		setLayout(new GridLayout(0, 1));
		// 보여줘야 되는거
		// 주제
		JLabel title = new JLabel(output.getTitle());
		// System.out.println("output>>" + output);
		add(title);
		// 팀명 : 조원 조원 조원
		JLabel teamName = new JLabel(team.getTName() + ":");
		JPanel studentList = new JPanel();
		studentList.setLayout(new GridLayout(1, 0));
		studentList.add(teamName);
		for (int i = 0; i < team.getMembers2().size(); i++) {
			Collections.sort(team.getMembers2());
			String name = team.getMembers2().get(i).getsName();
			JLabel tempName = new JLabel(name);
			studentList.add(tempName);
		}
		add(studentList);
		// 파일 목록
		if (output.getFile() != null) {
			for (int i = 0; i < output.getFile().size(); i++) {
				JLabel tempFile = new JLabel(output.getFile().get(i).getName());
				tempFile.addMouseListener(null);// [구현 예정]클릭 시 파일 다운로드
			}
		}
		// 메모
		JPanel reviewPanel = new JPanel();
		reviewPanel.setLayout(new FlowLayout());
		JLabel tempReview = new JLabel(output.getReview());
		reviewPanel.add(tempReview);
		add(reviewPanel);
		// 태그 목록
		JPanel tagList = new JPanel();
		tagList.setLayout(new GridLayout(1, 0));
		for (int i = 0; i < output.getTagList().size(); i++) {
			JButton tempTag = new JButton(output.getTagList().get(i).getName());
			tagList.add(tempTag);
		}
		add(tagList);
		// 평점
		JPanel scorePanel = new JPanel();
		String score = String.format("%.2f", output.getScore());
		String maxScore = String.format("/ %.2f", output.getMaxScore());
		JLabel tempScore = new JLabel(score);
		JLabel tempMaxScore = new JLabel(maxScore);
		reviewPanel.add(tempScore);
		reviewPanel.add(tempMaxScore);
		add(scorePanel);
		// TODO : 수정 버튼 구현
		JPanel btnList = new JPanel();
		btnList.setLayout(new GridLayout(1, 0));
		JButton editBtn = new JButton("수정");
		editBtn.addActionListener(new CRUDOutput(CRUDOutput.EDIT_OUTPUT, this.project, this.team, output));
		btnList.add(editBtn);
		// TODO: 삭제 버튼 구현
		JButton deleteBtn = new JButton("삭제");
		deleteBtn.addActionListener(new CRUDOutput(CRUDOutput.DELETE_OUTPUT, this.project, this.team, output));
		btnList.add(deleteBtn);
		add(btnList);
	}
}