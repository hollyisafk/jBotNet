package packet;

import java.util.ArrayList;

import net.listener;
import util.buffer_out;

public class packet_botnetversion extends _packet {
	private static packet_botnetversion instance = null;
	
	private int version;
	
	private packet_botnetversion() {
		super(PACKET_BOTNETVERSION);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_dword("version"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		version = get_argument("version").get_int();
		client.send(build(version));
	}
	
	public static byte[] build(int version) {
		buffer_out out = new buffer_out();
		out.insertDword(version);
		return out.format(PACKET_BOTNETVERSIONACK);
	}

	public static packet_botnetversion get_instance() {
		if (instance == null)
			instance = new packet_botnetversion();
		return instance;
	}
}
