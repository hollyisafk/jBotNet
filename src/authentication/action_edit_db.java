package authentication;

import net.listener;
import user.session;
import core.jbotnet;
import core.terminal;
import data.distributor;
import database.database;
import database.database_entry;

public class action_edit_db extends _action {
	private String m_usermask;
	private String m_flags;
	private String m_comment;
	
	public action_edit_db(String usermask, String flags, String comment) {
		super(action_type.EDIT_DB);
		m_usermask = usermask;
		m_flags = flags;
		m_comment = comment;
	}
	
	public boolean perform(listener client) {
		try {
			session s = client.get_session();
			database db = s.get_jbndatabase();
			database_entry dbe = db.get_entry(get_usermask());
			if (get_flags().length() == 0) {
	    		if (dbe == null) {
	    			return false;
	    		} else {
	    			terminal.print_session(client, "Deleting user entry from " + db.get_dbname() + ": " + get_usermask());
	    			db.delete_entry(get_usermask());
	    			distributor.delete_usermask(client.get_session(), client.get_session().get_jbndatabase(), dbe, get_comment());
	    		}
			} else {
				if (dbe == null) {
					terminal.print_session(client, "Creating user entry in " + db.get_dbname() + ": " + get_usermask() + " => " + get_flags());
					db.create_entry(get_usermask(), get_comment(), get_flags());
					distributor.update_usermask(s, db, db.get_entry(get_usermask()), get_comment());
				} else {
					terminal.print_session(client, "Editing user entryin " + db.get_dbname() + ": " + get_usermask() + " => " + get_flags());
					dbe.set_flags(get_flags());
					jbotnet.get_db().save();
					distributor.update_usermask(s, db, dbe, get_comment());
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String get_usermask() {
		return m_usermask;
	}
	
	public String get_flags() {
		return m_flags;
	}
	
	public String get_comment() {
		return m_comment;
	}
}
