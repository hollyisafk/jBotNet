package packet;

import util.buffer_in;

public class argument_word extends _argument {
	public argument_word(String name) {
		super(name);
	}

	public void retrieve(buffer_in in) {
		set_value(new Short(in.getWord()).toString());
	}
}
