package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import core.terminal;
import util.*;

public class server implements Runnable {
	public int guest_id = 0x40000000;
	public int user_id = 0xA;
	
	private int m_port = 21845;
	private int m_max_connections = 0;
	public ArrayList<listener> listeners;
	
	private static server instance = new server();
	
	public static server get_instance() {
		return instance;
	}
	
	private server() {
		
	}
	
	Timer keepalive;
	class keepalive_task extends TimerTask {
		buffer_out out = new buffer_out();
		
	    public void run() {
	    	for (listener l : listeners) {
	    		l.send(out.format(0x00));
	    	}
	    }
	}
	
	public void run () {
	    int i=0;

	    try {
	    	listeners = new ArrayList<listener>();
	    	ServerSocket sck = new ServerSocket(get_m_port());
	    	Socket serv;

	    	keepalive = new Timer();
		    keepalive.scheduleAtFixedRate(new keepalive_task(), 120 * 1000, 120*1000);

	    	while((i++ < get_m_max_connections()) || (get_m_max_connections() == 0)){
	    		serv = sck.accept();
	    		listener conn_c = new listener(serv);
	    		listeners.add(conn_c);
	            terminal.print_session(conn_c, "Incoming connection");
	    		Thread t = new Thread(conn_c);
	    		t.start();
	    	}
	    } catch (IOException ioe) {
	    	terminal.print("IOException on socket listen: " + ioe);
	    	ioe.printStackTrace();
	   	}
	}

	public void set_m_port(int m_port) {
		this.m_port = m_port;
	}

	public int get_m_port() {
		return m_port;
	}

	public void set_m_max_connections(int m_max_connections) {
		this.m_max_connections = m_max_connections;
	}

	public int get_m_max_connections() {
		return m_max_connections;
	}
}
