package VO;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String name;
	private List<Team> teams = new ArrayList<>();
	private boolean isBookmark;

	public boolean isBookmark() {
		return isBookmark;
	}

	public void setBookmark(boolean isBookmark) {
		this.isBookmark = isBookmark;
	}

	public Project(String name) {
		this.name = name;
		this.isBookmark = false;
		this.teams = new ArrayList<Team>();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<Team> getTeams() {
		return List.copyOf(teams);
	}//리스트 그 자체가 아니라 배열로 반환해야 할 이유?? 이게 더 안전한지?

	public void addTeam(Team team) {
		teams.add(team);
	}

	public Team findTeamByStudent(Student s) {
		for (Team t : teams) {
			if (t.hasStudent(s)) {
				return t;
			}
		}
		return null; // 학생 찾기
	}

	// 학생이 있는지
	public boolean hasStudent(Student s) {
		return findTeamByStudent(s) != null;
	}

}
