package authentication;

import packet.packet_message;
import user.session;
import core.jbotnet;
import core.terminal;
import database.database;
import net.listener;

public class action_send_command extends _action {
	private int m_senderid;
	private int m_status;
	private String m_sender;
	private String m_command;
	
	public action_send_command(int senderid, int status, String sender, String command) {
		super(action_type.SEND_COMMAND);
		m_senderid = senderid;
		m_status = status;
		m_sender = sender;
		m_command = command;
	}
	
	public boolean perform(listener client) {
		terminal.print_session(client, "Sending command: " + get_command());
		
		switch (get_status()) {
		case 0x01:
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
        			l.send(packet_message.build(get_senderid(), get_status(), get_sender(), get_command()));
        		}
        	}
			break;
		case 0x02:
        	for (listener l : jbotnet.get_svr().listeners) {
        		if (l.get_session().get_uid() == get_senderid()) {
        			l.send(packet_message.build(get_senderid(), get_status(), get_sender(), get_command()));
        			return true;
        		}
        	}
		}
		return true;
	}
	
	public int get_senderid() {
		return m_senderid;
	}
	
	public int get_status() {
		return m_status;
	}
	
	public String get_sender() {
		return m_sender;
	}
	
	public String get_command() {
		return m_command;
	}
}
