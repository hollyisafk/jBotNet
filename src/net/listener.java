package net;

import java.io.*;
import java.net.*;

import core.*;
import data.*;
import user.session;
import util.*;

public class listener implements Runnable {
    private Socket server;
    private session session;

    public listener(Socket server) {
    	this.set_socket(server);
    	set_session(new session());
      
    	buffer_out out = new buffer_out();
    	out.insertDword(0x04);
    	send(out.format(0x0A));
    }
    
    public void send(byte[] data) {
    	if (!get_socket().isConnected())
    		return;
    	
    	try {
    		get_socket().getOutputStream().write(data);
    		get_socket().getOutputStream().flush();
    	} catch (IOException e) {
    		//terminal.print("IOException [listener.send]");
    	}
    }

	public void run () {
        try {
			DataInputStream in = new DataInputStream (get_socket().getInputStream());
			//PrintStream out = new PrintStream(server.getOutputStream());
			
			byte id = 0;
			short length = 0;
			
			buffer_out recv = new buffer_out();
			buffer_out raw = new buffer_out();
			while (true)
			{
				if (get_socket().isClosed() == true) throw new IOException(":: Connection terminated!");
            
				raw.clear();
				int b = in.read();
        	  	raw.insertByte((byte)b);
                    
        	  	if (b == -1) throw new IOException(":: Connection terminated!");
        	  	if (b != 0x01) throw new IOException(":: Connection terminated!");

        	  	id = (byte)in.read();
        	  	raw.insertByte((byte)id);

        	  	b = in.read();
        	  	if (b == -1) throw new IOException(":: Connection terminated!");
        	  	length = (short) ((b << 0) & 0x000000FF);
        	  	raw.insertByte((byte)b);
            
        	  	b = in.read();
        	  	if (b == -1) throw new IOException(":: Connection terminated!");
        	  	length |= (short) ((b << 8) & 0x0000FF00);
        	  	raw.insertByte((byte)b);

        	  	for (int i = 0; i < (length - 4); i++)
        	  	{
        		  b = in.read();
        		  if (b == -1) throw new IOException(":: Connection terminated.");
        		  recv.insertByte((byte) b);
        		  raw.insertByte((byte) b);
        	  	}
            
        	  	parser.parse(this, id, recv.getBuffer());
        	  	recv.clear();
          	}
        } catch (IOException ioe) {
          	close();
        	jbotnet.get_svr().listeners.remove(this);
			distributor.send_user_logoff(get_session().get_uid());
			terminal.print_session(this, "Connection terminated");
        }
	}
	
	public void close() {
      	try { get_socket().close(); }
      	catch (IOException e) {}
	}

	public void set_socket(Socket server) {
		this.server = server;
	}

	public Socket get_socket() {
		return server;
	}

	public void set_session(session session) {
		this.session = session;
	}

	public session get_session() {
		return session;
	}
}