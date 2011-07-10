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
	
	private static void start_shell() {
		shell.start_shell();
	}
	
	public static void main(String[] args) {    
		settings.load();
		terminal.print_logo();
		terminal.print_version();
        terminal.print_stats();

        //get_db().create_database("null", "qp09b198do3kd38a7e", "am947fckzbe74hjtd0", "0vyyu2kxhy378ibvr1");
        //get_db().create_database("VPS", "n38sk2ifuz91m3kd", "pal38fk391jw8qk7zff", "1k4jx9swk3ma0x2l3md0s");
        //get_db().create_database("Falcon", "n38sk2ifuz91m3kd", "pal38fk391jw8qk7zff", "1k4jx9swk3ma0x2l3md0s");
        //get_db().create_database("BinaryFlux", "n38sk2ifuz91m3kd", "pal38fk391jw8qk7zff", "1k4jx9swk3ma0x2l3md0s");
        //get_db().create_database("StonerBot", "1kz04kd7avbv8ckle", "7ekdmaj93x9oem7v1", "qmxo5d3g2xxs09nhw");
        //get_db().create_database("Sandbox", "read", "write", "admin");
        
        if (args.length == 0)
        	start_server();
        
        
        else if (args[0].equals("-shell"))
        	start_shell();
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
