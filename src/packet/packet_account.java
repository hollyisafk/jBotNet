package packet;

import java.util.ArrayList;

import core.jbotnet;
import core.terminal;
import data.distributor;
import net.listener;
import user.account;
import user.session;
import util.buffer_out;

public class packet_account extends _packet {
	private static packet_account instance = null;
	
	private int command;
	private String name;
	private String pass;
	private String newpass;
	
	private packet_account() {
		super(PACKET_ACCOUNT);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("command"));
		args.add(new argument_string("name"));
		args.add(new argument_string("pass"));
		args.add(new argument_string("newpass"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		command = get_argument("command").get_int();
		name = get_argument("name").get_string();
		pass = get_argument("pass").get_string();
		newpass = get_argument("newpass").get_string();
		
		if (!client.get_session().is_state(session.LOGONSTATE_LOGON_PASSED)) {
			terminal.print_session(client, "Account failed: invalid state");
    		// dont do anything i guess
    		return;
    	}
    	
		String accountname = name;
		String accountpass = pass;
		String accountnewpass = newpass;
		account a;
    	
    	switch (command) {
    	// login
    	case 0x00:
    		if (!jbotnet.get_acnt().account_exists(accountname)) {
    			terminal.print_session(client, "Account logon failed: invalid account");
    			client.send(build(command, 0x00));
        		break;
    		}
    		
    		a = jbotnet.get_acnt().get_account(accountname);
    		if (!a.get_password().equals(accountpass)) {
    			terminal.print_session(client, "Account logon failed: invalid password");
    			client.send(build(command, 0x00));
        		break;
    		}
			terminal.print_session(client, "Account logon passed");
    		
			client.get_session().set_jbnaccount(a);

			client.send(build(command, 0x01));
    		
    		distributor.send_user_logon(client.get_session());
    		break;
    	// change password
    	case 0x01:
    		if (!jbotnet.get_acnt().account_exists(accountname)) {
    			terminal.print_session(client, "Account password change failed: invalid account");
    			client.send(build(command, 0x00));
        		break;
    		}
    		
    		a = jbotnet.get_acnt().get_account(accountname);
    		if (!a.get_password().equals(accountpass)) {
    			terminal.print_session(client, "Account password change failed: invalid password");
    			client.send(build(command, 0x00));
        		break;
    		}
    		
    		if (jbotnet.get_acnt().change_password(accountname, accountnewpass)) {
    			terminal.print_session(client, "Account password change passed");
    			client.send(build(command, 0x01));
    		} else {
    			terminal.print_session(client, "Account password change failed: unknown");
    			client.send(build(command, 0x00));
    		}
    		break;
    	// create account
    	case 0x02:
    		String create = jbotnet.get_acnt().create_account(accountname, accountpass, 0);
    		
    		if (create == null) {
    			terminal.print_session(client, "Account create passed");
    			client.send(build(command, 0x01));
    		} else {
    			terminal.print_session(client, create);
    			client.send(build(command, 0x00));
    		}
    	}
	}
	
	public static byte[] build(int command, int response) {
		buffer_out out = new buffer_out();
		out.insertDword(command);
		out.insertDword(response);
		return out.format(PACKET_ACCOUNT);
	}

	public static packet_account get_instance() {
		if (instance == null)
			instance = new packet_account();
		return instance;
	}
}
