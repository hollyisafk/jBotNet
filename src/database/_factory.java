package database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Hashtable;

public abstract class _factory<E extends _product> {
	protected String file_path;
	protected Hashtable<String, E> entries;

	public _factory(String file) {
		init(file);
	}
	
	public void init(String file) {
		file_path = file;
		entries = new Hashtable<String, E>();
	}
	
	public void add(E e) {
		entries.put(e.name, e);
	}
	
	public void rem(E e) {
		entries.remove(e);
	}
	
	public Collection<E> getCollection() {
		return entries.values();
	}
	
	public void save() {
		try {
	        FileOutputStream fos = new FileOutputStream( file_path );
	        ObjectOutputStream os = new ObjectOutputStream( fos );
	        
			for (E e : getCollection()) {
			        os.writeObject(e);
			        os.flush();
			}
		} catch (Exception e) {
			System.out.println(":: Save error!");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			entries = new Hashtable<String, E>();
			
			FileInputStream fis = new FileInputStream( file_path );
		    ObjectInputStream is = new ObjectInputStream( fis );
		    E in = (E)is.readObject();
		    
		    while (in != null) {
		    	entries.put(in.name, in);
		    	in = (E)is.readObject();
		    }
		    
		} catch (Exception e) {
		}
	}
}
