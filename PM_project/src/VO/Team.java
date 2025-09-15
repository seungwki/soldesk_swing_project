package VO;

import java.util.ArrayList;
import java.util.Collection;
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

	//통합용
	/** 수정 전용: 팀원 전체 반환 (수정이 필요한 코드에서만 사용) */
	public List<Student> membersMutable() {
		return members;
	}

	/** 수정 전용: 여러 명 추가 */
	public void addMembers(Collection<Student> list) {
		members.addAll(list);
	}

	/** 수정 전용: 한 명 제거 */
	public boolean removeMember(Student s) {
		return members.remove(s);
	}

	/** 수정 전용: 모두 이동 후 비우기 */
	public void moveAllMembersTo(Team dest) {
		dest.members.addAll(this.members);
		this.members.clear();
	}
}