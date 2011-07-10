package packet;

import java.util.ArrayList;

import authentication._action;
import authentication.action_send_command;
import authentication.sentry;
import net.listener;
import util.buffer_out;

public class packet_command extends _packet {
	private static packet_command instance = null;
	
	private int senderid;
	private int status;
	private String sender;
	private String command;
	
	private packet_command() {
		super(PACKET_COMMAND);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("senderid"));
		args.add(new argument_dword("status"));
		args.add(new argument_string("sender"));
		args.add(new argument_string("command"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		senderid = get_argument("senderid").get_int();
		status = get_argument("status").get_int();
		sender = get_argument("sender").get_string();
		command = get_argument("command").get_string();
		
		_action action = new action_send_command(senderid, status, sender, command);
		sentry.get_instance().perform(client, action);
	}
	
	public static byte[] build(int response, int address) {
		buffer_out out = new buffer_out();
		out.insertDword(response);
		out.insertDword(address);
		return out.format(PACKET_LOGON);
	}

	public static packet_command get_instance() {
		if (instance == null)
			instance = new packet_command();
		return instance;
	}
}
