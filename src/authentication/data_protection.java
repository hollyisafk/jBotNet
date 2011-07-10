package authentication;

import user.session;

public class data_protection implements _authenticator {
	public data_protection() {
		//sentry.get_instance().register(this);
	}
	
	public boolean can_do(session client, _action action) {
		if (action.get_type() == _action.action_type.READ_DB)
			return _can_read_db_entry(client, (action_read_db)action);
		if (action.get_type() == _action.action_type.EDIT_DB)
			return _can_edit_db_entry(client, (action_edit_db)action);
		if (action.get_type() == _action.action_type.CHANGE_DB_PASSWORD)
			return _can_change_db_password(client, (action_change_db_password)action);
		return true;
	}
	
	public boolean _can_read_db_entry(session client, action_read_db action) {
		if (client.get_jbndatabase() != null &&
			client.get_jbnflags() >= 0x01) {
			return true;
		}
		return false;
	}
	
	public boolean _can_edit_db_entry(session client, action_edit_db action) {
		if (client.get_jbndatabase() != null &&
			client.get_jbnflags() >= 0x02) {
			return true;
		}
		return false;
	}
	
	public boolean _can_change_db_password(session client, action_change_db_password action) {
		if (client.get_jbndatabase() != null &&
			client.get_jbnflags() == 0x04) {
			return true;
		}
		return false;
	}
	
	/* singleton */
	private static data_protection instance = new data_protection();
	
	public static data_protection get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
