package user;

import database.account;

public class session {
	public static final int LOGONSTATE_LOGON_PASSED			= 0x01;
	public static final int LOGONSTATE_IDENTIFIED			= 0x02;
	public static final int LOGONSTATE_HAS_USERLIST			= 0x04;
	public static final int LOGONSTATE_LOGGED_IN			= 0x08;
	
	public static final int DBACCESS_READ					= 0x01;
	public static final int DBACCESS_WRITE					= 0x02;
	public static final int DBACCESS_ADMIN					= 0x04;
	
	public int uid;
	public int logon_state;
	
	public String botid;
	public String botpass;
	
	public String bnetusername;
	public String bnetchannel;
	public int bnetserver;
	public String bnetserverip;
	
	/*
	 * 
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
	
	public account jbnaccount;
	public String jbndatabase;
	public String jbnpassword;
	public boolean jbncycle;
	public int jbnflags;
	
	public session() {
		uid = -1;
		logon_state = 0;
		
		botid = "";
		botpass = "";
		
		bnetusername = "";
		bnetchannel = "<Not logged in>";
		bnetserver = 0;
		bnetserverip = "0.0.0.0";
		
		jbnaccount.username = "";
		jbnaccount.password = "";
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
