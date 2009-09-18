package database;

public class database extends _product {
	private static final long serialVersionUID = 8659010261409419224L;
	
	public String dbname;
	public String read_password;
	public String write_password;
	public String admin_password;
	
	public database_entry_factory entries;
	
	public database(String n, String rp, String wp, String ap) {
		name = n;
		dbname = n;
		read_password = rp;
		write_password = wp;
		admin_password = ap;
		entries = new database_entry_factory();
	}
	
	public database_entry get_entry(String username) {
		return entries.get_entry(username);
	}
	
	public boolean create_entry(String username, String flags) {
		entries.add(new database_entry(username, flags));
		return true;
	}
}
