package core;

import database.*;
import plugin.*;
import user.account_factory;
import net.*;

public class jbotnet {
	private static void start_server() {
		get_svr().set_m_port(settings.net_port);
		get_svr().set_m_max_connections(settings.net_max_connections);
        Thread svr_t = new Thread(get_svr());
        svr_t.start();
	}
	
	public static void main(String[] args) {    
		settings.load();
		terminal.print_logo();
		terminal.print_version();
        terminal.print_stats();
        
        //get_db().create_database("null", "qp09b198do3kd38a7e", "am947fckzbe74hjtd0", "0vyyu2kxhy378ibvr1");
        
		start_server();
	}

	public static server get_svr() {
		return server.get_instance();
	}

	public static account_factory get_acnt() {
		return account_factory.get_instance();
	}

	public static database_factory get_db() {
		return database_factory.get_instance();
	}

	public static plugin_manager get_plug() {
		return plugin_manager.get_instance();
	}
}
