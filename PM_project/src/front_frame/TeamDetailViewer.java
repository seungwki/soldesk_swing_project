package front_frame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import VO.Team;
import VO.Student;
import VO.Output;
import VO.Tag;

public class TeamDetailViewer extends JPanel {
	private Team team;

	public TeamDetailViewer(Team team) {
		this.team = team;
		setLayout(null);
		setBackground(Color.WHITE);

		int y = 20;

		JLabel subjectLabel = new JLabel("주제 : " + team.getOutput().getTitle());
		subjectLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		subjectLabel.setBounds(20, y, 600, 30);
		add(subjectLabel);

		y += 40;

		JLabel teamLabel = new JLabel("팀 명 : " + team.getTName());
		teamLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		teamLabel.setBounds(20, y, 400, 30);
		add(teamLabel);

		y += 40;

		JLabel memberLabel = new JLabel("조원 : ");
		memberLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		memberLabel.setBounds(20, y, 100, 30);
		add(memberLabel);

		int memberX = 80;
		for (Student m : team.getMembers2()) {
			JLabel mLabel = new JLabel(m.getsName());
			mLabel.setOpaque(true);
			mLabel.setBackground(new Color(180, 200, 255));
			mLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
			mLabel.setBounds(memberX, y, 80, 30);
			mLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(mLabel);
			memberX += 90;
		}

		y += 50;

		JLabel fileLabel = new JLabel("파일 목록");
		fileLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		fileLabel.setBounds(20, y, 200, 30);
		add(fileLabel);

		y += 30;

		JTextArea fileArea = new JTextArea();
		fileArea.setEditable(false);
		fileArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		JScrollPane fileScroll = new JScrollPane(fileArea);
		fileScroll.setBounds(20, y, 400, 100);
		add(fileScroll);

		for (File file : team.getOutput().getFile()) {
			fileArea.append(file.getName() + "\n");
		}

		y += 120;

		JLabel tagLabel = new JLabel("<태그>");
		tagLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		tagLabel.setBounds(20, y, 100, 30);
		add(tagLabel);

		int tagX = 100;
		for (Tag tag : team.getOutput().getTagList()) {
			JLabel tagChip = new JLabel(tag.getName());
			tagChip.setOpaque(true);
			tagChip.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
			tagChip.setHorizontalAlignment(SwingConstants.CENTER);
			tagChip.setBounds(tagX, y, 70, 30);

			// 색상 구분
			if (tag.getColor().equals("blue")) {
				tagChip.setBackground(new Color(180, 200, 255));
			} else if (tag.getColor().equals("green")) {
				tagChip.setBackground(new Color(170, 255, 170));
			} else if (tag.getColor().equals("orange")) {
				tagChip.setBackground(new Color(255, 200, 100));
			} else if (tag.getColor().equals("red")) {
				tagChip.setBackground(new Color(255, 120, 120));
			} else {
				tagChip.setBackground(Color.LIGHT_GRAY);
			}

			add(tagChip);
			tagX += 80;
		}

		JButton addTagBtn = new JButton("+");
		addTagBtn.setBounds(tagX, y, 45, 30);
		add(addTagBtn);
	}
}
