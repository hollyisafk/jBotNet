package plugin;

import java.io.File;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;

import core.terminal;

public class plugin_manager {
    private HashMap<String, plugin> plugins;
    private HashMap<String, plugin> active_plugins;
    
    private static plugin_manager instance = new plugin_manager();
    
    public static plugin_manager get_instance() {
    	return instance;
    }
    
    public plugin_manager() {
        plugins = active_plugins = new HashMap<String, plugin>();
        load_plugins();
    }
	
    public int plugin_count() {
    	return plugins.size();
    }
    
	private void load_plugins()
    {
        File dir = new File(System.getProperty("user.dir") + "/bin/plugins/"); // <-- here
        File jars[] = dir.listFiles();
        for (int i = 0; i < jars.length; i++)
        {
            try {
                load_plugin(jars[i].toURI().toURL());
            } catch (MalformedURLException e) {
                terminal.print("Error loading plugin \"" + jars[i].getName() + "\": " +
                    e.getMessage());
            }
        }
    }
    
    @SuppressWarnings("unused")
	private void load_plugin(String jarFile)
    {
        try
        {
            load_plugin(new URL(jarFile));
        } 
        catch (MalformedURLException e)
        {

            try
            {
                load_plugin(new URL("file:///" + jarFile));
            }
            catch (MalformedURLException e2)
            {
                terminal.print("Malformed URL: " + e.getLocalizedMessage());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	private void load_plugin(URL jar)
    {
        try
        {
            URL[] urls = { jar };
            URLClassLoader loader = new URLClassLoader(urls);
            Class<plugin> pluginClass = (Class<plugin>)loader.loadClass("main");
            plugin p = (plugin) pluginClass.newInstance();
            p.initialize();
            
            plugins.put(p.getName(), p);
            active_plugins.put(p.getName(), p);
            terminal.print("Loaded plugin \"" + p.getName() + "\".");
        }
        catch (Exception e)
        {
            terminal.print("Error loading plugin: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
