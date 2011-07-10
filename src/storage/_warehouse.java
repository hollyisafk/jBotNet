package storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import core.terminal;

public abstract class _warehouse<E extends _product> extends _factory<E> {
	private static final long serialVersionUID = 444868625128281409L;
	
	protected String file_path;
	
	public _warehouse(String file) {
		super();
		init(file);
	}
	
	public void init(String file) {
		file_path = file;
		set_entries(new Hashtable<String, E>());
	}
	
	public void save() {
		_lock();
		try {
	        FileOutputStream fos = new FileOutputStream(file_path);
	        ObjectOutputStream os = new ObjectOutputStream(fos);
	        
			for (E e : get_collection()) {
			        os.writeObject(e);
			        os.flush();
			}
		} catch (Exception e) {
			terminal.print("Save error!");
			e.printStackTrace();
		}
		_unlock();
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		_lock();
		try {
			set_entries(new Hashtable<String, E>());
			
			FileInputStream fis = new FileInputStream( file_path );
		    ObjectInputStream is = new ObjectInputStream( fis );
		    E in = (E)is.readObject();
		    
		    while (in != null) {
		    	get_entries().put(in.get_name(), in);
		    	in = (E)is.readObject();
		    }
		    
		} catch (Exception e) {
			//terminal.print("Load error!");
			//e.printStackTrace();
		}
		_unlock();
	}
}
