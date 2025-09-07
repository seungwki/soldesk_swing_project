package VO;

import java.io.File;
import java.util.ArrayList;

public class Output {
	private ArrayList<File> file; //파일 목록
	private ArrayList<Tag> tagList = new ArrayList<>();//부착 된 태그 목록
	private String review;//메모
	private double score;//평점

	public ArrayList<File> getFile() {
		return file;
	}

	public void setFile(ArrayList<File> file) {
		this.file = file;
	}

	public ArrayList<Tag> getTagList() {
		return tagList;
	}

	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}