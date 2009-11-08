package packet;

import util.buffer_in;

public class argument_string extends _argument {
	public argument_string(String name) {
		super(name);
	}

	public void retrieve(buffer_in in) {
		set_value(in.getNTString());
	}
}
