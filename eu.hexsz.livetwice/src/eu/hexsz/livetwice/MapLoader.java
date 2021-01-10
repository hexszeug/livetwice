package eu.hexsz.livetwice;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MapLoader {
	
	private static MapLoader instance = new MapLoader();
	
	public static MapLoader getInstance() {
		return instance;
	}
	
	private MapLoader() {
	}
	
	public ArrayList<LTMap> maps = new ArrayList<LTMap>();
	public LTMap lobby = null;
	
	private int listPos = 0;
	private LTMap actualMap = null; 
	
	public void importMaps() {
		Logger log = LiveTwicePlugin.pluginInstance.getLogger();
		log.info("Reading map meta file (maps.json):");
		InputStream metaIS = getClass().getClassLoader().getResourceAsStream("maps.json");
		JSONParser JSONParser = new JSONParser();
		JSONObject meta = null;
		try {
			meta = (JSONObject) JSONParser.parse(new InputStreamReader(metaIS, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Failed to read map meta file.");
			return;
		}
		if (!meta.containsKey("maps")) {
			log.warning("Wrong map meta format.");
			return;
		}
		JSONArray maps = (JSONArray) meta.get("maps");
		@SuppressWarnings("unchecked")
		Iterator<Object> i = maps.iterator();
		while(i.hasNext()) {
			JSONObject map = (JSONObject) i.next();
			if (!map.containsKey("name")) log.warning("No name info!");
			if (!map.containsKey("file")) log.warning("No file name info!");
			if (!map.containsKey("author")) log.warning("No author info!");
			String name = (String) map.get("name");
			String fileName = (String) map.get("file");
			String author = (String) map.get("author");
			if(name.equalsIgnoreCase("lobby")) {
				this.lobby = new LTMap(name, fileName, author, this.movedMapLoc(1, 50, 1), log);
				if(!this.lobby.copyFileIntoWorld()) this.lobby = null;
				continue;
			}
			LTMap ltMap = new LTMap(name, fileName, author, this.movedMapLoc(1, 1, 1), log);
			if(!ltMap.copyFileIntoWorld()) continue;
			this.maps.add(ltMap);
		}
		this.listPos = this.maps.size();
	}
	
	public void setupWorld() {
		if (Queue.getInstance().maploadRunnable != null) Queue.getInstance().maploadRunnable.cancel();
		World world = ObjectsFactory.getWorld();
		world.setSpawnLocation(ObjectsFactory.getLobbyLoc());
		double blocksCount = 0;
		blocksCount += this.fill(this.movedMapLoc(0, 0, 0), this.movedMapLoc(49, 90, 49), Material.BARRIER);
		this.fill(this.movedMapLoc(1, 0, 1), this.movedMapLoc(48, 0, 48), Material.BEDROCK);
//		this.fill(this.movedMapLoc(1, 50, 1), this.movedMapLoc(48, 88, 48), Material.BARRIER);
		Bukkit.broadcastMessage("Sucessfully placed " + blocksCount + " blocks.");
		if(this.lobby != null) {
			this.lobby.load();
			Bukkit.broadcastMessage("Sucessfully loaded the lobby.");
		}
		else {
			Bukkit.broadcastMessage("Couldnt load lobby. Ignore it.");
		}
		int gamerulesCount = 0;
		world.setGameRule(GameRule.NATURAL_REGENERATION, false); gamerulesCount ++;
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false); gamerulesCount ++;
		world.setGameRule(GameRule.KEEP_INVENTORY, true); gamerulesCount ++;
		world.setGameRule(GameRule.DO_FIRE_TICK, false); gamerulesCount ++;
		world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false); gamerulesCount ++;
		Bukkit.broadcastMessage("Setted " + gamerulesCount + " gamerules");
		Bukkit.broadcastMessage("Loading random map...");
		String name = this.loadMap();
		Bukkit.broadcastMessage("Successfully loaded map \"" + name + "\".");
		int playerCount = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			PlayerAgency.getInstance().get(player).enterLobby(player);
			playerCount++;
		}
		Bukkit.broadcastMessage("Summoned " + playerCount + " players to the Lobby.");
	}
	
	public boolean isSettedUp() {
		if(this.movedMapLoc(1, 0, 1).getBlock().getType() != Material.BEDROCK) {
			return false;
		}
		if(this.movedMapLoc(49, 89, 49).getBlock().getType() != Material.BARRIER) {
			return false;
		}
		if(this.movedMapLoc(0, 0, 0).getBlock().getType() != Material.BARRIER) {
			return false;
		}
		if(this.movedMapLoc(25, 49, 25).getBlock().getType() != Material.BARRIER) {
			return false;
		}
		return true;
	}
	
	public void clearMap() {
		if (!this.isSettedUp()) {
			Bukkit.broadcastMessage("Please type \"/m setup\" before clear the map");
			return;
		}
		this.fill(this.movedMapLoc(1, 1, 1), this.movedMapLoc(48, 48, 48), Material.AIR);
		for (Entity e : ObjectsFactory.getWorld().getEntities()) {
			if (e instanceof Player) continue;
			e.remove();
		}
	}
	
	public void replaceStructureVoid() {
		this.fillReplace(this.movedMapLoc(1, 1, 1), this.movedMapLoc(48, 48, 48), Material.STRUCTURE_VOID, Material.STRUCTURE_BLOCK);
	}
	
	public double fill(Location edge1, Location edge2, Material material) {
		double counter = 0;
		Location edgeLower = edge1.clone();
		Location edgeHigher = edge2.clone();
		if (edge2.getBlockX() < edge1.getBlockX()) {
			edgeLower.setX(edge2.getX());
			edgeHigher.setX(edge1.getX());
		}
		if (edge2.getBlockY() < edge1.getBlockY()) {
			edgeLower.setY(edge2.getY());
			edgeHigher.setY(edge1.getY());
		}
		if (edge2.getBlockZ() < edge1.getBlockZ()) {
			edgeLower.setZ(edge2.getZ());
			edgeHigher.setZ(edge1.getZ());
		}
		Location loc = edgeLower.clone();
		for (int x = edgeLower.getBlockX(); x <= edgeHigher.getBlockX(); x++) {
			for (int y = edgeLower.getBlockY(); y <= edgeHigher.getBlockY(); y++) {
				for (int z = edgeLower.getBlockZ(); z <= edgeHigher.getBlockZ(); z++) {
					loc.setX(x);
					loc.setY(y);
					loc.setZ(z);
					loc.getBlock().setType(material);
					counter++;
				}
			}
		}
		return counter;
	}
	
	public double fillReplace(Location edge1, Location edge2, Material material, Material replace) {
		double counter = 0;
		Location edgeLower = edge1.clone();
		Location edgeHigher = edge2.clone();
		if (edge2.getBlockX() < edge1.getBlockX()) {
			edgeLower.setX(edge2.getX());
			edgeHigher.setX(edge1.getX());
		}
		if (edge2.getBlockY() < edge1.getBlockY()) {
			edgeLower.setY(edge2.getY());
			edgeHigher.setY(edge1.getY());
		}
		if (edge2.getBlockZ() < edge1.getBlockZ()) {
			edgeLower.setZ(edge2.getZ());
			edgeHigher.setZ(edge1.getZ());
		}
		Location loc = edgeLower.clone();
		for (int x = edgeLower.getBlockX(); x <= edgeHigher.getBlockX(); x++) {
			for (int y = edgeLower.getBlockY(); y <= edgeHigher.getBlockY(); y++) {
				for (int z = edgeLower.getBlockZ(); z <= edgeHigher.getBlockZ(); z++) {
					loc.setX(x);
					loc.setY(y);
					loc.setZ(z);
					if (loc.getBlock().getType() == replace) {
						loc.getBlock().setType(material);
					}
					counter++;
				}
			}
		}
		return counter;
	}
	
	public boolean loadMap(String name) {
		if (name.equalsIgnoreCase("setup")) {
			Bukkit.broadcastMessage("§6Setting up world...");
			this.setupWorld();
			Bukkit.broadcastMessage("§6Finished.");
			return true;
		}
		if (!this.isSettedUp()) {
			Bukkit.broadcastMessage("Please type \"/m setup\" before load a map");
			return false;
		}
		if (name.equalsIgnoreCase("next") || name.equalsIgnoreCase("")) {
			Bukkit.broadcastMessage("§6Loading next map...");
			this.loadMap();
			Bukkit.broadcastMessage("§6Finished.");
			return true;
		}
		for (LTMap map : this.maps) {
			if (map.getName().equalsIgnoreCase(name)) {
				if (Queue.getInstance().maploadRunnable != null) Queue.getInstance().maploadRunnable.cancel();
				Bukkit.broadcastMessage("§6Loading map \"§f§c" + map.getName() + "§r§6\" per command instuction...");
				this.clearMap();
				map.loadWithCredits();
				this.replaceStructureVoid();
				this.actualMap = map;
				Bukkit.broadcastMessage("§6Finished.");
				return true;
			}
		}
		Bukkit.broadcastMessage("§cMap \"" + name + "\" does not exist.");
		return false;
	}
	
	public String loadMap() {
		if (Queue.getInstance().maploadRunnable != null) Queue.getInstance().maploadRunnable.cancel();
		if (!this.isSettedUp()) {
			Bukkit.broadcastMessage("Please type \"/m setup\" before load a map");
			return "";
		}
		this.listPos++;
		if (this.listPos >= this.maps.size()) {
			Collections.shuffle(this.maps);
			this.listPos = 0;
		}
		this.clearMap();
		LTMap map = this.maps.get(this.listPos);
		Bukkit.broadcastMessage("§6Next map is \"§c§l" + map.getName() + "§r§6\" by §c" + map.getAuthor() + "§6.");
		map.loadWithCredits();
		this.replaceStructureVoid();
		this.actualMap = map;
		return map.getName();
	}
	
	public LTMap getActualMap() {
		return this.actualMap;
	}
	
	public List<String> getMapNameList() {
		List<String> result = new ArrayList<String>();
		for (LTMap map : this.maps) {
			result.add(map.getName());
		}
		return result;
	}
	
	private Location movedMapLoc(int x, int y, int z) {
		Location loc = ObjectsFactory.getMapLoc().clone();
		return loc.add(x, y, z).clone();
	}
	
}
