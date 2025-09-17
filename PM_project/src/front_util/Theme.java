package front_util;

import java.awt.Color;
import java.awt.Font;

public final class Theme {
	private Theme() {
	}

	// Fonts
	public static final Font FONT_14_BOLD = new Font("맑은 고딕", Font.BOLD, 14);
	public static final Font FONT_16 = new Font("맑은 고딕", Font.PLAIN, 16);

	// Metrics
	public static final int RADIUS_12 = 12;
	public static final int BORDER_THICK = 12;

	// ─ Tabs (TagManager 전용 팔레트) ─
	public static final java.awt.Color TAB_STUDENT_BG = new java.awt.Color(0xE6ECF6); // 학생 탭
	public static final java.awt.Color TAB_OUTPUT_BG = new java.awt.Color(0xAFC2F5); // 산출물 탭

	// 테두리 색
	public static final java.awt.Color BORDER_STUDENT = TAB_STUDENT_BG;
	public static final java.awt.Color BORDER_OUTPUT = TAB_OUTPUT_BG;
	public static final Color TAB_OFF_BG = new Color(0xE0E6F5); // 비선택 탭 배경(회색 톤)
	// Base colors
	public static final Color BLUE = new Color(0x4E74DE);
	public static final Color LIGHT_BORDER = TAB_STUDENT_BG; // 연한 파랑 테두리 톤
	public static final Color ACCENT_OUTPUT = TAB_OUTPUT_BG; // 산출물 강조색(노랑)
	public static final Color TAB_OFF_LINE = new Color(0xCED7EC); // 비선택 탭 하단 라인
}
