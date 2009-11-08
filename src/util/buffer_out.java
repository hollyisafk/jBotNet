package util;

//import java.io.IOException;

public class buffer_out {
    protected byte          buffer[];
    private int             defaultLength = 32;
    private int             currentLength;
    private int             maxLength;
    
    public buffer_out()
    {
        buffer = new byte[defaultLength];
        currentLength = 0;
        maxLength = defaultLength;
    }
    
    public void clear()
    {
        defaultLength = 32;
	        buffer = new byte[defaultLength];
		currentLength = 0;
		maxLength = defaultLength;
    }
    
    public int length()
    {
        return currentLength;
    }
    
    public byte[] getBuffer()
    {
		byte returnBuffer[] = new byte[currentLength];
		System.arraycopy(buffer, 0, returnBuffer, 0, currentLength);
		return returnBuffer;
    }
    
    public void reallocate(int size)
    {
        if ((currentLength + size) <= maxLength)
            return;

		while ((currentLength + size) > maxLength) {
	        maxLength = maxLength * 2;
	    }
	
		byte replaceBuffer[] = new byte[maxLength];
		System.arraycopy(buffer, 0, replaceBuffer, 0, currentLength);
		buffer = replaceBuffer;
    }
    
    public void insertByte(byte data)
    {
        reallocate(1);
        buffer[currentLength++] = data;
    }
    
    public void insertByte(int data)
    {
        reallocate(1);
        buffer[currentLength++] = (byte)data;
    }
    
    public void insertBytes(byte[] data)
    {
        for (int i = 0; i < data.length; i++)
        {
            insertByte(data[i]);
        }
    }
    
    public void insertWord(short data)
    {
        insertByte((byte)((data & 0x00FF) >> 0));
        insertByte((byte)((data & 0xFF00) >> 8));
    }
    
    public void insertWords(short[] data)
    {
        for (int i = 0; i < data.length; i++)
        {
            insertByte((byte)((data[i] & 0x00FF) >> 0));
            insertByte((byte)((data[i] & 0xFF00) >> 8));
        }
    }
    
    public void insertDword(long data)
    {
        insertByte((byte)((data & 0x000000FF) >> 0));
        insertByte((byte)((data & 0x0000FF00) >> 8));
        insertByte((byte)((data & 0x00FF0000) >> 16));
        insertByte((byte)((data & 0xFF000000) >> 24));
    }
    
    public void insertDwords(long[] data)
    {
        for (int i = 0; i < data.length; i++)
        {
            insertByte((byte)((data[i] & 0x000000FF) >> 0));
            insertByte((byte)((data[i] & 0x0000FF00) >> 8));
            insertByte((byte)((data[i] & 0x00FF0000) >> 16));
            insertByte((byte)((data[i]& 0xFF000000) >> 24));
        }
    }
    
    public void insertString(String data)
    {
        for (int i = 0; i < data.length(); i++)
        {
            insertByte((byte)data.charAt(i));
        }
    }
    
    public void insertNTString(String data)
    {
        insertString(data);
        insertByte((byte)0);
    }
    
    public byte[] format(int id)
    {
        byte returnBuffer[] = new byte[currentLength + 4];
		currentLength += 4;
	
		// Build header
		returnBuffer[0] = (byte)0x01;
		returnBuffer[1] = (byte)id;
		returnBuffer[2] = (byte)(((short)currentLength & 0x00FF) >> 0);
		returnBuffer[3] = (byte)(((short)currentLength & 0xFF00) >> 8);
	
		currentLength -= 4;
		System.arraycopy(buffer, 0, returnBuffer, 4, currentLength);
	
		return returnBuffer;
    }
    
    /*public void send(int id) throws IOException
    {
        Arikara.Main.Connection.send(formatBNCS((byte)id));
        clear();
    }*/
    
    /*public void sendProtocolInit() throws IOException
    {
        byte initByte[] = new byte[1];
        initByte[0] = (byte)0x01;
        Arikara.Main.Connection.send(initByte);
    }*/
    
    public String debugOutput()
	{
		StringBuffer returnString = new StringBuffer( (currentLength * 3) + // The hex
			(currentLength) +     // The ascii
			(currentLength / 4) + // The tabs/\n's
			30 );                 // The text
        
		//returnString.append("Buffer contents:\n");
		int i, j; // Loop variables
		for(i = 0; i < currentLength; i++)
		{
			if((i != 0) && (i % 16 == 0))
			{
				// If it's a multiple of 16 and i isn't null, show the ascii
				returnString.append('\t');
				for(j = i - 16; j < i; j++)
				{
					if(buffer[j] < 0x20 || buffer[j] > 0x7F)
						returnString.append('.');
					else
						returnString.append((char)buffer[j]);
				}
				// Add a linefeed after the string
				returnString.append("\n");
			}
    
			returnString.append(Integer.toString((buffer[i] & 0xF0) >> 4, 16) +
				Integer.toString((buffer[i] & 0x0F) >> 0, 16));
			returnString.append(' ');
		}
        
		// Add padding spaces if it's not a multiple of 16
		if(i != 0 && i % 16 != 0)
		{
			for(j = 0; j < ((16 - (i % 16)) * 3); j++)
			{
				returnString.append(' ');
			}
		}
		// Add the tab for alignment
		returnString.append('\t');
    
		// Add final chararacters at right, after padding
    
		// If it was at the end of a line, print out the full line
		if(i > 0 && (i % 16) == 0)
		{
			j = i - 16;
		}    
		else
		{
			j = (i - (i % 16));
		}
    
		for(; i >= 0 && j < i; j++)
		{
			if(buffer[j] < 0x20 || buffer[j] > 0x7F)
				returnString.append('.');
			else
				returnString.append((char) buffer[j]);
		}
    
		// Finally, tidy it all up with a newline
		returnString.append('\n');
		returnString.append("Length: " + currentLength + '\n');
    
		return returnString.toString();
	}
}
