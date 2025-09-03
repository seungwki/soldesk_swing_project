package VO;

import java.io.FileReader;
import java.util.ArrayList;

public class Lecture {
	private String lName;
	private String lPlace;
	private int index;
	private int isDeleted;
	private int bookmark;
	private ArrayList<Session> sessionList;
	private static int lastIndex = -1;

	public Lecture(String lName, String lPlace, String index) {
		this.lName = lName;
		this.lPlace = lPlace;
		this.index = Integer.parseInt(index);
		if (lastIndex == -1) {
			lastIndex = findLastIndex();
		}
		lastIndex += 1;
		isDeleted = 0;
		bookmark = 0;
		sessionList = new ArrayList<>();
	}

	public static int getLastIndex() {
		return lastIndex;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getlPlace() {
		return lPlace;
	}

	public void setlPlace(String lPlace) {
		this.lPlace = lPlace;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		if (isDeleted != 0)
			this.isDeleted = 1;
		else {
			this.isDeleted = 0;
		}
	}

	public int getBookmark() {
		return bookmark;
	}

	public void setBookmark(int bookmark) {
		this.bookmark = bookmark;
	}

	public ArrayList<Session> getSessionList() {
		return sessionList;
	}

//	public void setSessionList(ArrayList<Session> sessionList) {
//		this.sessionList = sessionList;
//	}

	private static int findLastIndex() {
		String lectureListFile = "resource\\data\\LectureList.txt";
		try (FileReader fileReader = new FileReader(lectureListFile)) {
			StringBuilder sb = new StringBuilder();
			int readData;
			while ((readData = fileReader.read()) != -1) {
				sb.append(readData);
			}
			String lectureListString = sb.toString();
			String[] lectureListArray = lectureListString.split(String.valueOf((char)3));
			return lectureListArray.length;
		} catch (Exception e) {
		} // try catch
		return -1;
	}// findLastIndex()
}