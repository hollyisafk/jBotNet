package database;

public class account extends _product {
	private static final long serialVersionUID = 2914008190537014119L;
	
	public int create_time;
	public int last_edit_time;
	public String username;
	public String password;
	public int flags;
	
	public account(String u, String p, int f) {
		name = u;
		create_time = 0;
		last_edit_time = 0;
		username = u;
		password = p;
		flags = f;
	}
}
