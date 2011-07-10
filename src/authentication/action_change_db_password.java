package authentication;

import core.jbotnet;
import database.database;
import user.session;
import net.listener;

public class action_change_db_password extends _action {
	private int m_password;
	private String m_new_password;
	
	public action_change_db_password(int password, String new_password) {
		super(action_type.CHANGE_DB_PASSWORD);
		m_password = password;
		m_new_password = new_password;
	}
	
	public boolean perform(listener client) {
		try {
			session s = client.get_session();
			database db = s.get_jbndatabase();
			
			switch (get_password()) {
			case 0:
				db.set_read_password(get_new_password());
				break;
			case 1:
				db.set_write_password(get_new_password());
				break;
			case 2:
				db.set_admin_password(get_new_password());
				break;
			default:
				return false;
			}

			jbotnet.get_db().save();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int get_password() {
		return m_password;
	}
	
	public String get_new_password() {
		return m_new_password;
	}
}
