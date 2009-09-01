package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import util.*;

public class server implements Runnable {

	public int guest_id = 0x40000000;
	public int user_id = 1;
	
	public int m_port = 21845;
	public int m_max_connections = 0;
	public ArrayList<listener> listeners;
	
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
	    	ServerSocket sck = new ServerSocket(m_port);
	    	Socket serv;

	    	keepalive = new Timer();
		    keepalive.scheduleAtFixedRate(new keepalive_task(), 120 * 1000, 120*1000);

	    	while((i++ < m_max_connections) || (m_max_connections == 0)){
	    		serv = sck.accept();
	            System.out.println(":: Incoming connection from " + serv.getRemoteSocketAddress().toString().replace("/", ""));
	    		listener conn_c = new listener(serv);
	    		listeners.add(conn_c);
	    		Thread t = new Thread(conn_c);
	    		t.start();
	    	}
	    } catch (IOException ioe) {
	    	System.out.println(":: IOException on socket listen: " + ioe);
	    	ioe.printStackTrace();
	   	}
	}
}
