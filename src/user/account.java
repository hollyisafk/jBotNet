package user;

import storage._product;

public class account extends _product {
	private static final long serialVersionUID = 2914008190537014119L;
	
	private int create_time;
	private int last_edit_time;
	private String username;
	private String password;
	private int flags;
	
	public account(String u, String p, int f) {
		set_name(u);
		set_create_time(0);
		set_last_edit_time(0);
		set_username(u);
		set_password(p);
		set_flags(f);
	}

	public void set_create_time(int create_time) {
		this.create_time = create_time;
	}

	public int get_create_time() {
		return create_time;
	}

	public void set_last_edit_time(int last_edit_time) {
		this.last_edit_time = last_edit_time;
	}

	public int get_last_edit_time() {
		return last_edit_time;
	}

	public void set_username(String username) {
		this.username = username;
	}

	public String get_username() {
		return username;
	}

	public void set_password(String password) {
		this.password = password;
	}

	public String get_password() {
		return password;
	}

	public void set_flags(int flags) {
		this.flags = flags;
	}

	public int get_flags() {
		return flags;
	}

}
