package database;

import storage._product;

public class database_entry extends _product {
	private static final long serialVersionUID = 4528026304434855371L;
	
	private long create_time;
	private long last_edit_time;
	private String username;
	private String comment;
	private String flags;
	
	public database_entry(String username, String comment, String flags) {
		this.set_create_time(System.currentTimeMillis() / 1000);
		this.set_name(username);
		this.setUsername(username);
		this.set_comment(comment);
		this.set_flags(flags);
	}

	public void set_create_time(long create_time) {
		this.create_time = create_time;
	}

	public long get_create_time() {
		return create_time;
	}

	public void set_last_edit_time(long last_edit_time) {
		this.last_edit_time = last_edit_time;
	}

	public long get_last_edit_time() {
		return last_edit_time;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String get_username() {
		return username;
	}

	public void set_comment(String comment) {
		this.comment = comment;
	}

	public String get_comment() {
		return comment;
	}

	public void set_flags(String flags) {
		this.flags = flags;
	}

	public String get_flags() {
		return flags;
	}
}
