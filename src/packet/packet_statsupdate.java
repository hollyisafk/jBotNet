package packet;

import java.io.IOException;
import java.util.ArrayList;

import authentication.login_procedure;

import net.listener;

import util.buffer_out;
import util.helper;
import core.jbotnet;
import core.settings;
import core.terminal;
import data.distributor;
import database.database;

public class packet_statsupdate extends _packet {
	private static packet_statsupdate instance = null;
	
	private String username;
	private String channel;
	private int ipaddr;
	private String database;
	private int cycle;
	
	private packet_statsupdate() {
		super(PACKET_STATSUPDATE);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_string("username"));
		args.add(new argument_string("channel"));
		args.add(new argument_dword("ipaddr"));
		args.add(new argument_string("database"));
		args.add(new argument_string("cycle"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		username = get_argument("username").get_string();
		channel = get_argument("channel").get_string();
		ipaddr = get_argument("ipaddr").get_int();
		database = get_argument("database").get_string();
		cycle = get_argument("cycle").get_int();
		
		/*if (!client.get_session().is_state(login_procedure.LOGONSTATE_LOGON_PASSED)) {
			if (settings.security_send_fail_response) {
				client.send(build(0x00));
			}
			try {
				client.get_socket().close();
			} catch (IOException e) {}
			terminal.print_session(client, "Received stats update with failed logon");
		} else {*/
		
    	byte octet[] = helper.dword_to_raw_ip(ipaddr);

		client.get_session().set_bnetusername(username);
		client.get_session().set_bnetchannel(channel);
		client.get_session().set_bnetserver(ipaddr);
		client.get_session().set_bnetserverip(helper.implode(octet, "."));
		client.get_session().set_jbncycle((cycle == 1 ? true : false));
		
		// attempts to be public
		if (database.length() == 0) {
			if (settings.security_allow_public_database) {
				// put into public database
        		client.get_session().set_state(login_procedure.LOGONSTATE_IDENTIFIED);
        		//client.session.jbndatabase = "public";
        		//client.session.jbnpassword = "";
				client.send(build(0x01));
        		terminal.print_session(client, "Database login is public");
			} else {
				// terminate
        		if (settings.security_send_fail_response) {
    				client.send(build(0x00));
        		}
        		try {
        			client.get_socket().close();
        		} catch (IOException e) {}
        		terminal.print_session(client, "Database login failed");
			}
		// attempts login
		} else {
        	String[] cred = database.split(" ");
        	String dbid;
        	String dbpw;
        	int flags = 0;
        	if (cred.length == 2) {
        		dbid = cred[0];
        		dbpw = cred[1];
        	} else {
        		dbid = "public";
        		dbpw = "";
        	}
        	
        	database db;
        	db = jbotnet.get_db().get_database(dbid);
        	if (db != null) {
            	if (db.get_read_password().equals(dbpw)) {
            		flags = 0x01;
            	} else if (db.get_write_password().equals(dbpw)) {
            		flags = 0x02;
            	} else if (db.get_admin_password().equals(dbpw)) {
            		flags = 0x04;
            	}
        	}
        	
        	if (db == null || flags == 0) {// terminate
        		if (settings.security_send_fail_response) {
    				client.send(build(0x00));
        		}
        		try {
        			client.get_socket().close();
        		} catch (IOException e) {}
        		terminal.print_session(client, "Database login failed");
        	} else {
        		// put into database
        		client.get_session().set_state(login_procedure.LOGONSTATE_IDENTIFIED);
        		client.get_session().set_jbndatabase(db);
        		client.get_session().set_jbnflags(flags);
        		if (client.get_session().get_uid() == -1) {
        			client.get_session().set_uid(jbotnet.get_svr().guest_id);
        			jbotnet.get_svr().guest_id++;
        		}
				client.send(build(0x01));
        		//terminal.print("Status update received");
        		
        		distributor.send_user_info(client.get_session());
        	}
		}
	}
	
	public static byte[] build(int response) {
		buffer_out out = new buffer_out();
		out.insertDword(response);
		return out.format(PACKET_STATSUPDATE);
	}

	public static packet_statsupdate get_instance() {
		if (instance == null)
			instance = new packet_statsupdate();
		return instance;
	}
}
