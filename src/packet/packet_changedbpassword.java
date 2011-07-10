package packet;

import java.util.ArrayList;

import authentication._action;
import authentication.action_change_db_password;
import authentication.sentry;

import net.listener;

public class packet_changedbpassword extends _packet {
	private static packet_changedbpassword instance = null;
	private int m_password;
	private String m_new_password;
	
	private packet_changedbpassword() {
		super(PACKET_CHANGEDBPASSWORD);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("password"));
		args.add(new argument_string("newpassword"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		try {
			m_password = get_argument("password").get_int();
			m_new_password = get_argument("newpassword").get_string();
			_action action = new action_change_db_password(m_password, m_new_password);
			sentry.get_instance().perform(client, action);
		} catch (Exception e) {
			//
		}
	}
	
	public static byte[] build() {
		return null;
	}

	public static packet_changedbpassword get_instance() {
		if (instance == null)
			instance = new packet_changedbpassword();
		return instance;
	}
}
