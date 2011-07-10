package core;

public class settings {
	public static int net_port;
	public static int net_max_connections;
	public static boolean security_allow_public_database;
	public static boolean security_require_bot;
	public static boolean security_send_fail_response;
	
	public static void load() {
		net_port = Integer.valueOf(config.get_instance().Read("net", "port"));
		net_max_connections = Integer.valueOf(config.get_instance().Read("net", "max_connections"));
		
		security_allow_public_database = Boolean.valueOf(config.get_instance().Read("security", "allow_public_database"));
		security_require_bot = Boolean.valueOf(config.get_instance().Read("security", "require_bot"));
		security_send_fail_response = Boolean.valueOf(config.get_instance().Read("security", "send_fail_response"));
	}
}
