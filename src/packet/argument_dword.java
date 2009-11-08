package packet;

import util.buffer_in;

public class argument_dword extends _argument {
	public argument_dword(String name) {
		super(name);
	}

	public void retrieve(buffer_in in) {
		set_value(new Integer(in.getDword()).toString());
	}
}
