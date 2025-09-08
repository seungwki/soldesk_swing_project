package VO;

import java.awt.Color;

public class Tag {
	private String name;//이름
	private String remark;//설명 <<< 표시 안 됨
	private Color color;//색

	public Tag(String name, String remark, Color color) {
		this.name = name;
		this.remark = remark;
		this.color = color;
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