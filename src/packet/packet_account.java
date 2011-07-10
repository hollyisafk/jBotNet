package packet;

import java.util.ArrayList;
import authentication._action;
import authentication.action_change_password;
import authentication.action_create_account;
import authentication.action_login;
import authentication.sentry;
import net.listener;
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
		_action action;
		
		command = get_argument("command").get_int();
		name = get_argument("name").get_string();
		pass = get_argument("pass").get_string();
		newpass = get_argument("newpass").get_string();
    	
		String accountname = name;
		String accountpass = pass;
		String accountnewpass = newpass;
    	
    	switch (command) {
    	// login
    	case 0x00:
    		action = new action_login(accountname, accountpass);
    		sentry.get_instance().perform(client, action);
    		break;
    	// change password
    	case 0x01:
    		action = new action_change_password(accountname, accountpass, accountnewpass);
    		sentry.get_instance().perform(client, action);
    		break;
    	// create account
    	case 0x02:
    		action = new action_create_account(accountname, accountpass);
    		sentry.get_instance().perform(client, action);
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
