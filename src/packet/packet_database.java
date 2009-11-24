package packet;

import java.util.ArrayList;

import authentication.*;
import core.*;
import data.*;
import database.*;
import net.*;
import util.*;

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
		command = get_argument("command").get_int();
		//age = get_argument("age").get_int();
		usermask = get_argument("usermask").get_string();
		flags = get_argument("flags").get_string();
		comment = get_argument("comment").get_string();
    	
		char[] flag_set = new char[256];
		
		for (int i = 0; i < 256; i++)
			flag_set[i] = 0x00;
		
		for (int i = 0; i < flags.length(); i++) {
			char cursor = flags.charAt(i);
			if (Character.isLetter(cursor)) {
				cursor = Character.toUpperCase(cursor);
				flag_set[cursor] = cursor;
			}
		}
		
		flags = "";
		for (int i = 0; i < 256; i++)
			if (flag_set[i] != 0x00)
				flags += flag_set[i];
		
    	switch (command) {
    	case 0x01:
    		action_read_db action0 = new action_read_db();
    		if (!sentry.get_instance().can_do(client.get_session(), action0))
    			return;
    		
    		age = 0;
			terminal.print_session(client, "Downloading database");
    		distributor.send_database(client, age);
    		break;
    	case 0x02:
    		action_edit_db action1 = new action_edit_db();
    		if (!sentry.get_instance().can_do(client.get_session(), action1))
    			return;
    		
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
    		action_edit_db action2 = new action_edit_db();
    		if (!sentry.get_instance().can_do(client.get_session(), action2))
    			return;
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
		return out.format(PACKET_DATABASE);
	}

	public static packet_database get_instance() {
		if (instance == null)
			instance = new packet_database();
		return instance;
	}
}
