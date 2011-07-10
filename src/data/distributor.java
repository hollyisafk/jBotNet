package data;

import packet.packet_database;
import packet.packet_userinfo;
import packet.packet_userloggingoff;
import authentication.login_procedure;
import net.listener;
import user.session;
import util.buffer_out;
import core.jbotnet;
import database.database;
import database.database_entry;

public class distributor {
	public static final int PACKET_IDLE   				= 0x00;
	public static final int PACKET_LOGON				= 0x01;
	public static final int PACKET_STATSUPDATE 			= 0x02;
	public static final int PACKET_DATABASE  			= 0x03;
	public static final int PACKET_MESSAGE  			= 0x04;
	public static final int PACKET_CYCLE  				= 0x05;
	public static final int PACKET_USERINFO  			= 0x06;
	public static final int PACKET_USERLOGGINGOFF  		= 0x07;
	public static final int PACKET_BROADCASTMESSAGE  	= 0x07;
	public static final int PACKET_COMMAND  			= 0x08;
	public static final int PACKET_CHANGEDBPASSWORD  	= 0x09;
	public static final int PACKET_BOTNETVERSIONACK		= 0x09;
	public static final int PACKET_BOTNETVERSION  		= 0x0A;
	public static final int PACKET_BOTNETCHAT  			= 0x0B;
	public static final int PACKET_ACCOUNT  			= 0x0D;
	public static final int PACKET_CHATDROPOPTIONS  	= 0x10;
	
	/*public static void send_to(Iterable<listener> to, byte[] data) {
		send_to(to, data, 0, 0);
	}

	public static void send_to(Iterable<listener> to, byte[] data, int skip) {
		send_to(to, data, skip, 0);
	}
	
	public static void send_to(Iterable<listener> to, byte[] data, int skip, int state) {
		for (listener l : to) {
    		session s = l.session;
    		if (s.uid == skip || !s.is_state(state))
    			continue;
        	l.send(data);
    	}
	}*/
	
	public static void send_all(byte[] data) {
		send_all(data, 0, 0);
	}
	
	public static void send_all(byte[] data, int skip) {
		send_all(data, skip, 0);
	}
	
	public static void send_all(byte[] data, int skip, int state) {
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s = l.get_session();
    		if (s.get_uid() == skip || !s.is_state(state))
    			continue;
        	l.send(data);
    	}
	}
	
	public static void send_to(byte[] data, database db) {
		send_to(data, 0, 0, db);
	}
	
	public static void send_to(byte[] data, int skip, database db) {
		send_to(data, skip, 0, db);
	}
	
	public static void send_to(byte[] data, int skip, int state, database db) {
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s = l.get_session();
    		if (s.get_uid() == skip || !s.is_state(state) || s.get_jbndatabase().get_dbname().equals(db.get_dbname()))
    			continue;
        	l.send(data);
    	}
	}
	
	public static void send_user_info(session s) {
		if (s == null || s.get_uid() < 1)
			return;
		
		byte[] data = packet_userinfo.build(s);
		send_all(data, s.get_uid(), login_procedure.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_user_logon(session s) {
		if (s == null || s.get_uid() < 1)
			return;
		
		byte[] data = packet_userinfo.build(s);
		send_all(data, s.get_uid(), login_procedure.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_user_logoff(int uid) {
		if (uid < 1)
			return;
		
		byte[] data = packet_userloggingoff.build(uid);
		send_all(data, 0, login_procedure.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_users(listener client) {
		byte[] data = packet_userinfo.build(client.get_session());
		client.send(data);
    	
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s = l.get_session();
    		if (s.get_uid() > 0 && s.get_uid() != client.get_session().get_uid() &&
    				client.get_session().is_state(login_procedure.LOGONSTATE_IDENTIFIED)) {
    			
    			data = packet_userinfo.build(s);
    			client.send(data);
    		}
    	}

    	buffer_out out = new buffer_out();
		out.clear();
    	client.send(out.format(PACKET_USERINFO));
	}
	
	public static void send_database(listener client, int age) {
		byte[] data;
		long time = System.currentTimeMillis() / 1000;
		
		if (age == 0)
			data = packet_database.build_1(0x00);
		else
			data = packet_database.build_1(0x01);
		client.send(data);
		
		for (database_entry entry : client.get_session().get_jbndatabase().get_entries().get_collection()) {
			if (age == 0 || time - entry.get_last_edit_time() < age) {
				data = packet_database.build_2(entry, 0);
				client.send(data);
			}
		}
		
		data = packet_database.build_1(0x02);
		client.send(data);
	}
	
	public static void update_usermask(session client, database db, database_entry dbe, String comment) {
		byte[] data = packet_database.build_2(dbe, client.get_uid());
		send_to(data, 0, login_procedure.LOGONSTATE_IDENTIFIED, db);
	}
	
	public static void delete_usermask(session client, database db, database_entry dbe, String comment) {
		byte[] data = packet_database.build_3(dbe, client.get_uid(), comment);
		send_to(data, 0, login_procedure.LOGONSTATE_IDENTIFIED, db);
	}
	
	public static void send_database_notify(listener client, int command, String user) {

	}
}
