package front_frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import VO.Tag;
import VO.TagRepository;
import front_ui.AutoGrowBox;
import front_ui.TabSpec;
import front_ui.TagListPanel;
import front_ui.TagRow;
import front_ui.TopBar;
import front_util.Theme;

public class TagManager extends BasePage {

	private boolean studentSelected = false;

	private AutoGrowBox box;
	private TagListPanel studentList, outputList;
	private TabsBar tabs;

	private boolean isDuplicateTag(boolean toStudent, String tagName) {
		TagListPanel targetPanel = toStudent ? studentList : outputList;
		return targetPanel.containsTag(tagName); //중복 체크 메서드 어디에 추가하는지,동명이 있는지
	}

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
		studentList.setOnAdd(() -> addNewTagDialog());
		outputList.setOnAdd(() -> addNewTagDialog());

		// 탭 선택 콜백(테두리색/가시성만 변경) — 내용물 생성 후에 등록
		tabs.setOnChange(selected -> {
			studentSelected = (selected == 0);
			box.setBorderColor(studentSelected ? BORDER_STUDENT : BORDER_OUTPUT);
			applyVisibility();
			getContentPanel().repaint();
		});
		tabs.setSelectedIndex(0, true);

		box.autoGrow();
		refreshScroll();

		studentList.loadTagsFromRepository(TagRepository.getStudentTags());
		outputList.loadTagsFromRepository(TagRepository.getOutputTags());
		applyVisibility();
		// 초기 태그 로딩 후, 이벤트 핸들러 연결
		studentList.loadStudentTags();
		for (Component comp : studentList.getComponents()) {
			if (comp instanceof TagRow) {
				applyTagRowHandlers(true, (TagRow) comp);
			}
		}

		outputList.loadOutputTags();
		for (Component comp : outputList.getComponents()) {
			if (comp instanceof TagRow) {
				applyTagRowHandlers(false, (TagRow) comp);
			}
		}
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
		TagRow row;
		if (toStudent) {
			row = studentList.addRow(chipText, chipBg, remarkText);
			// TagRepository에 태그 추가
			TagRepository.getStudentTags().add(new Tag(chipText, remarkText, chipBg));
		} else {
			row = outputList.addRow(chipText, chipBg, remarkText);
			// TagRepository에 태그 추가
			TagRepository.getOutputTags().add(new Tag(chipText, remarkText, chipBg));
		}

		// 좌클릭: 태그 수정
		row.setOnLeftClick(() -> editTagDialog(toStudent, row));

		// 우클릭: 태그 삭제
		row.setOnRightClick(() -> {
			int confirm = JOptionPane.showConfirmDialog(this, "태그를 삭제하시겠습니까?", "태그 삭제", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (toStudent) {
					studentList.removeRow(row);
					// TagRepository에서도 삭제
					TagRepository.getStudentTags().removeIf(t -> t.getName().equals(row.getTagName()));

					studentList.revalidate(); // 레이아웃 갱신
					studentList.repaint();
				} else {
					outputList.removeRow(row);
					// TagRepository에서도 삭제
					TagRepository.getOutputTags().removeIf(t -> t.getName().equals(row.getTagName()));

					outputList.revalidate(); // 레이아웃 갱신
					outputList.repaint();
				}
				box.autoGrow();
				refreshScroll();
			}
		});

