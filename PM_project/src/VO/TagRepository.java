package VO;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagRepository {
	public static final List<Tag> studentTags = new ArrayList<>();
	public static final List<Tag> outputTags = new ArrayList<>();

	// 북마크 상태 저장용 맵
	private static final Map<String, Boolean> bookmarkMap = new HashMap<>();

	static {
		initDefaultTags(); // 최초 초기화
	}

	public static void initDefaultTags() {
		// 초기화 전에 기존 북마크 상태 저장
		bookmarkMap.clear();
		for (Tag tag : getAllTags()) {
			bookmarkMap.put(tag.getName(), tag.isBookmarked());
		}

		// 기존 태그 리스트 백업 (이전 객체 재사용 위해)
		List<Tag> oldTags = new ArrayList<>(getAllTags());

		// 태그 리스트 초기화
		studentTags.clear();
		outputTags.clear();

		// 기본 학생 태그
		addOrReuseStudentTag("우수", "성적이 우수한 학생", Color.BLUE, oldTags);
		addOrReuseStudentTag("주의", "행동 주의 필요", Color.RED, oldTags);
		addOrReuseStudentTag("신입", "신규 등록 학생", Color.ORANGE, oldTags);

		// 기본 산출물 태그
		addOrReuseOutputTag("완료", "작업 완료됨", Color.GREEN, oldTags);
		addOrReuseOutputTag("검토필요", "검토 필요함", Color.YELLOW, oldTags);
		addOrReuseOutputTag("미완", "아직 미완성", Color.DARK_GRAY, oldTags);
	}

	private static void addOrReuseStudentTag(String name, String remark, Color color, List<Tag> oldTags) {
		Tag existing = findTagByNameInList(name, oldTags);
		if (existing != null) {
			existing.setRemark(remark);
			existing.setColor(color);
			// 북마크 상태 복원
			existing.setBookmarked(bookmarkMap.getOrDefault(name, false));
			studentTags.add(existing);
		} else {
			Tag tag = new Tag(name, remark, color);
			tag.setBookmarked(bookmarkMap.getOrDefault(name, false));
			studentTags.add(tag);
		}
	}

	private static void addOrReuseOutputTag(String name, String remark, Color color, List<Tag> oldTags) {
		Tag existing = findTagByNameInList(name, oldTags);
		if (existing != null) {
			existing.setRemark(remark);
			existing.setColor(color);
			// 북마크 상태 복원
			existing.setBookmarked(bookmarkMap.getOrDefault(name, false));
			outputTags.add(existing);
		} else {
			Tag tag = new Tag(name, remark, color);
			tag.setBookmarked(bookmarkMap.getOrDefault(name, false));
			outputTags.add(tag);
		}
	}

	private static Tag findTagByNameInList(String tagName, List<Tag> tags) {
		return tags.stream().filter(t -> t.getName().equalsIgnoreCase(tagName)).findFirst().orElse(null);
	}

	public static void clearAll() {
		// 북마크 상태 백업
		bookmarkMap.clear();
		for (Tag tag : getAllTags()) {
			bookmarkMap.put(tag.getName(), tag.isBookmarked());
		}

		// 태그 목록 재초기화
		initDefaultTags();
	}

	public static List<Tag> getAllTags() {
		List<Tag> allTags = new ArrayList<>();
		allTags.addAll(studentTags);
		allTags.addAll(outputTags);
		return allTags;
	}

	public static Tag findTagByName(String tagName) {
		return getAllTags().stream().filter(t -> t.getName().equalsIgnoreCase(tagName)).findFirst().orElse(null);
	}

	public static void setBookmark(String tagName, boolean bookmarked) {
		Tag tag = findTagByName(tagName);
		if (tag != null) {
			tag.setBookmarked(bookmarked);
			bookmarkMap.put(tag.getName(), bookmarked); // 북마크 상태 저장
		}
	}

	public static List<Tag> getBookmarkedTags() {
		List<Tag> bookmarked = new ArrayList<>();
		for (Tag t : getAllTags()) {
			if (t.isBookmarked()) {
				bookmarked.add(t);
			}
		}
		return bookmarked;
	}

	public static List<Tag> getStudentTags() {
		return studentTags;
	}

	public static List<Tag> getOutputTags() {
		return outputTags;
	}
}