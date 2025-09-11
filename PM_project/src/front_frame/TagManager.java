package front_frame;

import java.awt.Color;

import front_ui.*;
import front_util.*;

public class TagManager extends BasePage {

	private boolean studentSelected = false;

	private AutoGrowBox box;
	private TagListPanel studentList, outputList;
	private TabsBar tabs;

	private static final Color BORDER_STUDENT = Theme.TAB_OFF_BG;
	private static final Color BORDER_OUTPUT = Theme.ACCENT_OUTPUT;

	public TagManager() {
		super(new TopBar.OnMenuClick() {
			@Override
			public void onClass() {
				DefaultFrame.getInstance(new ClassManager());
			}

			@Override
			public void onStudent() {
				DefaultFrame.getInstance(new StudentManager());
			}

			@Override
			public void onTag() {
				DefaultFrame.getInstance(new TagManager());
			}
		});
		getTopBar().selectOnly("tag");

		// 파일 모양의 내용 표기 할 공간
		final int boxX = 19, boxW = 752, boxH = 460;

		final int boxBaseY = 24; // 탭 기준 Y(탭은 이 기준 유지)
		final int boxDrop = 12; // 박스/내용물만 아래로 내림
		final int boxY = boxBaseY + boxDrop;

		final int contentX = 16, contentY = 34;
		final int contentW = boxW - contentX * 2;
		final int contentH = boxH - contentY - 16;

		// ── 박스(자동 확장형) ───────────────────────────── , 내용 추가 시 스크롤바 갱신
		box = new AutoGrowBox();
		box.setBounds(boxX, boxY, boxW, boxH);
		box.setBorderColor(BORDER_OUTPUT);
		getContentPanel().add(box);

		// ── 탭 바(학생/산출물) ────────────────────────────
		final int tabW = 100, tabH = 28;
		final int tabBottom = boxBaseY + Theme.BORDER_THICK; // 내부 상단선
		final int tabY = tabBottom - tabH;

		tabs = new TabsBar(new TabSpec[] { new TabSpec("학생", Theme.TAB_OFF_BG), // 산출물 탭 색 변경은 Theme class에서
				new TabSpec("산출물", Theme.ACCENT_OUTPUT) // 마찬가지
		}, tabW, tabH);
		final int tabStudentX = boxX;
		final int tabOutputX = boxX + 110; // 탭 사이 간격
		tabs.setTabLocation(0, tabStudentX, tabY);
		tabs.setTabLocation(1, tabOutputX, tabY);

		// 부모(TabsBar) 크기를 자식 탭이 들어가도록 지정 (잘림 방지)
		int tabsRight = Math.max(tabStudentX, tabOutputX) + tabW;
		int tabsBottom = tabY + tabH;
		tabs.setBounds(0, 0, tabsRight, tabsBottom);

		getContentPanel().add(tabs);
		// 탭이 박스 위에 보이도록 Z-순서 조정
		getContentPanel().setComponentZOrder(tabs, 0);
		getContentPanel().setComponentZOrder(box, 1);

		// ── 내용물(흰 박스 내부) ──────────────────────────
		studentList = new TagListPanel();
		studentList.setBounds(contentX, contentY, contentW, contentH);
		box.add(studentList);

		outputList = new TagListPanel();
		outputList.setBounds(contentX, contentY, contentW, contentH);
		box.add(outputList);

		// + 버튼 로직 주입
		studentList.setOnAdd(() -> addItemToPanel(true, "새태그", Theme.BLUE, "(remark)"));
		outputList.setOnAdd(() -> addItemToPanel(false, "새태그", Theme.BLUE, "(remark)"));

		// 탭 선택 콜백(테두리색/가시성만 변경) — 내용물 생성 후에 등록
		tabs.setOnChange(selected -> {
			studentSelected = (selected == 0);
			box.setBorderColor(studentSelected ? BORDER_STUDENT : BORDER_OUTPUT);
			applyVisibility();
			getContentPanel().repaint();
		});
		tabs.setSelectedIndex(1, false);

		box.autoGrow();
		refreshScroll();
	}

	// 있으면 버그 덜 난다고 해서 추가한 널가드
	private void applyVisibility() {
		if (studentList == null || outputList == null)
			return; // 안전 가드
		studentList.setVisible(studentSelected);
		outputList.setVisible(!studentSelected);
	}

	// 한 줄 추가(+버튼/더미에서 사용)
	private void addItemToPanel(boolean toStudent, String chipText, Color chipBg, String remarkText) {
		if (toStudent)
			studentList.addRow(chipText, chipBg, remarkText);
		else
			outputList.addRow(chipText, chipBg, remarkText);
		box.autoGrow();
		refreshScroll();
	}
}
