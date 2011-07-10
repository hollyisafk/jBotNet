package authentication;

import net.listener;

public abstract class _action {
	public enum action_type {
		SEND_PACKET,
		READ_DB,
		EDIT_DB,
		EDIT_DB_PASSWORD,
		LOGIN,
		CREATE_ACCOUNT,
		CHANGE_PASSWORD,
		CHANGE_DB_PASSWORD,
		CHAT,
		SEND_COMMAND;
	}
	
	private action_type _action;
	
	public _action(action_type action) {
		_action = action;
	}
	
	public action_type get_type() {
		return _action;
	}
	
	public abstract boolean perform(listener client);
}
