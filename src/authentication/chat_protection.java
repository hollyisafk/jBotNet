package authentication;

import user.session;

public class chat_protection implements _authenticator {
	public chat_protection() {
		//sentry.get_instance().register(this);
	}
	
	public boolean can_do(session client, _action action) {
		//if (action.get_type() == _action.action_type.CHAT)
		//	return _can_chat(client, (action_read_db)action);
		return true;
	}
	
	public boolean _can_chat(session client, action_read_db action) {
		// check if they're muted?
		return false;
	}
	
	/* singleton */
	private static chat_protection instance = new chat_protection();
	
	public static chat_protection get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
