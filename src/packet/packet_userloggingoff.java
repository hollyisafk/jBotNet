package packet;

import java.util.ArrayList;

import net.listener;
import util.buffer_out;

public class packet_userloggingoff extends _packet {
	private static packet_userloggingoff instance = null;
	
	private packet_userloggingoff() {
		super(PACKET_USERLOGGINGOFF);
		ArrayList<_argument> args = new ArrayList<_argument>();
		set_arguments(args);
	}

	protected void analyze(listener client) {
		
	}
	
	public static byte[] build(int uid) {
		buffer_out out = new buffer_out();
		out.insertDword(uid);
		out.insertDword(0x07);
		return out.format(PACKET_USERLOGGINGOFF);
	}

	public static packet_userloggingoff get_instance() {
		if (instance == null)
			instance = new packet_userloggingoff();
		return instance;
	}
}