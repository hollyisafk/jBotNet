package authentication;

public abstract class _action {
	public enum action_type {
		SEND_PACKET,
		READ_DB,
		EDIT_DB,
		EDIT_DB_PASSWORD;
	}
	
	private action_type _action;
	
	public _action(action_type action) {
		_action = action;
	}
	
	public action_type get_action() {
		return _action;
	}
}
