package packet;

import java.util.ArrayList;
import net.listener;
import user.session;
import util.buffer_out;
import data.distributor;

public class packet_userinfo extends _packet {
	private static packet_userinfo instance = null;
	
	private packet_userinfo() {
		super(PACKET_USERINFO);
		ArrayList<_argument> args = new ArrayList<_argument>();
		set_arguments(args);
	}

	protected void analyze(listener client) {
		distributor.send_users(client);
		client.get_session().set_state(session.LOGONSTATE_HAS_USERLIST);
	}
	
	public static byte[] build(int response) {
		buffer_out out = new buffer_out();
		out.insertDword(response);
		return out.format(PACKET_USERINFO);
	}

	public static packet_userinfo get_instance() {
		if (instance == null)
			instance = new packet_userinfo();
		return instance;
	}
}
