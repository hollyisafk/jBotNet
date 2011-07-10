package authentication;

import net.listener;
import packet.packet_account;
import user.account;
import core.jbotnet;
import core.terminal;
import data.distributor;

public class action_login extends _action {
	private String m_username;
	private String m_password;
	
	public action_login(String username, String password) {
		super(action_type.LOGIN);
		m_username = username;
		m_password = password;
	}
	
	public boolean perform(listener client) {
		account a;
		if (!jbotnet.get_acnt().account_exists(get_username())) {
			terminal.print_session(client, "Account logon failed: invalid account");
			client.send(packet_account.build(0x00, 0x00));
    		return false;
		}
		
		a = jbotnet.get_acnt().get_account(get_username());
		if (!a.get_password().equals(get_password())) {
			terminal.print_session(client, "Account logon failed: invalid password");
			client.send(packet_account.build(0x00, 0x00));
    		return false;
		}
		terminal.print_session(client, "Account logon passed");
		
		client.get_session().set_jbnaccount(a);
		client.get_session().set_state(login_procedure.LOGONSTATE_LOGGED_IN);

		client.send(packet_account.build(0x00, 0x01));
		
		distributor.send_user_logon(client.get_session());
		return true;
	}

	public String get_username() {
		return m_username;
	}
	
	public String get_password() {
		return m_password;
	}
}
