package packet;

import java.util.ArrayList;

import net.*;
import util.*;

public abstract class _packet extends _semaphore {
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
	
	private int id;
	private ArrayList<_argument> arguments;
	protected buffer_out out;
	
	protected _packet(int id) {
		super();
		set_id(id);
	}
	
	public void parse(listener client, buffer_in in) {
		_lock();
		for (int i = 0; i < arguments.size(); i++)
			arguments.get(i).retrieve(in);
		this.analyze(client);
		_unlock();
	}
	
	protected void analyze(listener client) {}
	
	protected void set_id(int id) {
		this.id = id;
	}
	
	public int get_id() {
		return id;
	}

	protected void add_argument(_argument argument) {
		this.arguments.add(argument);
	}
	
	protected void set_arguments(ArrayList<_argument> arguments) {
		this.arguments = arguments;
	}

	protected _argument get_argument(String name) {
		for (_argument argument : arguments)
			if (argument.get_name().equals(name))
				return argument;
		return null;
	}
	
	protected ArrayList<_argument> get_arguments() {
		return arguments;
	}
}
