package database;

public class database_entry extends _product {
	private static final long serialVersionUID = -4528026304434855371L;
	
	public int create_time;
	public int last_edit_time;
	public String username;
	public String flags;
	
	public database_entry(String username, String flags) {
		this.name = username;
		this.username = username;
		this.flags = flags;
	}
}
