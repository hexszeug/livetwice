package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Queue {
	private final static Queue INSTANCE = new Queue();
	
	private List<UUID> queueds = new ArrayList<UUID>();
	private List<UUID> gamers = new ArrayList<UUID>();
	
	private BukkitRunnable runnable;
	private int minPlayers = 2;
	private int timeToStart = 15;
	public boolean modMode = false;
	
	private boolean hideStart = false;
	private BukkitRunnable startRunnable = null;
	private BukkitRunnable winSoundRunnable = null;
	
	public BukkitRunnable maploadRunnable = null;
	
	private Queue() {
		
	}
	
	public static Queue getInstance() {
		return INSTANCE;
	}
	
	public void setConfigData(int minPlayers, int timeToStart, boolean modMode) {
		this.minPlayers = minPlayers;
		this.timeToStart = timeToStart;
		this.modMode = modMode;
		
	}
	
	public boolean loginPlayer(Player player) {
		if (!PlayerAgency.getInstance().get(player).selectedTwoKits()) {
			player.sendMessage("§cPlease select two kits with the pickaxe!");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.VOICE, 100, 1);
			return false;
		}
		player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.VOICE, 100, 1);
		UUID id = player.getUniqueId();
		if (!queueds.contains(id)) queueds.add(id);
		return true;
	}
	
	public void logoutPlayer(Player player) {
		player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT, SoundCategory.VOICE, 100, 1);
		UUID id = player.getUniqueId();
		if (queueds.contains(id)) queueds.remove(id);
		if (this.runnable != null) if (!this.runnable.isCancelled()) if (queueds.size() < this.minPlayers) {
			this.runnable.cancel();
			Bukkit.broadcastMessage("§cStart cancelled!");
		}
	}
	
	public void startTimer() {
		if (this.modMode) return;
		if (LiveTwicePlugin.pluginInstance.gameIsRunning) return;
		if (queueds.size() < this.minPlayers) return;
		if (this.runnable != null) if (!runnable.isCancelled()) return;
		this.runnable = new BukkitRunnable() {

			private int timer;
			private Queue queue;
			
			public BukkitRunnable setData(Queue queue, int time) {
				this.queue = queue;
				this.timer = time + 1;
				return this;
			}
			
			@Override
			public void run() {
				this.timer--;
				this.queue.showTimer(this.timer);
				if (this.timer <= 0) {
					this.queue.startGame();
					this.cancel();
				}
				if (this.queue.getQueuedsCount() >= Bukkit.getOnlinePlayers().size()) {
					if (this.queue.maploadRunnable == null) {
						this.queue.startGame();
						this.cancel();
					}
					else if (this.queue.maploadRunnable.isCancelled()) {
						this.queue.startGame();
						this.cancel();
					}
				}
			}
			
		}.setData(this, this.timeToStart);
		this.runnable.runTaskTimer(LiveTwicePlugin.pluginInstance, 0, 20);
		
	}
	
	public void showTimer(int timer) {
		Bukkit.broadcastMessage("§6Game starts in §c" + timer + "s§6!");
	}
	
	public void startGame() {
		ObjectsFactory.getWorld().setTime(0);
		ObjectsFactory.getWorld().setWeatherDuration(0);
		this.hideStart = true;
		this.gamers.clear();
		List<String> names = new ArrayList<String>();
		for (UUID id : this.queueds) {
			Player player = Bukkit.getPlayer(id);
			LTMap map = MapLoader.getInstance().getActualMap();
			player.sendTitle("§6Invisible period (§c30s§6)", "§6The map is \"§f§c" + map.getName() + "§r§6\" by §c" + map.getAuthor(), 0, 180, 20);
			PlayerAgent agent = PlayerAgency.getInstance().get(player);
			agent.reliveCrts(player);
			this.gamers.add(id);
			names.add(player.getName());
			player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.VOICE, 100, 0);
			agent.leaveJumpnRun(player, false);
			agent.gameStartsStats(player);
		}
		Bukkit.broadcastMessage("§6++++++++++ §c§lGO! §r§6++++++++++");
		this.queueds.clear();
		LiveTwicePlugin.pluginInstance.gameIsRunning = true;
		LiveTwicePlugin.pluginInstance.getLogger().info(names.toString() + " are in.");
		this.hideShowPlayers();
		if (this.startRunnable != null) this.startRunnable.cancel();
		this.startRunnable = new BukkitRunnable() {
			private int timer = 30;
			
			@Override
			public void run() {
				this.timer--;
				if (!Queue.getInstance().areSpectators()) {
					this.timer = 0;
				}
				if (this.timer <= 0) {
					Queue.getInstance().stopHideStart();
					Queue.getInstance().hideShowPlayers();
					this.cancel();
				}
			}
			
		};
		this.startRunnable.runTaskTimer(LiveTwicePlugin.pluginInstance, 0, 20);
	}
	
	public void stopHideStart() {
		this.hideStart = false;
		this.hideShowPlayers();
		for (UUID id : this.gamers) {
			Player player = Bukkit.getPlayer(id);
			player.sendTitle("§cFight!", "§6Everyone is now visible!", 0, 80, 20);
			player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.VOICE, 100, 2);
		}
	}
	
	public boolean isQueued(Player player) {
		return queueds.contains(player.getUniqueId());
	}
	
	public String[] getQueueds() {
		List<String> result = new ArrayList<String>();
		for (UUID uuid : queueds) {
			result.add(Bukkit.getPlayer(uuid).getName());
		}
		result.add("Count: " + queueds.size());
		return result.toArray(new String[0]);
	}
	
	public int getQueuedsCount() {
		return queueds.size();
	}
	
	public List<String> getNotQueuedsPlusKitState() {
		List<String> result = new ArrayList<String>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!queueds.contains(player.getUniqueId())) {
				PlayerAgent agent = PlayerAgency.getInstance().get(player);
				result.add(player.getName() + " forgot: " + agent.getKitState());
			}
		}
		return result;
	}
	
	public int getMinPlayers() {
		return this.minPlayers;
	}
	
	public boolean isIngame(Player player) {
		return gamers.contains(player.getUniqueId());
	}
	
	public int playersIn() {
		return this.gamers.size();
	}
	
	public boolean isHideStart() {
		return this.hideStart;
	}
	
	public boolean areSpectators() {
		int playersOn = Bukkit.getOnlinePlayers().size();
		if (this.queueds.size() >= playersOn) return false;
		else return true;
	}
	
	public void playerQuit(Player player) {
		UUID id = player.getUniqueId();
		if (this.queueds.contains(id)) this.queueds.remove(id);
		if (this.gamers.contains(id)) this.gamers.remove(id);
		this.hideShowPlayers();
		this.wins();
	}
	
	public void wins() {
		if (this.playersIn() > 1) return;
		if (!LiveTwicePlugin.pluginInstance.gameIsRunning) return;
		if (this.gamers.isEmpty()) {
			Bukkit.broadcastMessage("§c§lQuitted Game!");
		}
		else {
			Player winner = Bukkit.getPlayer(this.gamers.get(0));
			this.gamers.clear();
			Bukkit.broadcastMessage("§c§l" + winner.getName() + " §6wins!");
			for (Player player : ObjectsFactory.getWorld().getPlayers()) {
				player.sendTitle("§c" + winner.getName(), "§6wins!", 0, 242, 20);
			}
			PlayerAgent agent = PlayerAgency.getInstance().get(winner);
			agent.enterLobby(winner);
			agent.winStats(winner);
		}
		LiveTwicePlugin.pluginInstance.gameIsRunning = false;
		this.hideShowPlayers();
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.MUSIC_DISC_PIGSTEP, SoundCategory.VOICE, 100, 1);
		}
		if (this.winSoundRunnable != null) this.winSoundRunnable.cancel();
		this.winSoundRunnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.stopSound(Sound.MUSIC_DISC_PIGSTEP, SoundCategory.VOICE);
				}
				this.cancel();
			}
			
		};
		this.winSoundRunnable.runTaskLater(LiveTwicePlugin.pluginInstance, 242);
		if (this.maploadRunnable != null) this.maploadRunnable.cancel();
		this.maploadRunnable = new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!LiveTwicePlugin.pluginInstance.gameIsRunning) {
					MapLoader.getInstance().loadMap();
				}
				this.cancel();
			}
			
		};
		this.maploadRunnable.runTaskLater(LiveTwicePlugin.pluginInstance, 250);
	}
	
	public void hideShowPlayers() {
		LiveTwicePlugin plugin = LiveTwicePlugin.pluginInstance;
		Collection<? extends Player> players = Bukkit.getOnlinePlayers(); 
		for (Player playerA : players) {
			PlayerAgent agent = PlayerAgency.getInstance().get(playerA);
			if (agent == null) continue;
			playerA.setLevel(0);
			if (this.isIngame(playerA)) playerA.setAllowFlight(false);
			else if (agent.isJumpnRunning()) {
				playerA.setAllowFlight(false);
			}
			else {
				playerA.setAllowFlight(true);
			}
			for (Player playerB : players) {
				if (plugin.gameIsRunning) {
					if (this.hideStart) {
						if (this.isIngame(playerA)) playerA.hidePlayer(plugin, playerB);
						else playerA.showPlayer(plugin, playerB);
					}
					else {
						if (this.isIngame(playerA) && !this.isIngame(playerB)) playerA.hidePlayer(plugin, playerB);
						else playerA.showPlayer(plugin, playerB);
					}
				}
				else {
					playerA.showPlayer(plugin, playerB);
				}
			}
		}
	}
}
