package packet;

import java.util.ArrayList;

import net.listener;
import user.session;
import util.buffer_out;
import core.jbotnet;
import core.terminal;
import data.distributor;
import database.database_entry;

public class packet_database extends _packet {
	private static packet_database instance = null;
	
	private int command;
	private int age;
	private String usermask;
	private String flags;
	private String comment;
	
	private packet_database() {
		super(PACKET_DATABASE);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("command"));
		//args.add(new argument_dword("age"));
		args.add(new argument_string("usermask"));
		args.add(new argument_string("flags"));
		args.add(new argument_string("comment"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		if (!client.get_session().is_state(session.LOGONSTATE_IDENTIFIED)) {
    		return;
    	}

		command = get_argument("command").get_int();
		//age = get_argument("age").get_int();
		usermask = get_argument("usermask").get_string();
		flags = get_argument("flags").get_string();
		comment = get_argument("comment").get_string();
    	
    	switch (command) {
    	case 0x01:
    		if (client.get_session().get_jbnflags() < 0x01 || client.get_session().get_jbndatabase() == null) {
    			break;
    		}
    		
    		age = 0;
			terminal.print_session(client, "Downloading database");
    		distributor.send_database(client, age);
    		break;
    	case 0x02:
    		if (client.get_session().get_jbnflags() < 0x02 || client.get_session().get_jbndatabase() == null) {
    			break;
    		}
    		
    		database_entry dbe = client.get_session().get_jbndatabase().get_entry(usermask);
    		if (dbe == null) {
    			terminal.print_session(client, "Creating user entry: " + usermask + " => " + flags);
    			client.get_session().get_jbndatabase().create_entry(usermask, comment, flags);
    			distributor.update_usermask(client.get_session(), client.get_session().get_jbndatabase(), client.get_session().get_jbndatabase().get_entry(usermask), comment);
    		} else {
    			terminal.print_session(client, "Editing user entry: " + usermask + " => " + flags);
    			dbe.set_flags(flags);
    			jbotnet.get_db().save();
    			distributor.update_usermask(client.get_session(), client.get_session().get_jbndatabase(), dbe, comment);
    		}
    		break;
    	case 0x03:
    		if (client.get_session().get_jbnflags() < 0x02 || client.get_session().get_jbndatabase() == null) {
    			break;
    		}
    		comment = flags;
    		
    		dbe = client.get_session().get_jbndatabase().get_entry(usermask);
    		if (dbe == null) {
    			//client.session.jbndatabase.create_entry(usermask, comment, flags);
    		} else {
    			terminal.print_session(client, "Deleting user entry: " + usermask);
    			client.get_session().get_jbndatabase().delete_entry(usermask);
    			distributor.delete_usermask(client.get_session(), client.get_session().get_jbndatabase(), dbe, comment);
    		}
    	}
	}
	
	public static byte[] build(int response, int address) {
		buffer_out out = new buffer_out();
		// TODO: move code here
		return out.format(PACKET_DATABASE);
	}

	public static packet_database get_instance() {
		if (instance == null)
			instance = new packet_database();
		return instance;
	}
}
