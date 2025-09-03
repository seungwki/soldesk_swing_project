package VO;

import java.util.ArrayList;

public class Session {
	private String ssName;
	private ArrayList<Team> teamList;
	private int index;
	private int isDelete;

	public Session(String ssName, int index) {
		this.ssName = ssName;
		this.index = index;
	}

	public String getSsName() {
		return ssName;
	}

	public void setSsName(String ssName) {
		this.ssName = ssName;
	}

	public ArrayList<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(ArrayList<Team> teamList) {
		this.teamList = teamList;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
}