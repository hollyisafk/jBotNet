package net;

import java.io.*;
import java.net.*;

import core.*;
import data.*;
import data.parser;
import util.*;
import user.*;

public class listener implements Runnable {
    public Socket server;
    public session session;

    public listener(Socket server) {
      this.server=server;
      session = new session();
      
      buffer_out out = new buffer_out();
      out.insertDword(0x04);
      send(out.format(0x0A));
    }
    
    public void send(byte[] data) {
    	try {
    		//buffer_in tmp = new buffer_in(data);
    		//System.out.println("out:");
    		//System.out.println(tmp.debugOutput());
    		
    		server.getOutputStream().write(data);
    		server.getOutputStream().flush();
    	} catch (IOException e) {
    		System.out.println(":: IOException [listener.send]");
    	}
    }

	public void run () {
      try {
        DataInputStream in = new DataInputStream (server.getInputStream());
        //PrintStream out = new PrintStream(server.getOutputStream());

        byte id = 0;
    	short length = 0;

        buffer_out recv = new buffer_out();
        buffer_out raw = new buffer_out();
        while (true)
        {
            if (server.isClosed() == true)
            {
            	jbotnet.svr.listeners.remove(this);
            	distributor.send_user_logoff(session.uid);
                System.out.println(":: Disconnected!");
                break;
            }
            
            raw.clear();
            int b = in.read();
            raw.insertByte((byte)b);
                    
            if (b == -1)
            {
            	jbotnet.svr.listeners.remove(this);
            	distributor.send_user_logoff(session.uid);
                System.out.println(":: Connection terminated!");
                //currentStatus = SCK_ERROR;
                break;
            }
			
            if (b != 0x01)
            {
            	jbotnet.svr.listeners.remove(this);
            	distributor.send_user_logoff(session.uid);
                System.out.println(":: Invalid packet format!");
                //currentStatus = SCK_ERROR;
                break;
            }

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
            
    		//System.out.println("raw:");
            //System.out.println(raw.debugOutput());
            
            parser.parse(this, id, recv.getBuffer());
            recv.clear();
        }
        server.close();
      } catch (IOException ioe) {
      	distributor.send_user_logoff(session.uid);
        System.out.println(":: IOException [listener.run]");
        ioe.printStackTrace();
    	jbotnet.svr.listeners.remove(this);
      }
    }
}