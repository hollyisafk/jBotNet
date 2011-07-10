package util;

public class helper {
	public static int raw_ip_to_dword(byte[] addr_raw) {
		int addr = (((int)addr_raw[0] << 0) & 0x000000FF) |
		   (((int)addr_raw[1] << 8) & 0x0000FF00) |
		   (((int)addr_raw[2] << 16) & 0x00FF0000) |
		   (((int)addr_raw[3] << 24) & 0xFF000000);
		return addr;
	}
	
	public static byte[] dword_to_raw_ip(int ipaddr) {
		byte octet[] = new byte[4];
    	octet[0] = (byte)((ipaddr & 0x000000FF) >> 0);
    	octet[1] = (byte)((ipaddr & 0x0000FF00) >> 8);
    	octet[2] = (byte)((ipaddr & 0x00FF0000) >> 16);
    	octet[3] = (byte)((ipaddr & 0xFF000000) >> 24);
    	return octet;
	}
	
	public static String implode(byte[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}
}
