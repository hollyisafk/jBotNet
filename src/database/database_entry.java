package database;

public class database_entry {
	int create_time;
	int last_edit_time;
	String username;
	String flags;
	
	public database_entry(String username, String flags) {
		this.username = username;
		this.flags = flags;
	}
}
