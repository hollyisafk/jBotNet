package database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class _factory<E> {
	protected String file_path;
	protected ArrayList<E> entries;

	public _factory(String file) {
		init(file);
	}
	
	public void init(String file) {
		file_path = file;
		entries = new ArrayList<E>();
	}
	
	public void add(E e) {
		entries.add(e);
	}
	
	public void rem(E e) {
		entries.remove(e);
	}
	
	public Iterator<E> getIterator() {
		return entries.iterator();
	}
	
	public void save() {
		try {
	        FileOutputStream fos = new FileOutputStream( file_path );
	        ObjectOutputStream os = new ObjectOutputStream( fos );
	        
			for (E e : entries) {
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
			entries = new ArrayList<E>();
			
			FileInputStream fis = new FileInputStream( file_path );
		    ObjectInputStream is = new ObjectInputStream( fis );
		    E in = (E)is.readObject();
		    
		    while (in != null) {
		    	entries.add(in);
		    	in = (E)is.readObject();
		    }
		    
		} catch (Exception e) {
		}
	}
}
