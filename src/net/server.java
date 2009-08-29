package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server implements Runnable {

	public int m_port = 21845;
	public int m_max_connections = 0;
	public ArrayList<listener> listeners;
	
	public void run () {
	    int i=0;

	    try{
	    	listeners = new ArrayList<listener>();
	    	ServerSocket sck = new ServerSocket(m_port);
	    	Socket serv;

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
