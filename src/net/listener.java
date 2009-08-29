package net;

import java.io.*;
import java.net.*;

import core.*;
import util.*;
import user.*;

public class listener implements Runnable {
    public Socket server;
    public session session;

    public listener(Socket server) {
      this.server=server;
      session = new session();
    }
    
    public void send(byte[] data) {
    	try {
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
        while (true)
        {
            if (server.isClosed() == true)
            {
                System.out.println(":: Disconnected!");
                break;
            }
            
            int b = in.read();
                    
            if (b == -1)
            {
                System.out.println(":: Connection terminated!");
                //currentStatus = SCK_ERROR;
                break;
            }
			
            if (b != 0x01)
            {
                System.out.println(":: Invalid packet format!");
                //currentStatus = SCK_ERROR;
                break;
            }

            id = (byte)in.read();

            b = in.read();
            if (b == -1) throw new IOException(":: Connection terminated!");
            length = (short) ((b << 0) & 0x000000FF);

            b = in.read();
            if (b == -1) throw new IOException(":: Connection terminated!");
            length |= (short) ((b << 8) & 0x0000FF00);

            for (int i = 0; i < (length - 4); i++)
            {
	    		b = in.read();
	    		if (b == -1) throw new IOException(":: Connection terminated.");
	    		recv.insertByte((byte) b);
            }
                
            parser.parse(this, id, recv.getBuffer());
            recv.clear();
        }
        server.close();
      } catch (IOException ioe) {
        System.out.println(":: IOException [listener.run]");
        ioe.printStackTrace();
      }
    }
}