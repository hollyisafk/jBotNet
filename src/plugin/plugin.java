package plugin;

public abstract class plugin {
    private String name                     = null;
    private String info                     = null;
    private String author                   = null;
    private float version              		= 0.0f;
    
    public plugin() {}
    
    protected plugin(String newName, String newInfo, String newAuthor, float newVersion) {
        name = newName;
        info = newInfo;
        author = newAuthor;
        version = newVersion;
    }
    
    public abstract void initialize();
    
    public void setName(String newName) {
        name = newName;
    }
    
    public void setInfo(String newInfo) {
        info = newInfo;
    }

    public void setAuthor(String newAuthor) {
        author = newAuthor;
    }
    
    public void setVersion(float newVersion) {
        version = newVersion;
    }
    
    public String getName() {
        return name;
    }
    
    public String getInfo() {
        return info;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public float getVersion() {
    	return version;
    }
}
