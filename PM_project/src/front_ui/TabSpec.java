// front_ui/TabSpec.java
package front_ui;

import java.awt.Color;
import java.util.EventListener;

//태그 관리 화면용 클래스
public class TabSpec {
	public final String title;
	public final Color color;
	private String degree;

	public TabSpec(String title, Color color) {
		this.title = title;
		this.color = color;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
}