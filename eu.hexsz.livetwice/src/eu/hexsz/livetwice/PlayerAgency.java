package eu.hexsz.livetwice;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

//SINGLETON

public final class PlayerAgency {
	private final static PlayerAgency INSTANCE = new PlayerAgency();
	
	private Map<UUID, PlayerAgent> plMap;
	
	private PlayerAgency() {
		this.plMap = new HashMap<UUID, PlayerAgent>();
	}
	
	public static PlayerAgency getInstance() {
		return INSTANCE;
	}
	
	public void add(Player player) {
		this.plMap.put(player.getUniqueId(), new PlayerAgent());
	}
	
	public boolean has(Player player) {
		return this.plMap.containsKey(player.getUniqueId());
	}
	
	public PlayerAgent get(Player player) {
		return this.plMap.get(player.getUniqueId());
	}
	
	public void remove(Player player) {
		this.plMap.remove(player.getUniqueId());
	}
}
