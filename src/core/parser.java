package core;

import java.io.IOException;

import net.listener;
import util.*;
import user.*;

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
	public static final int PACKET_BOTNETVERSION  		= 0x0A;
	public static final int PACKET_BOTNETCHAT  			= 0x0B;
	public static final int PACKET_ACCOUNT  			= 0x0D;
	public static final int PACKET_CHATDROPOPTIONS  	= 0x10;
	
	public static String implode(byte[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}
	
	public static void parse(listener client, int id, byte[] data) {
        buffer_in in = new buffer_in(data);
        buffer_out out = new buffer_out();
        
        System.out.println(in.debugOutput());
        
	    switch (id) {
        case PACKET_IDLE:
        	break;
        case PACKET_LOGON:
        	String botid = in.getNTString();
        	String botpass = in.getNTString();
        	
    		byte[] addr_raw = client.server.getInetAddress().getAddress();
    		int addr = (((int)addr_raw[0] << 0) & 0x000000FF) |
					   (((int)addr_raw[1] << 8) & 0x0000FF00) |
					   (((int)addr_raw[2] << 16) & 0x00FF0000) |
					   (((int)addr_raw[3] << 24) & 0xFF000000);
    		
        	if (!Boolean.parseBoolean(jbotnet.cfg.Read("security", "require_bot")) ||
        			(jbotnet.cfg.Read("bot", botid).equals(botpass) && botpass.length() != 0)) {
        		client.session.botid = botid;
        		client.session.botpass = botpass;
        		client.session.set_state(session.LOGONSTATE_LOGON_PASSED);
        		
        		out.insertByte(0x01);
        		out.insertDword(addr);
        		client.send(out.format(PACKET_LOGON));
        		System.out.println(":: Bot login passed");
        	} else {
        		if (Boolean.parseBoolean(jbotnet.cfg.Read("security", "send_fail_response"))) {
        			out.insertByte(0x00);
        			out.insertDword(addr);
        			System.out.println(out.debugOutput());
        			client.send(out.format(PACKET_LOGON));
        		}
        		try {
        			client.server.close();
        		} catch (IOException e) {}
        		System.out.println(":: Bot login failed");
        	}
        	break;
        case PACKET_STATSUPDATE:
        	if (!client.session.is_state(session.LOGONSTATE_LOGON_PASSED)) {
        		if (Boolean.parseBoolean(jbotnet.cfg.Read("security", "send_fail_response"))) {
        			out.insertByte(0x00);
        			client.send(out.format(PACKET_STATSUPDATE));
        		}
        		try {
        			client.server.close();
        		} catch (IOException e) {}
        		System.out.println(":: Received stats update with failed logon");
        	} else {
        		String username = in.getNTString();
        		String channel = in.getNTString();
        		int ipaddr = in.getDword();
        		String database = in.getNTString();
        		int cycle = in.getDword(); // defunct, ignore
        		
            	byte octet[] = new byte[4];
            	octet[0] = (byte)((ipaddr & 0x000000FF) >> 0);
            	octet[1] = (byte)((ipaddr & 0x0000FF00) >> 8);
            	octet[2] = (byte)((ipaddr & 0x00FF0000) >> 16);
            	octet[3] = (byte)((ipaddr & 0xFF000000) >> 24);

        		client.session.bnetusername = username;
        		client.session.bnetchannel = channel;
        		client.session.bnetserverip = implode(octet, ".");
        		client.session.jbncycle = (cycle == 1 ? true : false);
        		
        		// attempts to be public
        		if (database.length() == 0) {
        			if (Boolean.parseBoolean(jbotnet.cfg.Read("security", "allow_public_database"))) {
        				// put into public database	       
	            		client.session.set_state(session.LOGONSTATE_IDENTIFIED);     	
    	        		client.session.jbndatabase = "public";
    	        		client.session.jbnpassword = "";
            			out.insertByte(0x01);
            			client.send(out.format(PACKET_STATSUPDATE));
                		System.out.println(":: Database login is public");
        			} else {
        				// terminate
                		if (Boolean.parseBoolean(jbotnet.cfg.Read("security", "send_fail_response"))) {
                			out.insertByte(0x00);
                			client.send(out.format(PACKET_STATSUPDATE));
                		}
                		try {
                			client.server.close();
                		} catch (IOException e) {}
                		System.out.println(":: Database login failed");
        			}
        		// attempts login
        		} else {
	            	String[] cred = database.split(" ");
	            	String dbid = cred[0];
	            	String dbpw = cred[1];
	            	
	        		client.session.jbndatabase = dbid;
	        		client.session.jbnpassword = dbpw;
	        		
	            	if (jbotnet.cfg.Read("database", dbid).equals(dbpw) && dbpw.length() != 0) {
	            		// put into database
	            		client.session.set_state(session.LOGONSTATE_IDENTIFIED);
            			out.insertByte(0x01);
            			client.send(out.format(PACKET_STATSUPDATE));
                		System.out.println(":: Database login passed");
	            	} else {
	            		// terminate
                		if (Boolean.parseBoolean(jbotnet.cfg.Read("security", "send_fail_response"))) {
                			out.insertByte(0x00);
                			client.send(out.format(PACKET_STATSUPDATE));
                		}
                		try {
                			client.server.close();
                		} catch (IOException e) {}
                		System.out.println(":: Database login failed");
	            	}
        		}
        	}
        	break;
        case PACKET_DATABASE:
        	break;
        case PACKET_MESSAGE:
        	break;
        case PACKET_CYCLE:
        	break;
        case PACKET_USERINFO:
        	break;
        case PACKET_USERLOGGINGOFF:
        	break;
        case PACKET_COMMAND:
        	break;
        case PACKET_CHANGEDBPASSWORD:
        	break;
        case PACKET_BOTNETVERSION:
        	break;
        case PACKET_BOTNETCHAT:
        	break;
        case PACKET_ACCOUNT:
        	break;
        case PACKET_CHATDROPOPTIONS:
        	break;
        }
	}
}
