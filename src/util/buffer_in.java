package util;

public class buffer_in {
	private byte                            buffer[];
	private int				position;
	private int				currentLength;

	public buffer_in()
	{
		buffer = new byte[0];
		currentLength = 0;
		position = 0;
	}

	public buffer_in(byte[] data)
	{
		buffer = new byte[data.length];
		buffer = data;
		currentLength = data.length;
		position = 0;
	}
    
	public void setBuffer(byte[] data)
	{
		buffer = new byte[data.length];
		buffer = data;
		currentLength = data.length;
		position = 0;
	}

        public byte[] getBuffer()
        {
            return buffer;
        }
        
	public byte getByte()
	{
		byte returnByte = (byte)0x00;
		returnByte = buffer[position];
		position++;
		return returnByte;
	}

	public short getWord()
	{
		int returnWord = (((short)getByte() << 0) & 0x00FF) |
			(((short)getByte() << 8) & 0xFF00);
        
		return (short)(returnWord & 0x0000FFFF);
	}

	public int getDword()
	{
		return (((int)getByte() << 0) & 0x000000FF) |
			(((int)getByte() << 8) & 0x0000FF00) |
			(((int)getByte() << 16) & 0x00FF0000) |
			(((int)getByte() << 24) & 0xFF000000);
	}

	public String getString(int size)
	{
		String returnString = new String();
		for (int i = 0; i < size; i++)
		{
			returnString += (char)buffer[position];
			position++;
		}
		return returnString.toString();
	}
        
        public String getNTString()
        {
            for (int i = position, j = 0; i < currentLength; i++, j++) {
                if (buffer[i] == 0x00) {
                    String ret = new String(buffer, position, j);
                    position += (j + 1);
                    return ret;
                }
            }
            System.out.println(":: Buffer couldn't find a null character!");
            return "";
        }
        
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
/*
	public String getNTString()
        {
		StringBuffer returnString = new StringBuffer();
		byte currentByte = (byte)0xFF;
		while (true)
		{
			currentByte = buffer[position];
                        if (currentByte == 0x00)
                            break;
			returnString.append((char)currentByte);
			position++;
		}
		return returnString.toString();
	}
*/
        public byte[] getBytes(int number)
        {
            byte[] ret = new byte[number];
            for (int i = 0; i < number; i++)
                ret[i] = getByte();
        
            return ret;
        }
        
}
