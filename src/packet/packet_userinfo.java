package packet;

import java.util.ArrayList;

import authentication.login_procedure;
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
		client.get_session().set_state(login_procedure.LOGONSTATE_HAS_USERLIST);
	}
	
	public static byte[] build(session s) {
		buffer_out out = new buffer_out();
		out.insertDword(s.get_uid());
		out.insertDword(s.get_jbnflags());
		out.insertDword(0x00);
		out.insertNTString(s.get_bnetusername());
		out.insertNTString(s.get_bnetchannel());
		out.insertDword(s.get_bnetserver());
		out.insertNTString(s.get_jbnaccount() == null ? "" : s.get_jbnaccount().get_username());
		out.insertNTString(s.get_jbndatabase() == null ? "" : s.get_jbndatabase().get_dbname());
		return out.format(PACKET_USERINFO);
	}

	public static packet_userinfo get_instance() {
		if (instance == null)
			instance = new packet_userinfo();
		return instance;
	}
}