		box.autoGrow();
		refreshScroll();

	}

	//태그 추가 하는 메서드
	private void addNewTagDialog() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// 태그명 라벨
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("태그명:"), gbc);

		// 태그명 입력란
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		JTextField tagField = new JTextField(20);
		panel.add(tagField, gbc);

		// 카테고리 라벨
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		panel.add(new JLabel("카테고리:"), gbc);

		// 카테고리 콤보박스
		gbc.gridx = 1;
		gbc.gridy = 1;
		String[] categories = { "학생", "산출물" };
		JComboBox<String> categoryCombo = new JComboBox<>(categories);
		panel.add(categoryCombo, gbc);

		// 설명 라벨
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(new JLabel("설명:"), gbc);

		// 설명 입력란 (여러 줄 가능)
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;

		JTextArea remarkArea = new JTextArea(6, 20); // 6줄 높이, 20열 너비
		remarkArea.setLineWrap(true);
		remarkArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(remarkArea);
		panel.add(scrollPane, gbc);

		// 색상 선택 콤보박스
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(new JLabel("색상:"), gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		String[] colorOptions = { "파랑", "빨강", "회색", "노랑", "직접 선택" };
		JComboBox<String> colorCombo = new JComboBox<>(colorOptions);
		panel.add(colorCombo, gbc);

		// 다이얼로그 표시
		int result = JOptionPane.showConfirmDialog(TagManager.this, panel, "새 태그 추가", JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION)
			return;

		String tagName = tagField.getText().trim();
		if (tagName.isEmpty()) {
			JOptionPane.showMessageDialog(TagManager.this, "태그명을 입력하세요.");
			return;
		}

		String category = (String) categoryCombo.getSelectedItem();
		boolean toStudent = category.equals("학생");

		if (isDuplicateTag(toStudent, tagName)) {
			JOptionPane.showMessageDialog(TagManager.this, "이미 존재하는 태그명입니다.", "중복 태그", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String remark = remarkArea.getText().trim();
		if (remark.isEmpty())
			remark = "-";

		// 색상 처리
		String colorChoice = (String) colorCombo.getSelectedItem();
		Color color;
		switch (colorChoice) {
		case "파랑":
			color = Color.BLUE;
			break;
		case "빨강":
			color = Color.RED;
			break;
		case "회색":
			color = Color.DARK_GRAY;
			break;
		case "노랑":
			color = Color.ORANGE;
			break;
		case "직접 선택":
			color = JColorChooser.showDialog(TagManager.this, "태그 색상 선택", Color.LIGHT_GRAY);
			if (color == null)
				color = Color.LIGHT_GRAY;
			break;
		default:
			color = Color.LIGHT_GRAY;
		}

		addItemToPanel(toStudent, tagName, color, remark); // 👉 중복이 아니면 마지막에 추가
	}

	private void editTagDialog(boolean toStudent, TagRow row) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;

		// 태그명
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("태그명:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		JTextField tagField = new JTextField(row.getTagName(), 20);
		panel.add(tagField, gbc);

		// 카테고리 (수정 불가)
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		panel.add(new JLabel("카테고리:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		String[] categories = { "학생", "산출물" };
		JComboBox<String> categoryCombo = new JComboBox<>(categories);
		categoryCombo.setSelectedItem(toStudent ? "학생" : "산출물");
		categoryCombo.setEnabled(false);
		panel.add(categoryCombo, gbc);

		// 설명
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(new JLabel("설명:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		JTextArea remarkArea = new JTextArea(row.getRemark(), 6, 20);
		remarkArea.setLineWrap(true);
		remarkArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(remarkArea);
		panel.add(scrollPane, gbc);

		// 색상
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(new JLabel("색상:"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;

		String[] colorOptions = { "파랑", "빨강", "회색", "노랑", "직접 선택" };
		JComboBox<String> colorCombo = new JComboBox<>(colorOptions);
		panel.add(colorCombo, gbc);

		// 현재 색상에 맞는 기본 선택 설정
		Color currentColor = row.getTagColor();
		if (currentColor.equals(Color.BLUE)) {
			colorCombo.setSelectedItem("파랑");
		} else if (currentColor.equals(Color.RED)) {
			colorCombo.setSelectedItem("빨강");
		} else if (currentColor.equals(Color.DARK_GRAY)) {
			colorCombo.setSelectedItem("회색");
		} else if (currentColor.equals(Color.ORANGE)) {
			colorCombo.setSelectedItem("노랑");
		} else {
			colorCombo.setSelectedItem("직접 선택");
		}

		// 다이얼로그 표시
		int result = JOptionPane.showConfirmDialog(this, panel, "태그 수정", JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION)
			return;

		String newTagName = tagField.getText().trim();
		if (newTagName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "태그명을 입력하세요.");
			return;
		}

		// 중복 체크 (자기 자신 제외)
		TagListPanel targetPanel = toStudent ? studentList : outputList;
		boolean isDuplicate = false;
		for (Component comp : targetPanel.getComponents()) {
			if (comp instanceof TagRow) {
				TagRow other = (TagRow) comp;
				if (other != row && other.getTagName().equalsIgnoreCase(newTagName)) {
					isDuplicate = true;
					break;
				}
			}
		}
		if (isDuplicate) {
			JOptionPane.showMessageDialog(this, "이미 존재하는 태그명입니다.", "중복 태그", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String newRemark = remarkArea.getText().trim();
		if (newRemark.isEmpty())
			newRemark = "-";

		// 선택한 색상 처리
		String colorChoice = (String) colorCombo.getSelectedItem();
		Color newColor;
		switch (colorChoice) {
		case "파랑":
			newColor = Color.BLUE;
			break;
		case "빨강":
			newColor = Color.RED;
			break;
		case "회색":
			newColor = Color.DARK_GRAY;
			break;
		case "노랑":
			newColor = Color.ORANGE;
			break;
		case "직접 선택":
			newColor = JColorChooser.showDialog(this, "태그 색상 선택", currentColor);
			if (newColor == null)
				newColor = currentColor;
			break;
		default:
			newColor = currentColor;
		}

		// 변경 반영
		String oldTagName = row.getTagName(); // 기존 이름 저장

		row.setTagName(newTagName);
		row.setRemark(newRemark);
		row.setTagColor(newColor);

		// TagRepository에서 기존 태그 객체 찾아서 변경 반영
		List<Tag> targetList = toStudent ? TagRepository.getStudentTags() : TagRepository.getOutputTags();
		for (Tag tag : targetList) {
			if (tag.getName().equalsIgnoreCase(oldTagName)) {
				tag.setName(newTagName);
				tag.setRemark(newRemark);
				tag.setColor(newColor);
				break;
			}
		}

		repaint();
	}

	// TagRow에 클릭 이벤트 핸들러 주입
	private void applyTagRowHandlers(boolean toStudent, TagRow row) {
		row.setOnLeftClick(() -> editTagDialog(toStudent, row));

		row.setOnRightClick(() -> {
			int confirm = JOptionPane.showConfirmDialog(this, "태그를 삭제하시겠습니까?", "태그 삭제", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				TagListPanel panel = toStudent ? studentList : outputList;
				panel.removeRow(row);

				List<Tag> repoList = toStudent ? TagRepository.getStudentTags() : TagRepository.getOutputTags();
				repoList.removeIf(t -> t.getName().equalsIgnoreCase(row.getTagName()));

				panel.revalidate();
				panel.repaint();
				box.autoGrow();
				refreshScroll();
			}
			// ✅ 북마크 클릭 시 정렬 새로고침
			row.setOnBookmarkClick(() -> {
				if (toStudent) {
					studentList.loadStudentTags(); // 내부에서 북마크 정렬됨
					for (Component comp : studentList.getComponents()) {
						if (comp instanceof TagRow) {
							TagRow r2 = (TagRow) comp;
							applyTagRowHandlers(true, r2); // 이벤트 재적용
						}
					}
				} else {
					outputList.loadOutputTags();
					for (Component comp : outputList.getComponents()) {
						if (comp instanceof TagRow) {
							TagRow r2 = (TagRow) comp;
							applyTagRowHandlers(false, r2);
						}
					}
				}
			});
		});
	}

}
