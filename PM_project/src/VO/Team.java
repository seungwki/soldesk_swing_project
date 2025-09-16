package VO;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private String tName;
	private int degree;
	private List<Student> members = new ArrayList<>();
	private Output output;

	public Output getOutput() {
		return output;
	}

	public void setOutput(Output output) {
		this.output = output;
	}

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

	public List<Student> getMembers2() {//불변성 이슈
		return members;
	}

	public void addMember(Student s) {
		members.add(s);
	}

	public boolean hasStudent(Student s) {
		return members.contains(s);
	}

	@Override
	public String toString() {
		return tName;
	}

}