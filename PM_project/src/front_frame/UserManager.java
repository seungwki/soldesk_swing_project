package front_frame;

import java.util.HashMap;

import VO.User;

public class UserManager {
	private static UserManager instance = new UserManager();
	private HashMap<String, User> users = new HashMap<>();
	private String currentUserId;

	private UserManager() {
		// 테스트용 초기 계정
		users.put("admin", new User("admin", "admin", "관리자", "비공개", "admin@example.com", true));
	}

	public static UserManager getInstance() {
		return instance;
	}

	public boolean isDuplicate(String id) {
		return users.containsKey(id);
	}

	public void addUser(User user) {
		users.put(user.getId(), user);
	}

	public User getUser(String id) {
		return users.get(id);
	}

	public boolean login(String id, String pw) {
		User user = users.get(id);
		boolean ok = user != null && user.getPassword().equals(pw);
		if (ok)
			setCurrentUser(id); // ← 이 한 줄 추가
		return ok;
	}

	public void setCurrentUser(String id) {
		this.currentUserId = id;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public User getCurrentUser() {
		return currentUserId == null ? null : users.get(currentUserId);
	}

	private final java.util.List<Runnable> profileListeners = new java.util.ArrayList<>();

	public void addProfileListener(Runnable r) {
		profileListeners.add(r);
	}

	public void removeProfileListener(Runnable r) {
		profileListeners.remove(r);
	}

	public void notifyProfileChanged() {
		for (Runnable r : new java.util.ArrayList<>(profileListeners))
			r.run();
	}

	// ---- 현재 사용자 프로필 이미지 경로 ----
	// (클래스패스 기준. 파일시스템 쓰면 경로 로더만 바꾸면 됨)
	public String getCurrentProfileImagePath() {
	    User u = getCurrentUser();
	    String g = (u == null || u.getGender() == null) ? "비공개" : u.getGender();
	    return switch (g) {
	        case "남" -> "profile_male.png";
	        case "여" -> "profile_female.png";
	        default   -> "profile_secret.png";
	    };
	}
}
