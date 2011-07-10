package authentication;

import net.listener;
import packet.packet_account;
import user.account;
import core.jbotnet;
import core.terminal;

public class action_change_password extends _action {
	private String m_username;
	private String m_password;
	private String m_new_password;
	
	public action_change_password(String username, String password, String new_password) {
		super(action_type.CHANGE_PASSWORD);
		m_username = username;
		m_password = password;
		m_new_password = new_password;
	}
	
	public boolean perform(listener client) {
		account a;
		
		if (!jbotnet.get_acnt().account_exists(get_username())) {
			terminal.print_session(client, "Account password change failed: invalid account");
			client.send(packet_account.build(0x01, 0x00));
    		return false;
		}
		
		a = jbotnet.get_acnt().get_account(get_username());
		if (!a.get_password().equals(get_password())) {
			terminal.print_session(client, "Account password change failed: invalid password");
			client.send(packet_account.build(0x01, 0x00));
    		return false;
		}
		
		if (jbotnet.get_acnt().change_password(get_username(), get_new_password())) {
			terminal.print_session(client, "Account password change passed");
			client.send(packet_account.build(0x01, 0x01));
			return true;
		} else {
			terminal.print_session(client, "Account password change failed: unknown");
			client.send(packet_account.build(0x01, 0x00));
			return false;
		}
	}

	public String get_username() {
		return m_username;
	}

	public String get_password() {
		return m_password;
	}
	
	public String get_new_password() {
		return m_new_password;
	}
}
