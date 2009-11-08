package core;

import java.lang.String;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import util._semaphore;

/**
 * please ignore this giant piece of shit i call a config class
 */

public class config extends _semaphore
{
	private static config instance = null;
	
    private config()
    {
    }
    
    public static config get_instance() {
    	if (instance == null)
    		instance = new config();
    	return instance;
    }
    
    public void Write(String block, String var, String value) {
    	Write(block, var, value, "/inc/config.txt");
    }
    
    public void Write(String block, String var, String value, String file_loc)
    {
    	_lock();
        try
        {
            String configFilename = System.getProperty("user.dir") + file_loc;
            File file = new File(configFilename);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String rLine = new String();
            String dLine = new String();
            String wrtConfig = new String();
            String cfgBlock = new String();
            String cfgVar = new String();
            //String cfgValue = new String();
            String ws1 = new String("  ");
            String ws2 = new String();
            if (var.length() < 6)
                ws2 = "\t\t\t";
            else if (var.length() > 5 && var.length() < 15)
                ws2 = "\t\t";
            else
                ws2 = "\t";
            String cfgLineAry[];
            boolean inBlock = false;
            boolean foundLocation = false;
            
            while ((dLine = in.readLine()) != null)
            {
                wrtConfig += (dLine.toString() + "\r\n");
                if (foundLocation)
                    continue;
                
                rLine = NoWhiteSpace(dLine);
                if (rLine.length() == 0)
                    continue;
                if (rLine.charAt(0) == '#')
                    continue;

                cfgLineAry = SplitString(rLine, ' ', (char)0x09);

                if (inBlock)
                {
                    if (rLine.equals("}"))
                    {
                        if (cfgBlock.equalsIgnoreCase(block))
                        {
                            wrtConfig = wrtConfig.substring(0, wrtConfig.length() - 4);
                            wrtConfig += (ws1 + var + ws2 + value + "\r\n}");
                            foundLocation = true;
                        }
                        inBlock = false;
                    }
                    else
                    {
                        cfgVar = cfgLineAry[0];
                        //cfgValue = NoWhiteSpace(rLine.substring(cfgLineAry[0].length(), rLine.length()));
                        if ((cfgBlock.equalsIgnoreCase(block)) && (cfgVar.equalsIgnoreCase(var)))
                        {
                            foundLocation = true;
                            wrtConfig = wrtConfig.substring(0, (wrtConfig.length() - dLine.length()));
                            wrtConfig += (var + ws2 + value + "\r\n");
                        }
                    }
                }
                else
                {
                    if (cfgLineAry[1].equals("{"))
                    {
                        cfgBlock = rLine.substring(0, rLine.length() - 2);
                        inBlock = true;
                    }
                    else if (cfgLineAry[cfgLineAry.length - 1].charAt(cfgLineAry[cfgLineAry.length - 1].length() - 1) == '{')
                    {
                        cfgBlock = rLine.substring(0, rLine.length() - 1);
                        inBlock = true;
                    }
                }
            }
            
            if (foundLocation == false)
            {
                wrtConfig += ("\r\n" + block + " {" + "\r\n");
                wrtConfig += (ws1 + var + ws2 + value + "\r\n");
                wrtConfig += ("}");
            }
            
            
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(wrtConfig.toString(), 0, wrtConfig.length());
            out.flush();
        }
        catch (IOException e)
        {
            terminal.print("Error reading configuration file!");
        } finally {
        	_unlock();
        }
    }
    
    public String Read(String block, String var) {
    	return Read(block, var, "/inc/config.txt");
    }
    
    public String Read(String block, String var, String file_loc)
    {
    	_lock();
        try
        {
            File file = new File(System.getProperty("user.dir") + file_loc);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String rLine = new String();
            String cfgBlock = new String();
            String cfgVar = new String();
            String cfgValue = new String();
            String cfgLineAry[];
            boolean inBlock = false;
            int cLine = 0;

            while ((rLine = in.readLine()) != null)
            {
                cLine++;
                rLine = NoWhiteSpace(rLine);

                if (rLine.length() == 0)
                    continue;
                if (rLine.charAt(0) == '#')
                    continue;

                cfgLineAry = SplitString(rLine, ' ', (char)0x09);

                if (inBlock)
                {
                    if (rLine.equals("}"))
                    {
                        inBlock = false;
                    }
                    else if (cfgLineAry.length < 2)
                    {
                        ThrowError(cLine, "No value set for variable");
                    }
                    else
                    {
                        cfgVar = cfgLineAry[0];
                        cfgValue = NoWhiteSpace(rLine.substring(cfgLineAry[0].length(), rLine.length()));
                        if ((cfgBlock.equalsIgnoreCase(block)) && (cfgVar.equalsIgnoreCase(var)))
                        {
                            return cfgValue;
                        }
                    }
                }
                else
                {
                    if (cfgLineAry.length > 2)
                    {
                        ThrowError(cLine, "Spaces not allowed in block identifier");
                    }
                    else if ((cfgLineAry.length == 0) || (cfgLineAry[cfgLineAry.length - 1].length() == 0))
                    {
                        ThrowError(cLine, "Invalid block opening format");
                    }
                    else
                    {
                        if (cfgLineAry[1].equals("{"))
                        {
                            cfgBlock = rLine.substring(0, rLine.length() - 2);
                            inBlock = true;
                        }
                        else if (cfgLineAry[cfgLineAry.length - 1].charAt(cfgLineAry[cfgLineAry.length - 1].length() - 1) == '{')
                        {
                            cfgBlock = rLine.substring(0, rLine.length() - 1);
                        inBlock = true;
                        }
                        else
                        {
                            ThrowError(cLine, "Block not open");
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            terminal.print("Error reading configuration file!");
        } finally {
        	_unlock();
        }

        return "";
    }
    
    public String NoWhiteSpace(String line)
    {
        String strCurrent = line;
        
        for (int i = 0; i < line.length(); i++)
        {
            if ((strCurrent.charAt(0) == ' ') || (strCurrent.charAt(0) == (byte)0x09))
            {
                strCurrent = strCurrent.substring(1, strCurrent.length());
            }
            else
            {
                break;
            }
        }
        
        return strCurrent;
    }

    private String[] SplitString(String line, char delimeter, char delimetertwo)
    {
        int lcount = 1;

        for (int x = 0; x < line.length(); x++)
        {
            if ((line.charAt(x) == delimeter) || (line.charAt(x) == delimetertwo))
            {
                lcount++;
            }
        }

        String output[] = new String[lcount];
        String cLine = line;
        String current = new String();
        int apos = 0;

        for (int i = 0; i < cLine.length(); i++)
        {
            if ((cLine.charAt(i) == delimeter) || (cLine.charAt(i) == delimetertwo))
            {
                output[apos] = current;
                current = "";
                apos++;
            }
            else
            {
                current += cLine.charAt(i);
            }
        }

        output[apos] = current;
        return output;
    }
    
    public void ThrowError(int line, String description)
    {
        terminal.print("Config Error: Line " + line + " -- " + description);
    }
}