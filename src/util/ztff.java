package util;

public class ztff {
	public static final int ASCII_UCASE_LOW 	= 0x41;
	public static final int ASCII_UCASE_HIGH	= 0x5A;
	public static final int ASCII_LCASE_LOW 	= 0x61;
	public static final int ASCII_LCASE_HIGH	= 0x7A;
	
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
