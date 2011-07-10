package packet;

import util.buffer_in;

public abstract class _argument {
	private String name;
	// this is a bad idea; but all Java objects will cast into
	// a string, so in this situation I will assume it is safe
	private String value;
	
	protected _argument(String name) {
		this.set_name(name);
	}
	
	public abstract void retrieve(buffer_in in);

	private void set_name(String name) {
		this.name = name;
	}

	public String get_name() {
		return name;
	}

	protected void set_value(String value) {
		this.value = value;
	}

	protected String get_value() {
		return value;
	}
	
	public String get_string() {
		return value;
	}
	
	public byte get_byte() {
		try {
			return Byte.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public short get_short() {
		try {
			return Short.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int get_int() {
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			return 0;
		}
	}
}
