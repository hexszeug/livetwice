package eu.hexsz.livetwice;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LiveTwicePlugin extends JavaPlugin{
	
	public static LiveTwicePlugin pluginInstance;
	
	public boolean gameIsRunning = false;
	
	public void onEnable() {
		pluginInstance = this;
		this.getLogger().info("Started LiveTwice!");
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		MyListener myListener = new MyListener();
		pluginManager.registerEvents(myListener, this);
		
		ObjectsFactory.createConfig();
		ObjectsFactory.loadConfig();
		
		getCommand("livetwice-info").setExecutor(new InfoCommand());
		getCommand("s").setExecutor(new SCommand());
		getCommand("queue").setExecutor(new QueueCommand());
		getCommand("map").setExecutor(new MapCommand());
		
		MapLoader.getInstance().importMaps();
		MapLoader.getInstance().clearMap();
		MapLoader.getInstance().loadMap();
	}
	
	public void onDisable() {
		this.getLogger().info("Stopped LiveTwice!");
	}
}
