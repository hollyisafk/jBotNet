package database;

public class account {
	int create_time;
	int last_edit_time;
	String username;
	String password;
	int flags;
	
	public account(String u, String p, int f) {
		create_time = 0;
		last_edit_time = 0;
		username = u;
		password = p;
		flags = f;
	}
}
