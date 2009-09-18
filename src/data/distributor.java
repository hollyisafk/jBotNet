package data;

import net.listener;
import user.session;
import util.buffer_out;
import core.jbotnet;

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
    	for (listener l : jbotnet.svr.listeners) {
    		session s = l.session;
    		if (s.uid == skip || !s.is_state(state))
    			continue;
        	l.send(data);
    	}
	}
	
	public static void send_user_info(session s) {
		if (s.uid < 1)
			return;
		
		buffer_out out = new buffer_out();
		out.insertDword(s.uid);
		out.insertDword(0x01);
		out.insertDword(0x00);
		out.insertNTString(s.bnetusername);
		out.insertNTString(s.bnetchannel);
		out.insertDword(s.bnetserver);
		out.insertNTString(s.jbnaccount.username);
		out.insertNTString(s.jbndatabase);
    	send_all(out.format(PACKET_USERINFO), s.uid, session.LOGONSTATE_HAS_USERLIST);
	}
	
	public static void send_user_logon(session s) {
		if (s.uid < 1)
			return;
		
		buffer_out out = new buffer_out();
		out.insertDword(s.uid);
		out.insertDword(0x01);
		out.insertDword(0x00);
		out.insertNTString(s.bnetusername);
		out.insertNTString(s.bnetchannel);
		out.insertDword(s.bnetserver);
		out.insertNTString(s.jbnaccount.username);
		out.insertNTString(s.jbndatabase);
    	send_all(out.format(PACKET_USERINFO), s.uid, session.LOGONSTATE_HAS_USERLIST);
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
		out.insertDword(client.session.uid);
		out.insertDword(0x01);
		out.insertDword(0x00);
		out.insertNTString(client.session.bnetusername);
		out.insertNTString(client.session.bnetchannel);
		out.insertDword(client.session.bnetserver);
		out.insertNTString(client.session.jbnaccount.username);
		out.insertNTString(client.session.jbndatabase);
    	client.send(out.format(PACKET_USERINFO));
    	
    	for (listener l : jbotnet.svr.listeners) {
    		session s = l.session;
    		if (s.uid > 0 && s.uid != client.session.uid &&
    				client.session.is_state(session.LOGONSTATE_IDENTIFIED)) {
    			
	    		out.clear();
	    		out.insertDword(s.uid);
	    		out.insertDword(0x01);
	    		out.insertDword(0x00);
	    		out.insertNTString(s.bnetusername);
	    		out.insertNTString(s.bnetchannel);
	    		out.insertDword(s.bnetserver);
	    		out.insertNTString(s.jbnaccount.username);
	    		out.insertNTString(s.jbndatabase);
	        	client.send(out.format(PACKET_USERINFO));
    		}
    	}

		out.clear();
    	client.send(out.format(PACKET_USERINFO));
	}
}
