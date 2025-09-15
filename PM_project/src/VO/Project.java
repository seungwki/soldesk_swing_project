//김승우
package VO;

import java.util.ArrayList;
import java.util.List;

public class Project {
	private String name;
	private String place;
	private List<Team> teams = new ArrayList<>();
	private final List<Student> unassigned = new ArrayList<>();
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

	// 추가한거.............................................................................................................
	@Override
	public String toString() {
		return this.name;
	}

	//--------------------------수정을 위해 추가한 내용---------------------------------------------------
	public List<Student> getUnassigned() {
		return unassigned;
	}

	/** 수정 전용: 팀 리스트(가변) */
	public List<Team> teamsMutable() {
		return teams;
	}

	/** 수정 전용: 팀 추가/삭제 */
	public void addTeam1(Team t) {
		teams.add(t);
	}

	public boolean removeTeam(Team t) {
		return teams.remove(t);
	}

	/** 편의: 차수+팀명으로 팀 찾기 */
	public Team findTeam(int degree, String name) {
		for (Team t : teams) {
			if (t.getDegree() == degree && t.getTName().equals(name))
				return t;
		}
		return null;
	}

	/** 팀의 모든 멤버를 '미배정'으로 이동시키고 팀 삭제 */
	public boolean deleteTeamAndMoveMembersToUnassigned(int degree, String name) {
		Team target = findTeam(degree, name);
		if (target == null)
			return false;

		for (Student s : new ArrayList<>(target.getMembers())) {
			addUnassigned(s);
		}
		return teams.remove(target);
	}

	public void addUnassigned(Student s) {
		if (s == null)
			return;
		// 중복 방지(이름+전화 기준, 필요에 맞게 equals/hashCode로 바꿔도 됨)
		for (Student x : unassigned) {
			if (x.getsName().equals(s.getsName()) && x.getsNum().equals(s.getsNum()))
				return;
		}
		unassigned.add(s);
	}

	public void removeUnassigned(Student s) {
		if (s == null)
			return;
		unassigned.removeIf(x -> x.getsName().equals(s.getsName()) && x.getsNum().equals(s.getsNum()));
	}
}
