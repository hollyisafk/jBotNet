package core;

import net.listener;

public class terminal {
	public static void print(String line) {
		System.out.println(":: " + line);
	}
	
	public static void print_logo() {
        System.out.println("      _ ______                               ");
        System.out.println("     (_|____  \\        _                 _   ");
        System.out.println("      _ ____)  ) ___ _| |_ ____  _____ _| |_ ");
        System.out.println("     | |  __  ( / _ (_   _)  _ \\| ___ (_   _)");
        System.out.println("     | | |__)  ) |_| || |_| | | | ____| | |_ ");
        System.out.println("    _| |______/ \\___/  \\__)_| |_|_____)  \\__)");
        System.out.println("   (__/\r\n");
	}
	
	public static void print_version() {
        print(" Welcome to jBotnet");
        print(" Written by Richard Pianka");
        print(" Version 0.1\r\n");
	}
	
	public static void print_stats() {
        int acnt_count = jbotnet.get_acnt().get_collection().size();
        int db_count = jbotnet.get_db().get_collection().size();
        int plugin_count = jbotnet.get_plug().plugin_count();
        
        print("Loaded " + acnt_count + " account" + (acnt_count != 1 ? "s" : ""));
        print("Loaded " + db_count + " database" + (db_count != 1 ? "s" : ""));
        print("Loaded " + plugin_count + " plugin" + (plugin_count != 1 ? "s" : "") + "\r\n");
	}
	
	public static void print_session(listener client, String line) {
		String ip = client.get_socket().getRemoteSocketAddress().toString().replace("/", "");
		String[] parts = ip.split(":");
		String addr = parts[0];
		String pad = "";
		for (int i = 0; i < 15 - addr.length(); i++)
			pad += " ";
		//int port = Integer.valueOf(parts[1]);
		
		print("[" + addr + "] " + pad + line);
	}
}
