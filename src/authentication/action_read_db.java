package authentication;

import core.terminal;
import data.distributor;
import net.listener;

public class action_read_db extends _action {
	private int m_age;
	
	public action_read_db(int age) {
		super(action_type.READ_DB);
		m_age = age;
	}
	
	public boolean perform(listener client) {
		terminal.print_session(client, "Downloading database");
		distributor.send_database(client, get_age());
		return true;
	}
	
	public int get_age() {
		return m_age;
	}
}
