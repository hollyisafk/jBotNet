package packet;

import java.util.ArrayList;

import authentication.login_procedure;
import net.listener;
import util.buffer_out;

import core.*;

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
		
		/*if (!client.get_session().is_state(login_procedure.LOGONSTATE_IDENTIFIED)) {
    		return;
    	}*/
    	
    	byte[] data = build(command, action, client.get_session().get_uid(), message);
    	
    	switch (command) {
    	case 0x01:
        	for (listener l : jbotnet.get_svr().listeners) {
        		if (l.get_session().get_jbndatabase() == null || client.get_session().get_jbndatabase() == null)
        			continue;
        		if (l.get_session().get_uid() != client.get_session().get_uid()
        				&& l.get_session().get_jbndatabase().get_dbname().equals(client.get_session().get_jbndatabase().get_dbname())
        				&& l.get_session().get_jbndatabase().get_dbname().length() != 0) {
        			l.send(data);
        		}
        	}
    		break;
    	case 0x02:
        	for (listener l : jbotnet.get_svr().listeners) {
        		if (l.get_session().get_uid() == clientid)
        			l.send(data);
        	}
    		break;
    	}
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
