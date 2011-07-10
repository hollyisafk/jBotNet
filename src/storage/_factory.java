package storage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;

import util._semaphore;

public abstract class _factory<E extends _product> extends _semaphore implements Serializable {
	private static final long serialVersionUID = 1093478949172778752L;
	
	private Hashtable<String, E> entries;

	public _factory() {
		set_entries(new Hashtable<String, E>());
	}
	
	public void add(E e) {
		get_entries().put(e.get_name().toLowerCase(), e);
	}
	
	public void rem(String key) {
		get_entries().remove(key.toLowerCase());
	}
	
	public Collection<E> get_collection() {
		return get_entries().values();
	}

	protected void set_entries(Hashtable<String, E> entries) {
		this.entries = entries;
	}

	protected Hashtable<String, E> get_entries() {
		return entries;
	}
}
