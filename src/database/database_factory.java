package database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		return get_entries().get(name.toLowerCase());
	}
	
	public boolean database_exists(String name) {
		return get_entries().get(name.toLowerCase()) != null ? true : false;
	}
	
	public boolean create_database(String name, String rp, String wp, String ap) {
		//_lock();
		if (database_exists(name))
			return false;
		
		Pattern p = Pattern.compile("^[A-Za-z0-9_]*$");
		Matcher m = p.matcher(name);
		boolean b = m.matches();
		
		if (!b)
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
