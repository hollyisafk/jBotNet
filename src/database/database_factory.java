package database;

import storage._warehouse;

public class database_factory extends _warehouse<database> {
	private static final long serialVersionUID = 2130899247165277029L;

	private static database_factory instance = new database_factory();
	
	public static database_factory get_instance() {
		// no lazy loading
		return instance;
	}
	
	private database_factory() {
		super(System.getProperty("user.dir") + "/data/databases.jbn");
		load();
	}
	
	public database get_database(String name) {
		return get_entries().get(name);
	}
	
	public boolean database_exists(String name) {
		return get_entries().get(name) != null ? true : false;
	}
	
	public boolean create_database(String name, String rp, String wp, String ap) {
		//_lock();
		if (database_exists(name))
			return false;
		
		add(new database(name, rp, wp, ap));
		save();
		
		//_unlock();
		return true;
	}
	
	public void set_read_password(String name, String password) {
		_lock();
		get_database(name).set_read_password(password);
		save();		
		_unlock();
	}
	
	public void set_write_password(String name, String password) {
		_lock();
		get_database(name).set_write_password(password);
		save();		
		_unlock();
	}
	
	public void set_admin_password(String name, String password) {
		_lock();
		get_database(name).set_admin_password(password);
		save();		
		_unlock();
	}
}
