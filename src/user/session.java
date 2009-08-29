package user;

public class session {
	public static final int LOGONSTATE_LOGON_PASSED			= 0x01;
	public static final int LOGONSTATE_IDENTIFIED			= 0x02;
	
	public int logon_state;
	
	public String botid;
	public String botpass;
	
	public String bnetusername;
	public String bnetchannel;
	public String bnetserverip;
	
	public String jbndatabase;
	public String jbnpassword;
	public boolean jbncycle;
	
	public session() {
		botid = "";
		botpass = "";
	}
	
	public void set_state(int state) {
		logon_state |= state;
	}
	
	public boolean is_state(int state) {
		return ((logon_state & state) == state);
	}
}
