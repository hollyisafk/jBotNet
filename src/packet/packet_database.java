package packet;

import java.util.ArrayList;

import authentication.*;
import database.*;
import net.*;
import util.*;

public class packet_database extends _packet {
	private static packet_database instance = null;
	
	private int command;
	//private int age;
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
		_action action;
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
    		action = new action_read_db(0);
    		sentry.get_instance().perform(client, action);
    		break;
    	case 0x02:
    		action = new action_edit_db(usermask, flags, comment);
    		sentry.get_instance().perform(client, action);
    		break;
    	case 0x03:
    		action = new action_edit_db(usermask, "", comment);
    		sentry.get_instance().perform(client, action);
    		break;
    	}
	}
	
	public static byte[] build_1(int subtype) {
		buffer_out out = new buffer_out();
		out.insertDword(0x01);
		out.insertDword(subtype);
		return out.format(PACKET_DATABASE);
	}

	public static byte[] build_2(database_entry entry, int uid) {
		buffer_out out = new buffer_out();
		out.insertDword(0x02);
		out.insertDword(uid);
		out.insertDword(entry.get_last_edit_time());
		out.insertNTString(entry.get_username());
		out.insertNTString(entry.get_flags());
		out.insertNTString("");
		return out.format(PACKET_DATABASE);
	}

	public static byte[] build_3(database_entry entry, int uid, String comment) {
		buffer_out out = new buffer_out();
		out.insertDword(0x03);
		out.insertDword(uid);
		out.insertDword(entry.get_last_edit_time());
		out.insertNTString(entry.get_username());
		out.insertNTString(comment);
		return out.format(PACKET_DATABASE);
	}

	public static packet_database get_instance() {
		if (instance == null)
			instance = new packet_database();
		return instance;
	}
}
