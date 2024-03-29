package data;

import java.util.ArrayList;

import authentication.*;
import packet.*;
import net.listener;
import util.*;

public class parser {
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
	
	@SuppressWarnings("unused")
	private static boolean is_setup = setup();
	private static ArrayList<_packet> packets;
	
	private parser() {}
	
	private static boolean setup() {
		packets = new ArrayList<_packet>();
		register(packet_account.get_instance());
		register(packet_botnetchat.get_instance());
		register(packet_botnetversion.get_instance());
		register(packet_changedbpassword.get_instance());
		register(packet_command.get_instance());
		register(packet_database.get_instance());
		register(packet_logon.get_instance());
		register(packet_message.get_instance());
		register(packet_statsupdate.get_instance());
		register(packet_userinfo.get_instance());
		return true;
	}
	
	public static void register(_packet packet) {
		packets.add(packet);
	}
	
	public static void parse(listener client, int id, byte[] data) {
        buffer_in in = new buffer_in(data);
        
        action_send_packet action = new action_send_packet(id, in);
        if (!sentry.get_instance().can_do(client.get_session(), action)) {
        	try {
        		client.get_socket().close();
        	} catch (Exception e) {}
        	return;
        }
        
        _packet on;

        for (int i = 0; i < packets.size(); i++) {
        	on = packets.get(i);
        	if (on.get_id() == id) {
        		on.parse(client, in);
        	}
        }
	}
}
