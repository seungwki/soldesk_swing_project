package VO;

import java.awt.Color;

public class Tag {
	private String name;
	private String remark;
	private Color color;
	private boolean bookmarked;

	public Tag(String name, String remark, Color color) {
		this.name = name;
		this.remark = remark;
		this.color = color;
		this.bookmarked = false; // 기본값은 false
	}

	// --- Getter 메서드 ---
	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public Color getColor() {
		return color;
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public void setBookmarked(boolean bookmarked) {
		this.bookmarked = bookmarked;
	}

	// --- Setter 메서드 ---
	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	// --- 디버깅용 toString() ---
	@Override
	public String toString() {
		return "TagData{name='" + name + "', remark='" + remark + "', color=" + color + "}";
	}
}