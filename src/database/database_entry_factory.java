package database;

import core.jbotnet;
import storage._factory;

public class database_entry_factory extends _factory<database_entry> {
	private static final long serialVersionUID = 527943763565872264L;

	public database_entry_factory() {
		super();
	}
	
	public boolean create_entry(String username, String comment, String flags) {
		_lock();
		add(new database_entry(username, comment, flags));
		jbotnet.get_db().save();
		_unlock();
		return true;
	}
	
	public boolean edit_entry(String username, String flags) {
		_lock();
		database_entry dbe = get_entry(username);
		if (dbe != null) {
			dbe.set_flags(flags);
			jbotnet.get_db().save();
			return true;
		}
		_unlock();
		return false;
	}
	
	public boolean delete_entry(String username) {
		_lock();
		rem(username);
		jbotnet.get_db().save();
		_unlock();
		return true;
	}
	
	public database_entry get_entry(String username) {
		return get_entries().get(username);
	}
}