package data;

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
		if (s.get_uid() < 1)
			return;
		
		buffer_out out = new buffer_out();
		out.insertDword(s.get_uid());
		out.insertDword(s.get_jbnflags());
		out.insertDword(0x00);
		out.insertNTString(s.get_bnetusername());
		out.insertNTString(s.get_bnetchannel());
		out.insertDword(s.get_bnetserver());
		out.insertNTString(s.get_jbnaccount() == null ? "" : s.get_jbnaccount().get_username());
		out.insertNTString(s.get_jbndatabase() == null ? "" : s.get_jbndatabase().get_dbname());
    	send_all(out.format(PACKET_USERINFO), s.get_uid(), session.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_user_logon(session s) {
		if (s.get_uid() < 1)
			return;
		
		buffer_out out = new buffer_out();
		out.insertDword(s.get_uid());
		out.insertDword(s.get_jbnflags());
		out.insertDword(0x00);
		out.insertNTString(s.get_bnetusername());
		out.insertNTString(s.get_bnetchannel());
		out.insertDword(s.get_bnetserver());
		out.insertNTString(s.get_jbnaccount() == null ? "" : s.get_jbnaccount().get_username());
		out.insertNTString(s.get_jbndatabase() == null ? "" : s.get_jbndatabase().get_dbname());
    	send_all(out.format(PACKET_USERINFO), s.get_uid(), session.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_user_logoff(int uid) {
		if (uid < 1)
			return;
		
		buffer_out out = new buffer_out();
		out.insertDword(uid);
		out.insertDword(0x07);
		send_all(out.format(PACKET_USERLOGGINGOFF), 0, session.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_users(listener client) {
		buffer_out out = new buffer_out();
		out.clear();
		out.insertDword(client.get_session().get_uid());
		out.insertDword(client.get_session().get_jbnflags());
		out.insertDword(0x00);
		out.insertNTString(client.get_session().get_bnetusername());
		out.insertNTString(client.get_session().get_bnetchannel());
		out.insertDword(client.get_session().get_bnetserver());
		out.insertNTString(client.get_session().get_jbnaccount() == null ? "" :
							client.get_session().get_jbnaccount().get_username());
		out.insertNTString(client.get_session().get_jbndatabase() == null ? "" : 
							client.get_session().get_jbndatabase().get_dbname());
    	client.send(out.format(PACKET_USERINFO));
    	
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s = l.get_session();
    		if (s.get_uid() > 0 && s.get_uid() != client.get_session().get_uid() &&
    				client.get_session().is_state(session.LOGONSTATE_IDENTIFIED)) {
    			
	    		out.clear();
	    		out.insertDword(s.get_uid());
	    		out.insertDword(s.get_jbnflags());
	    		out.insertDword(0x00);
	    		out.insertNTString(s.get_bnetusername());
	    		out.insertNTString(s.get_bnetchannel());
	    		out.insertDword(s.get_bnetserver());
	    		out.insertNTString(s.get_jbnaccount() == null ? "" : s.get_jbnaccount().get_username());
	    		out.insertNTString(s.get_jbndatabase() == null ? "" : s.get_jbndatabase().get_dbname());
	        	client.send(out.format(PACKET_USERINFO));
    		}
    	}

		out.clear();
    	client.send(out.format(PACKET_USERINFO));
	}
	
	public static void send_database(listener client, int age) {
		long time = System.currentTimeMillis() / 1000;
		buffer_out out = new buffer_out();
		
		out.insertDword(0x01);
		if (age == 0) {
			out.insertDword(0x00);
		} else {
			out.insertDword(0x01);
		}
		client.send(out.format(PACKET_DATABASE));
		
		for (database_entry entry : client.get_session().get_jbndatabase().get_entries().get_collection()) {
			if (age == 0 || time - entry.get_last_edit_time() < age) {
				out.clear();
				out.insertDword(0x02);
				out.insertDword(0x00);
				out.insertDword(entry.get_last_edit_time());
				out.insertNTString(entry.get_username());
				out.insertNTString(entry.get_flags());
				out.insertNTString("");
				client.send(out.format(PACKET_DATABASE));
			}
		}
		
		out.clear();
		out.insertDword(0x01);
		out.insertDword(0x02);
		client.send(out.format(PACKET_DATABASE));
	}
	
	public static void update_usermask(session client, database db, database_entry dbe, String comment) {
		buffer_out out = new buffer_out();
		out.insertDword(0x02);
		out.insertDword(client.get_uid());
		out.insertDword(dbe.get_last_edit_time());
		out.insertNTString(dbe.get_username());
		out.insertNTString(dbe.get_flags());
		out.insertNTString(comment);
		send_all(out.format(PACKET_DATABASE), 0, session.LOGONSTATE_IDENTIFIED);
	}
	
	public static void delete_usermask(session client, database db, database_entry dbe, String comment) {
		buffer_out out = new buffer_out();
			
		out.clear();
		out.insertDword(0x03);
		out.insertDword(client.get_uid());
		out.insertDword(dbe.get_last_edit_time());
		out.insertNTString(dbe.get_username());
		out.insertNTString(comment);
		send_all(out.format(PACKET_DATABASE), 0, session.LOGONSTATE_IDENTIFIED);
	}
	
	public static void send_database_notify(listener client, int command, String user) {

	}
}
