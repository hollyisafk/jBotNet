package authentication;

import net.listener;
import util.buffer_in;

public class action_send_packet extends _action {
	private int _packet_id = 0;
	private buffer_in _data;
	
	public action_send_packet(int packet_id, buffer_in data) {
		super(action_type.SEND_PACKET);
		_packet_id = packet_id;
		_data = data;
	}
	
	public int get_packet_id() {
		return _packet_id;
	}
	
	public buffer_in get_data() {
		return _data;
	}
	
	public boolean perform(listener client) {
		//purely for authentication
		return true;
	}
}
