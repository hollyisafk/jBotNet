package packet;

import java.util.ArrayList;
import net.listener;
import util.buffer_out;
import core.jbotnet;
import core.terminal;

public class packet_command extends _packet {
	private static packet_command instance = null;
	
	private int sender_id;
	private int status;
	private String sender;
	private String command;
	
	private packet_command() {
		super(PACKET_COMMAND);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("sender_id"));
		args.add(new argument_dword("status"));
		args.add(new argument_string("sender"));
		args.add(new argument_string("command"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		sender_id = get_argument("sender_id").get_int();
		status = get_argument("status").get_int();
		sender = get_argument("sender").get_string();
		command = get_argument("command").get_string();
		
		out.insertDword(client.get_session().get_uid());
		out.insertDword(status);
		out.insertNTString(sender);
		out.insertNTString(command);
		
		terminal.print_session(client, "Sending command: " + command);
		
		switch (status) {
		case 0x01:
        	for (listener l : jbotnet.get_svr().listeners) {
        		if (l.get_session().get_jbndatabase() == null || client.get_session().get_jbndatabase() == null)
        			continue;
        		if (l.get_session().get_uid() != client.get_session().get_uid()
        				&& l.get_session().get_jbndatabase().get_dbname().equals(client.get_session().get_jbndatabase().get_dbname())
        				&& l.get_session().get_jbndatabase().get_dbname().length() != 0) {
        			l.send(out.format(PACKET_MESSAGE));
        		}
        	}
			break;
		case 0x02:
        	for (listener l : jbotnet.get_svr().listeners) {
        		if (l.get_session().get_uid() == sender_id)
        			l.send(out.format(PACKET_MESSAGE));
        	}
		}
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
