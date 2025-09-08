package frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import VO.Output;
import VO.Project;
import VO.Team;

public class EdOutput implements ActionListener {
	public final static int EDIT_OUTPUT = (int) 'e';
	public final static int DELETE_OUTPUT = (int) 'd';
	private int input;
	private Project project;
	private Team team;
	private Output output;

	public EdOutput(int input, Project project, Team team, Output output) {
		this.input = input;
		this.project = project;
		this.team = team;
		this.output = output;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 수정
		if (input == 'e') {
		}
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
		}
	}
}
