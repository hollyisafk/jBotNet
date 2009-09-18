package core;

import database.*;
import net.*;

public class jbotnet {
	public static config cfg;
	public static server svr;
	public static account_factory acnt;
	public static database_factory db;
	
	private static void start_server() {
		svr.m_port = Integer.parseInt(cfg.Read("net", "port"));
		svr.m_max_connections = Integer.parseInt(cfg.Read("net", "max_connections"));
        Thread svr_t = new Thread(svr);
        svr_t.start();
	}
	
	public static void main(String[] args) {
        System.out.println("      _ ______                               ");
        System.out.println("     (_|____  \\        _                 _   ");
        System.out.println("      _ ____)  ) ___ _| |_ ____  _____ _| |_ ");
        System.out.println("     | |  __  ( / _ (_   _)  _ \\| ___ (_   _)");
        System.out.println("     | | |__)  ) |_| || |_| | | | ____| | |_ ");
        System.out.println("    _| |______/ \\___/  \\__)_| |_|_____)  \\__)");
        System.out.println("   (__/\r\n");
        
        System.out.println(":: Welcome to jBotnet");
        System.out.println(":: Written by Richard Pianka");
        System.out.println(":: Version 0.1\r\n");
        
        cfg = new config();
        acnt = new account_factory();
        acnt.load();
        db = new database_factory();
        db.load();
        svr = new server();
		start_server();
	}
}
