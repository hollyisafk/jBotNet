package authentication;

import core.jbotnet;
import user.session;

public class user_protection implements _authenticator {
	public user_protection() {
		//sentry.get_instance().register(this);
	}
	
	public boolean can_do(session client, _action action) {
		if (action.get_type() == _action.action_type.CREATE_ACCOUNT)
			return _can_create_account(client, (action_create_account)action);
		if (action.get_type() == _action.action_type.CHANGE_PASSWORD)
			return _can_change_password(client, (action_change_password)action);
		if (action.get_type() == _action.action_type.LOGIN)
			return _can_login(client, (action_login)action);
		return true;
	}
	
	public boolean _can_create_account(session client, action_create_account action) {
		if (jbotnet.get_acnt().account_exists(action.get_username()))
			return false;
		if (action.get_username().length() == 0 ||
			action.get_password().length() == 0)
			return false;
		
		return true;
	}

	public boolean _can_change_password(session client, action_change_password action) {
		return true;
	}
	
	public boolean _can_login(session client, action_login action) {
		//if (!jbotnet.get_acnt().account_exists(action.get_username()))
		//	return false;
		
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
