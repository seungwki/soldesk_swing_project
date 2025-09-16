package VO;

public class User {
    private String id;
    private String password;
    private String name;
    private String gender;
    private String email;
    private boolean agreed;

    public User(String id, String password, String name, String gender, String email, boolean agreed) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.agreed = agreed;
    }

    // getter만 필요하면 여기에 추가
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public boolean isAgreed() { return agreed; }

	public void setGender(String gender) {
		this.gender = gender;
	}

	
}