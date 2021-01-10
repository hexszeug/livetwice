package eu.hexsz.livetwice;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class ObjectsFactory {
	private static Location mapLoc;
	private static Location spawnLoc;
	private static Location lobbyLoc;
	
	public static Scoreboard teamsBoard;
	public static Map<KitType, Team> teams = new HashMap<KitType, Team>();
	
	
	public static void createConfig() {
		LiveTwicePlugin plugin = LiveTwicePlugin.pluginInstance;
		FileConfiguration config = plugin.getConfig();
		config.addDefault("min-players", 2);
		config.addDefault("start-timer", 15);
		config.addDefault("moderated", false);
		config.addDefault("x-pos", -1);
		config.addDefault("y-pos", 61);
		config.addDefault("z-pos", -1);
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public static void loadConfig() {
		LiveTwicePlugin plugin = LiveTwicePlugin.pluginInstance;
		FileConfiguration config = plugin.getConfig();
		
		mapLoc = new Location(getWorld(), config.getDouble("x-pos"), config.getDouble("y-pos"), config.getDouble("z-pos"));
		spawnLoc = mapLoc.clone();
		spawnLoc = spawnLoc.add(25.0, 47.0, 25.0);
		lobbyLoc = spawnLoc.clone();
		lobbyLoc = lobbyLoc.add(0.0, 3.0, 0.0);
		int minPlayers = config.getInt("min-players");
		if (minPlayers < 2) {
			for (int i = 0; i < 20; i++) {
				Bukkit.getLogger().info("[ERROR]: Started with a min-player lower than 2! Please change!");
			}
		}
		if (minPlayers < 1) {
			minPlayers = 2;
		}
		Queue.getInstance().setConfigData(minPlayers, config.getInt("start-timer"), config.getBoolean("moderated"));
	}
	
	public static void generateTeams() {
		//ScoreboardManager manager = Bukkit.getScoreboardManager();
		//for(KitType kit : KitType.values()) {
		//}
	}
	
	public static Location getMapLoc() {
		return mapLoc.clone();
	}
	
	public static Location getLobbyLoc() {
		return lobbyLoc.clone();
	}
	
	public static Location getIngameLoc() {
		return spawnLoc.clone();
	}
	
	public static World getWorld() {
		return Bukkit.getWorld("world");
	}
	
	public static ItemStack getSwitcher() {
		ItemStack item = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("SWITCH");
		PersistentDataContainer customNBTData = meta.getPersistentDataContainer();
		customNBTData.set(new NamespacedKey(LiveTwicePlugin.pluginInstance, "switcher"), PersistentDataType.STRING, "1b");
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getStarter(boolean isActive) {
		ItemStack item;
		if (isActive) item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		else item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		if (isActive) meta.setDisplayName("§r§cLeave queue");
		else meta.setDisplayName("§r§2Join Game");
		PersistentDataContainer customNBTData = meta.getPersistentDataContainer();
		if (isActive) customNBTData.set(new NamespacedKey(LiveTwicePlugin.pluginInstance, "starter"), PersistentDataType.STRING, "0b");
		else customNBTData.set(new NamespacedKey(LiveTwicePlugin.pluginInstance, "starter"), PersistentDataType.STRING, "1b");
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSpectate() {
		ItemStack item = new ItemStack(Material.ENDER_EYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r§bEnter spectator mode");
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getQueueChestplate() {
		ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
			leatherMeta.setColor(Color.LIME);
			meta = (ItemMeta) leatherMeta;
			item.setItemMeta(meta);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getQueuedSign() {
		ItemStack item = new ItemStack(Material.LIME_CONCRETE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aWaiting queue");
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getKitPrewievQuitter() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cQuit prewiev");
		item.setItemMeta(meta);
		return item;
	}
	
	public static NamespacedKey getNamespacedKey(String key) {
		return new NamespacedKey(LiveTwicePlugin.pluginInstance, "key");
	}
	
	public static void setPrefix(Player player, KitType kit) {
		String kitName = KitType.kitToString(kit).substring(4);
		if (kit == KitType.NOTHING) kitName = "Lobby";
		player.setPlayerListName("§6" + kitName + ": §c§l" + player.getName());
		player.setDisplayName("§c§l" + kitName + ": " + player.getName() + "§6");
	}
}
