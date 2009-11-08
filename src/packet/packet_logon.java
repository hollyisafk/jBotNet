package packet;

import java.io.IOException;
import java.util.ArrayList;

import net.listener;

import user.session;
import util.buffer_out;
import util.helper;
import core.config;
import core.settings;
import core.terminal;

public class packet_logon extends _packet {
	private static packet_logon instance = null;
	
	private String botid;
	private String botpass;
	
	private packet_logon() {
		super(PACKET_LOGON);
		ArrayList<_argument> args = new ArrayList<_argument>();
		args.add(new argument_string("botid"));
		args.add(new argument_string("botpassword"));
		set_arguments(args);
	}

	protected void analyze(listener client) {
		botid = get_argument("botid").get_string();
		botpass = get_argument("botpassword").get_string();
		
		byte[] addr_raw = client.get_socket().getInetAddress().getAddress();
		int addr = helper.raw_ip_to_dword(addr_raw);
		
		if (!settings.security_require_bot || (config.get_instance().Read("bot", botid).equals(botpass) && botpass.length() != 0)) {
			client.get_session().set_botid(botid);
			client.get_session().set_botpass(botpass);
			client.get_session().set_state(session.LOGONSTATE_LOGON_PASSED);

			client.send(build(0x01, addr));
			terminal.print_session(client, "Bot login passed");
		} else {
			if (settings.security_send_fail_response) {
				client.send(build(0x00, addr));
			}
			try {
				client.get_socket().close();
			} catch (IOException e) {}
			terminal.print("Bot login failed");
		}
	}
	
	public static byte[] build(int response, int address) {
		buffer_out out = new buffer_out();
		out.insertDword(response);
		out.insertDword(address);
		return out.format(PACKET_LOGON);
	}

	public static packet_logon get_instance() {
		if (instance == null)
			instance = new packet_logon();
		return instance;
	}
}
