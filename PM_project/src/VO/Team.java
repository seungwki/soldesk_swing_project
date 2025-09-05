package VO;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private String tName;
	private int degree;
	private List<Student> members = new ArrayList<>();

	public Team(String tName, int degree) {
		this.tName = tName;
		this.degree = degree;
	}

	public String getTName() {
		return tName;
	}

	public int getDegree() {
		return degree;
	}

	public List<Student> getMembers() {
		return List.copyOf(members);
	}

	public void addMember(Student s) {
		members.add(s);
	}

	public boolean hasStudent(Student s) {
		return members.contains(s);
	}

}
