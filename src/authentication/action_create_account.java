package authentication;

import packet.packet_account;
import core.jbotnet;
import core.terminal;
import net.listener;

public class action_create_account extends _action {
	private String m_username;
	private String m_password;
	
	public action_create_account(String username, String password) {
		super(action_type.CREATE_ACCOUNT);
		m_username = username;
		m_password = password;
	}
	
	public boolean perform(listener client) {
		String create = jbotnet.get_acnt().create_account(get_username(), get_password(), 0);
		
		if (create == null) {
			terminal.print_session(client, "Account create passed");
			client.send(packet_account.build(0x02, 0x01));
			return true;
		} else {
			terminal.print_session(client, create);
			client.send(packet_account.build(0x02, 0x00));
			return false;
		}
	}

	public String get_username() {
		return m_username;
	}
	
	public String get_password() {
		return m_password;
	}
}
