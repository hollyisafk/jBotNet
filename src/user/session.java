package user;

import database.database;

public class session {
	public static final int LOGONSTATE_LOGON_PASSED			= 0x01;
	public static final int LOGONSTATE_IDENTIFIED			= 0x02;
	public static final int LOGONSTATE_HAS_USERLIST			= 0x04;
	public static final int LOGONSTATE_LOGGED_IN			= 0x08;
	
	public static final int DBACCESS_READ					= 0x01;
	public static final int DBACCESS_WRITE					= 0x02;
	public static final int DBACCESS_ADMIN					= 0x04;
	
	private int uid;
	private int logon_state;
	
	private String botid;
	private String botpass;
	
	private String bnetusername;
	private String bnetchannel;
	private int bnetserver;
	private String bnetserverip;
	
	private account jbnaccount;
	private database jbndatabase;
	private boolean jbncycle;
	private int jbnflags;
	
	public session() {
		set_uid(-1);
		logon_state = 0;
		
		set_botid("");
		set_botpass("");
		
		set_bnetusername("");
		set_bnetchannel("<Not logged in>");
		set_bnetserver(0);
		set_bnetserverip("0.0.0.0");
		
		set_jbncycle(false);
	}
	
	public void set_state(int state) {
		logon_state |= state;
	}
	
	public boolean is_state(int state) {
		return ((logon_state & state) == state);
	}
	
	public int get_state() {
		return logon_state;
	}

	public void set_uid(int uid) {
		this.uid = uid;
	}

	public int get_uid() {
		return uid;
	}

	public void set_botid(String botid) {
		this.botid = botid;
	}

	public String get_botid() {
		return botid;
	}

	public void set_botpass(String botpass) {
		this.botpass = botpass;
	}

	public String get_botpass() {
		return botpass;
	}

	public void set_bnetusername(String bnetusername) {
		this.bnetusername = bnetusername;
	}

	public String get_bnetusername() {
		return bnetusername;
	}

	public void set_bnetchannel(String bnetchannel) {
		this.bnetchannel = bnetchannel;
	}

	public String get_bnetchannel() {
		return bnetchannel;
	}

	public void set_bnetserver(int bnetserver) {
		this.bnetserver = bnetserver;
	}

	public int get_bnetserver() {
		return bnetserver;
	}

	public void set_bnetserverip(String bnetserverip) {
		this.bnetserverip = bnetserverip;
	}

	public String get_bnetserverip() {
		return bnetserverip;
	}

	public void set_jbnaccount(account jbnaccount) {
		this.jbnaccount = jbnaccount;
	}

	public account get_jbnaccount() {
		return jbnaccount;
	}

	public void set_jbndatabase(database jbndatabase) {
		this.jbndatabase = jbndatabase;
	}

	public database get_jbndatabase() {
		return jbndatabase;
	}

	public void set_jbncycle(boolean jbncycle) {
		this.jbncycle = jbncycle;
	}

	public boolean is_jbncycle() {
		return jbncycle;
	}

	public void set_jbnflags(int jbnflags) {
		this.jbnflags = jbnflags;
	}

	public int get_jbnflags() {
		return jbnflags;
	}
}
