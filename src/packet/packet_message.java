package packet;

import java.util.ArrayList;
import net.listener;
import util.buffer_out;
import core.jbotnet;
import core.terminal;

public class packet_message extends _packet {
	private static packet_message instance = null;
	
	private String sender;
	private String message;
	
	private packet_message() {
		super(PACKET_MESSAGE);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_string("sender"));
		args.add(new argument_string("message"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		sender = get_argument("sender").get_string();
		message = get_argument("message").get_string();
		
		terminal.print_session(client, "Sending message: " + message);
		byte[] data = build(client.get_session().get_uid(), 0x01, sender, message);

    	for (listener l : jbotnet.get_svr().listeners) {
    		if (l.get_session().get_jbndatabase() == null || client.get_session().get_jbndatabase() == null)
    			continue;
    		//l.get_session().get_uid() != client.get_session().get_uid()
			
    		if (l.get_session().get_jbndatabase().get_dbname().equals(client.get_session().get_jbndatabase().get_dbname())
    				&& l.get_session().get_jbndatabase().get_dbname().length() != 0) {
    			l.send(data);
    		}
    	}
	}
	
	public static byte[] build(int uid, int status, String sender, String message) {
		buffer_out out = new buffer_out();
		
		out.insertDword(uid);
		out.insertDword(status);
		out.insertNTString(sender);
		out.insertNTString(message);
		return out.format(PACKET_MESSAGE);
	}

	public static packet_message get_instance() {
		if (instance == null)
			instance = new packet_message();
		return instance;
	}
}
