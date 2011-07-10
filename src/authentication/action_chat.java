package authentication;

import packet.packet_botnetchat;
import user.session;
import core.jbotnet;
import database.database;
import net.listener;

public class action_chat extends _action {
	private int m_command;
	private int m_action;
	private int m_clientid;
	private String m_message;
	
	public action_chat(int command, int action, int clientid, String message) {
		super(action_type.CHAT);
		m_command = command;
		m_action = action;
		m_clientid = clientid;
		m_message = message;
	}
	
	public boolean perform(listener client) {
    	byte[] data = packet_botnetchat.build(get_command(), get_action(), client.get_session().get_uid(), get_message());
    	
		switch (m_command) {
		case 0x00:
			return send_global(client, data);
		case 0x01:
			return send_database(client, data);
		case 0x02:
			return send_user(client, data);
		}
		
		return false;
	}
	
	private boolean send_global(listener client, byte[] data) {
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s1 = l.get_session();
    		session s2 = client.get_session();
    		if (!s1.is_state(login_procedure.LOGONSTATE_IDENTIFIED) ||
    			 s1.get_uid() == s2.get_uid())
    			continue;
    		l.send(data);
    	}
    	return true;
	}
	
	private boolean send_database(listener client, byte[] data) {
    	for (listener l : jbotnet.get_svr().listeners) {
    		session s1 = l.get_session();
    		session s2 = client.get_session();
    		database db1 = s1.get_jbndatabase();
    		database db2 = s2.get_jbndatabase();
    		if (db1 == null || db2 == null)
    			continue;
    		if (s1.get_uid() != s2.get_uid()
    				&& db1.get_dbname().equals(db2.get_dbname())
    				&& db1.get_dbname().length() != 0) {
    			l.send(data);
    		}
    	}
    	return true;
	}
	
	private boolean send_user(listener client, byte[] data) {
    	for (listener l : jbotnet.get_svr().listeners) {
    		if (l.get_session().get_uid() == get_clientid()) {
    			l.send(data);
    			return true;
    		}
    	}
    	return false;
	}
	
	public int get_command() {
		return m_command;
	}
	
	public int get_action() {
		return m_action;
	}
	
	public int get_clientid() {
		return m_clientid;
	}
	
	public String get_message() {
		return m_message;
	}
}
