package authentication;

import user.*;

public class login_procedure implements _authenticator {
	public static final int PACKET_IDLE   					= 0x00;
	public static final int PACKET_LOGON					= 0x01;
	public static final int PACKET_STATSUPDATE 				= 0x02;
	public static final int PACKET_DATABASE  				= 0x03;
	public static final int PACKET_MESSAGE  				= 0x04;
	public static final int PACKET_CYCLE  					= 0x05;
	public static final int PACKET_USERINFO  				= 0x06;
	public static final int PACKET_USERLOGGINGOFF  			= 0x07;
	public static final int PACKET_BROADCASTMESSAGE  		= 0x07;
	public static final int PACKET_COMMAND  				= 0x08;
	public static final int PACKET_CHANGEDBPASSWORD  		= 0x09;
	public static final int PACKET_BOTNETVERSIONACK			= 0x09;
	public static final int PACKET_BOTNETVERSION  			= 0x0A;
	public static final int PACKET_BOTNETCHAT  				= 0x0B;
	public static final int PACKET_ACCOUNT  				= 0x0D;
	public static final int PACKET_CHATDROPOPTIONS  		= 0x10;
	
	public static final int LOGONSTATE_LOGON_PASSED			= 0x01;
	public static final int LOGONSTATE_IDENTIFIED			= 0x02;
	public static final int LOGONSTATE_HAS_USERLIST			= 0x04;
	public static final int LOGONSTATE_LOGGED_IN			= 0x08;
	
	public login_procedure() {
		//sentry.get_instance().register(this);
	}
	
	public boolean can_do(session client, _action action) {
		if (action.get_type() == _action.action_type.SEND_PACKET) {
			return _can_send_packet(client, (action_send_packet)action);
		}
		return true;
	}
	
	private boolean _can_send_packet(session client, action_send_packet action) {
		switch (action.get_packet_id()) {
		case PACKET_IDLE:
			return true;
		case PACKET_LOGON:
			return true;
		case PACKET_STATSUPDATE:
			return client.is_state(LOGONSTATE_LOGON_PASSED);
		case PACKET_DATABASE:
			return client.is_state(LOGONSTATE_IDENTIFIED);
		case PACKET_MESSAGE:
			return client.is_state(LOGONSTATE_LOGON_PASSED);
		case PACKET_CYCLE:
			return client.is_state(LOGONSTATE_IDENTIFIED);
		case PACKET_USERINFO:
			return client.is_state(LOGONSTATE_IDENTIFIED);
		case PACKET_BROADCASTMESSAGE:
			return client.is_state(LOGONSTATE_LOGGED_IN);
		case PACKET_COMMAND:
			return client.is_state(LOGONSTATE_IDENTIFIED);
		case PACKET_CHANGEDBPASSWORD:
			return client.is_state(LOGONSTATE_IDENTIFIED);
		case PACKET_BOTNETVERSION:
			return true;
		case PACKET_BOTNETCHAT:
			return client.is_state(LOGONSTATE_LOGON_PASSED);
		case PACKET_ACCOUNT:
			return client.is_state(LOGONSTATE_LOGON_PASSED);
		case PACKET_CHATDROPOPTIONS:
			return client.is_state(LOGONSTATE_LOGON_PASSED);
		}
		return false;
	}
	
	/* singleton */
	private static login_procedure instance = new login_procedure();
	
	public static login_procedure get_instance() {
		// no lazy loading
		return instance;
	}
	/* singleton */
}
