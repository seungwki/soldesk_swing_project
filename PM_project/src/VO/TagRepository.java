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
		addOrReuseStudentTag("주의", "관심 필요", Color.RED, oldTags);
		addOrReuseStudentTag("신입", "3개월 이내의 학생", Color.ORANGE, oldTags);
		addOrReuseStudentTag("팀장", "리더쉽 위주", Color.green, oldTags);
		addOrReuseStudentTag("멘토", "친근하며 가르침 능숙", Color.BLUE, oldTags);
		addOrReuseStudentTag("발표", "발표 담당", Color.ORANGE, oldTags);
		addOrReuseStudentTag("자료", "자료조사자", Color.ORANGE, oldTags);

		// 기본 산출물 태그
		addOrReuseOutputTag("완료", "작업 완료됨", Color.blue, oldTags);
		addOrReuseOutputTag("미완", "아직 미완성", Color.ORANGE, oldTags);
		addOrReuseOutputTag("수작", "훌륭함", Color.BLUE, oldTags);
		addOrReuseOutputTag("초안", "상의단계", Color.LIGHT_GRAY, oldTags);
		addOrReuseOutputTag("통과", "채점 완료", Color.LIGHT_GRAY, oldTags);
		addOrReuseOutputTag("지각", "기한오버", Color.red, oldTags);
		addOrReuseOutputTag("파일없음", "자료,파일 누락", Color.red, oldTags);
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
