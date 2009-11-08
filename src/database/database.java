package database;

import storage._product;

public class database extends _product {
	private static final long serialVersionUID = 8659010261409419224L;
	
	private String dbname;
	private String read_password;
	private String write_password;
	private String admin_password;
	
	private database_entry_factory entries;
	
	public database(String n, String rp, String wp, String ap) {
		set_name(n);
		set_dbname(n);
		set_read_password(rp);
		set_write_password(wp);
		set_admin_password(ap);
		set_entries(new database_entry_factory());
	}
	
	public database_entry get_entry(String username) {
		return get_entries().get_entry(username);
	}
	
	public boolean create_entry(String username, String comment, String flags) {
		return get_entries().create_entry(username, comment, flags);
	}
	
	public boolean edit_entry(String username, String flags) {
		return get_entries().edit_entry(username, flags);
	}
	
	public boolean delete_entry(String username) {
		return get_entries().delete_entry(username);
	}

	public void set_dbname(String dbname) {
		this.dbname = dbname;
	}

	public String get_dbname() {
		return dbname;
	}

	public void set_read_password(String read_password) {
		this.read_password = read_password;
	}

	public String get_read_password() {
		return read_password;
	}

	public void set_write_password(String write_password) {
		this.write_password = write_password;
	}

	public String get_write_password() {
		return write_password;
	}

	public void set_admin_password(String admin_password) {
		this.admin_password = admin_password;
	}

	public String get_admin_password() {
		return admin_password;
	}

	public void set_entries(database_entry_factory entries) {
		this.entries = entries;
	}

	public database_entry_factory get_entries() {
		return entries;
	}
}
