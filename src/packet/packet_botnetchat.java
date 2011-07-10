package packet;

import java.util.ArrayList;

import authentication.*;

import net.listener;
import util.buffer_out;

public class packet_botnetchat extends _packet {
	private static packet_botnetchat instance = null;
	
	private int command;
	private int action;
	private int clientid;
	private String message;
	
	private packet_botnetchat() {
		super(PACKET_BOTNETCHAT);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("command"));
		args.add(new argument_dword("action"));
		args.add(new argument_dword("clientid"));
		args.add(new argument_string("message"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		command = get_argument("command").get_int();
		action = get_argument("action").get_int();
		clientid = get_argument("clientid").get_int();
		message = get_argument("message").get_string();
		
		_action a = new action_chat(command, action, clientid, message);
		sentry.get_instance().perform(client, a);
	}
	
	public static byte[] build(int command, int action, int uid, String message) {
		buffer_out out = new buffer_out();
    	out.insertDword(command);
    	out.insertDword(action);
    	out.insertDword(uid);
    	out.insertNTString(message);
		return out.format(PACKET_BOTNETCHAT);
	}

	public static packet_botnetchat get_instance() {
		if (instance == null)
			instance = new packet_botnetchat();
		return instance;
	}
}
