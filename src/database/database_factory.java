package database;

public class database_factory extends _factory<database> {
	public database_factory() {
		super(System.getProperty("user.dir") + "/data/databases.jbn");
	}
	
	public database get_database(String name) {
		for (database db : entries) {
			if (db.name.equalsIgnoreCase(name)) {
				return db;
			}
		}
		
		return null;
	}
	
	public boolean create_database(String name, String rp, String wp, String ap) {
		boolean exists = false;
		
		for (database db : entries) {
			if (db.name.equalsIgnoreCase(name)) {
				exists = true;
				break;
			}
		}
		
		if (exists)
			return false;
		
		add(new database(name, rp, wp, ap));
		save();
		
		return true;
	}
	
	public void set_read_password(String name, String password) {
		get_database(name).read_password = password;
		save();
	}
	
	public void set_write_password(String name, String password) {
		get_database(name).write_password = password;
	}
	
	public void set_admin_password(String name, String password) {
		get_database(name).admin_password = password;
	}
}
