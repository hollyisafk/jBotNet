package authentication;

import core.jbotnet;
import user.session;

public class server_protection implements _authenticator {
	public server_protection() {
	}
	
	public boolean can_do(session client, _action action) {
		if (action.get_type() == _action.action_type.CREATE_ACCOUNT)
			return _can_create_account(client, (action_create_account)action);
		return true;
	}
	
	public boolean _can_connect(session client, action_create_account action) {
		if (jbotnet.get_acnt().account_exists(action.get_username()))
			return false;
		if (action.get_username().length() == 0 ||
			action.get_password().length() == 0)
			return false;
		
		return true;
	}
	
	/* singleton */
	private static user_protection instance = new user_protection();
	
	public static user_protection get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
