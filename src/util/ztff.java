package util;

public class ztff {
	public static final int ASCII_UCASE_LOW 	= 0x41;
	public static final int ASCII_UCASE_HIGH	= 0x5A;
	public static final int ASCII_LCASE_LOW 	= 0x61;
	public static final int ASCII_LCASE_HIGH	= 0x7A;
	
	/*
 	(4.1) (DWORD) database access flags
		1 = read
		2 = write
		4 = restricted access
	(4.1) (DWORD) administrative capabilities
		Specified in Zerobot Traditional Flags Format (ZTFF):
		A = superuser, can perform any administrative action
		B = broadcast, may use talk-to-all
		C = connection, may administer botnet connectivity
		D = database, may create and maintain databases
		I = ID control, may create and modify hub IDs
		S = botnet service
	(4.1) (Admin only) (DWORD) IP address of the bot being described
	 */
	
	public static int getFlag(char flag) {
		if (!Character.isLetter(flag))
			return 0;
		
		int ascii = 0;
		int value = 1;
		if (Character.isUpperCase(flag))
			ascii = flag - ASCII_UCASE_LOW;
		else
			ascii = flag - ASCII_LCASE_LOW;
		
		for (int i = 0; i < ascii; i++)
			value *= 2;
		
		return value;
	}
	
	public static int setFlag(int flags, char flag) {
		return flags | flag;
	}
	
	public static boolean isFlag(int flags, char flag) {
		return ((flags & flag) == flag);
	}
}
