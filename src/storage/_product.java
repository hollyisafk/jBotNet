package storage;

import java.io.Serializable;

public abstract class _product implements Serializable {
	private static final long serialVersionUID = 7192872896913640696L;
	
	private String name;

	protected void set_name(String name) {
		this.name = name;
	}

	protected String get_name() {
		return name;
	}
}
