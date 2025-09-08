package VO;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String name;
	private String place;
	private List<Team> teams = new ArrayList<>();
	//	private ArrayList<Team> teams2 = new ArrayList<>();
	private boolean isBookmark;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

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

	public Project(String name, String place) {
		this.name = name;
		this.place = place;
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
		return List.copyOf(teams); //정렬 시도 시 불변성 이슈
	}

	public void addTeam(Team team) {
		teams.add(team);
	}

	public ArrayList<Team> getTeams2() {//정렬 시도 시 불변성 이슈로 걍 따로 만듦
		return (ArrayList<Team>) teams;
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
