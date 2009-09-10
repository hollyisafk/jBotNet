package user;

public class session {
	public static final int LOGONSTATE_LOGON_PASSED			= 0x01;
	public static final int LOGONSTATE_IDENTIFIED			= 0x02;
	public static final int LOGONSTATE_HAS_USERLIST			= 0x04;
	
	public int uid;
	public int logon_state;
	
	public String botid;
	public String botpass;
	
	public String bnetusername;
	public String bnetchannel;
	public int bnetserver;
	public String bnetserverip;
	
	public String jbnusername;
	public String jbnuserpass;
	public String jbndatabase;
	public String jbnpassword;
	public boolean jbncycle;
	
	public session() {
		uid = -1;
		logon_state = 0;
		
		botid = "";
		botpass = "";
		
		bnetusername = "";
		bnetchannel = "<Not logged in>";
		bnetserver = 0;
		bnetserverip = "0.0.0.0";
		
		jbnusername = "";
		jbnuserpass = "";
		jbndatabase = "public";
		jbnpassword = "";
		jbncycle = false;
	}
	
	public void set_state(int state) {
		logon_state |= state;
	}
	
	public boolean is_state(int state) {
		return ((logon_state & state) == state);
	}
}
