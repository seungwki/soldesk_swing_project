package VO;

import java.util.ArrayList;
import java.util.List;

public class Student implements Comparable<Student> {
	private String sName;
	private String sNum;
	private String memo;
	private List<Tag> tags = new ArrayList<>();

	public Student(String sName, String sNum) {
		this.sName = sName;
		this.sNum = sNum;
	}

	public String getsName() {
		return sName;
	}

	public String getsNum() {
		return sNum;
	}

	public String getmemo() {
		return memo;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public void setsNum(String sNum) {
		this.sNum = sNum;
	}

	public void setmemo(String memo) {
		this.memo = memo;
	}

	@Override
	public int compareTo(Student o) {
		return this.getsName().compareTo(o.getsName());
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		if (!tags.contains(tag))
			tags.add(tag);
	}
}
