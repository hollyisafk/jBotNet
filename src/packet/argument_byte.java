package packet;

import util.buffer_in;

public class argument_byte extends _argument {
	public argument_byte(String name) {
		super(name);
	}

	public void retrieve(buffer_in in) {
		set_value(new Byte(in.getByte()).toString());
	}
}
