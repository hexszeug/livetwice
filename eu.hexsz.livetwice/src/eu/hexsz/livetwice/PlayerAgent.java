package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerAgent {
	private KitType kitA;
	private KitType kitB;
	private AbstractKit crtA;
	private AbstractKit crtB;
	private boolean crtAAlive;
	private boolean crtBAlive;
	private char crt; //A = kitA; B = kitB
	private boolean usedA;
	private boolean usedB;
	
	
	private KitMenu menu;
	
	private int cooldown;
	private int cooldownStart = 5;
	
	private boolean isInLobby = false;
	private boolean isJumpnRunning = false;
	
	public PlayerAgent() {
		this.crt = 'A';
		this.crtAAlive = false;
		this.crtBAlive = false;
		this.kitA = KitType.NOTHING;
		this.kitB = KitType.NOTHING;
		this.menu = new KitMenu();
		PluginManager pluginManager = LiveTwicePlugin.pluginInstance.getServer().getPluginManager();
		pluginManager.registerEvents(this.menu, LiveTwicePlugin.pluginInstance);
	}
	
	public void openMenu(Player player) {
		menu.open(player, 0, this.kitA, this.kitB);
	}
	
	public void setKit(char id, KitType kit, Player player) {
		if (kit != KitType.NOTHING) {
			switch (id) {
			case 'A':
			case 'a':
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.VOICE, 100, 0);
				if (this.kitB == kit) {
					AbstractKit crtBuffer = this.crtA;
					KitType kitBuffer = this.kitA;
					this.crtA = this.crtB;
					this.kitA = this.kitB;
					this.crtB = crtBuffer;
					this.kitB = kitBuffer;
					player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.VOICE, 100, 0);
				}
				this.kitA = kit;
				this.crtA = this.createCrtByKit(kit);
				break;
			case 'B':
			case 'b':
				player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.VOICE, 100, 0);
				if (this.kitA == kit) {
					AbstractKit crtBuffer = this.crtA;
					KitType kitBuffer = this.kitA;
					this.crtA = this.crtB;
					this.kitA = this.kitB;
					this.crtB = crtBuffer;
					this.kitB = kitBuffer;
					player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.VOICE, 100, 0);
				}
				this.kitB = kit;
				this.crtB = this.createCrtByKit(kit);
				break;
			default:
				LiveTwicePlugin.pluginInstance.getLogger().info("Someone tried to change a not existing Character.");
				break;
			}
		}
		else {
			player.sendMessage("§cNothing is not a kit.");
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.VOICE, 100, 1);
		}
	}
	
	public boolean selectedTwoKits() {
		return this.kitA != KitType.NOTHING && this.kitB != KitType.NOTHING;
	}
	
	public boolean switchCrt(Player player, boolean withLimitations) {
		if (withLimitations) if (!this.isSwitchable(player)) return false;
		switch(this.crt) {
			case 'A':
				this.crtA.save(player);
				this.crt = 'B';
				this.crtB.equip(player);
				ObjectsFactory.setPrefix(player, this.kitB);
				if (!this.usedB) this.setStartSlowFalling(player);
				break;
			case 'B':
				this.crtB.save(player);
				this.crt = 'A';
				this.crtA.equip(player);
				ObjectsFactory.setPrefix(player, this.kitA);
				if (!this.usedA) this.setStartSlowFalling(player);
				break;
		}
		player.setFireTicks(0);
		player.setArrowsInBody(0);
		this.refreshStats(player);
		return true;
	}
	
	public boolean isSwitchable(Player player) {
		Entity entity = (Entity) player;
		if (!LiveTwicePlugin.pluginInstance.gameIsRunning) {
			player.sendMessage("§cYou can't switch because the game is not running.");
			this.enterLobby(player);
			return false;
		}
		if (!(this.crtAAlive && this.crtBAlive)) {
			player.sendMessage("§cYou can't switch because your other character is dead.");
			return false;
		}
		else if ((this.kitA == KitType.NOTHING || this.kitB == KitType.NOTHING)) {
			player.sendMessage("§cYou can't switch because your haven't chosen two kits.");
			return false;
		}
		else if (this.cooldown > 0) {
			player.sendMessage("§cYou have to wait " + this.cooldown + "s!");
			return false;
		}
		else if (!entity.isOnGround()) {
			player.sendMessage("§cYou have to be on the ground to switch!");
			return false;
		}
		else return true;
	}
	
	public void died(Player player) {
		if ((this.crtAAlive || this.crtBAlive)) {
			if (this.crtAAlive && this.crtBAlive) {
				player.sendMessage("§6§oYour character died. Try your best with the one left.");
				switch (this.crt) {
				case 'A':
				case 'a':
					this.crtBAlive = false;
					break;
				case 'B':
				case 'b':
					this.crtAAlive = false;
					break;
				}
			}
			else {
				this.crtAAlive = this.crtBAlive = false;
				player.sendMessage("§6§oBoth of your characters died. You are out. Try again next time.");
				this.loseStats(player);
				Queue.getInstance().playerQuit(player);
			}
		}
		else {
			this.enterLobby(player);
			return;
		}
		this.respawn(player);
	}
	
	public void respawn(Player player) {
		if (this.crtAAlive || this.crtBAlive) {
			this.switchCrt(player, false);
		}
		else {
			this.enterLobby(player);
		}
	}
	
	public void reliveCrts(Player player) {
		if (this.crtA != null && this.crtB != null) {
			this.crtAAlive = true;
			this.crtBAlive = true;
			this.usedA = false;
			this.usedB = false;
			Location position = ObjectsFactory.getIngameLoc();
			this.crt = 'A';
			this.crtA.save(player);
			Random random = new Random();
			int x = random.nextInt(46) - 23;
			int z = random.nextInt(46) - 23;
			this.crtA.refill(position.clone().add(x, 0, z), player);
			x = random.nextInt(46) - 23;
			z = random.nextInt(46) - 23;
			this.crtB.refill(position.clone().add(x, 0, z), player);
			this.crtA.equip(player);
			ObjectsFactory.setPrefix(player, this.kitA);
			this.isInLobby = false;
			this.setStartSlowFalling(player);
			this.usedA = true;
		}
	}
	
	private void setStartSlowFalling(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 160, 1, false));
	}
	
	public AbstractKit getCrt(boolean actual) {
		switch (this.crt) {
		case 'A':
		case 'a':
			if (actual) return this.crtA;
			else return this.crtB;
		case 'B':
		case 'b':
			if (actual) return this.crtB;
			else return this.crtA;
		default:
			return null;
		}
	}
	
	public void countCooldown(Player player, BukkitRunnable task) {
		float progress = ((float) this.cooldown) / ((float) this.cooldownStart);
		if (progress >= 0.0 && progress <= 1.0) {
			player.setExp(progress);
		}
		if (this.cooldown <= 0) {
			task.cancel();
		}
		else {
			this.cooldown--;
		}
	}
	
	public void startCooldown(Player player) {
		this.cooldown = this.cooldownStart;
		BukkitRunnable runnable = new BukkitRunnable() {
			private PlayerAgent agent;
			private Player player;
			
			public BukkitRunnable setData(PlayerAgent agent, Player player) {
				this.agent = agent;
				this.player = player;
				return this;
			}
			
			@Override
			public void run() {
				if (this.agent == null) this.cancel();
				this.agent.countCooldown(this.player, this);
			}
		}.setData(this, player);
		runnable.runTaskTimer(LiveTwicePlugin.pluginInstance, 0, 20);
	}
	
	public AbstractKit createCrtByKit(KitType kit) {
		switch(kit) {
		case FLYING_BOWMAN: return new FlyingBowmanKit();
		case KNIGHT: return new KnightKit();
		case TURTLE_MASTER: return new TurtleMasterKit();
		case ELF: return new ElfKit();
		case ASSASSIN: return new AssassinKit();
		case SQUIRE: return new SquireKit();
		case TANK: return new TankKit();
		case GHOST: return new GhostKit();
		case GIANT: return new GiantKit();
		case DWARF: return new DwarfKit();
		case HEALER: return new HealerKit();
		case SMART_GUY: return new SmartGuyKit();
		case BUILDER: return new BuilderKit();
		case PRINCE: return new PrinceKit();
		case UNICORN: return new UnicornKit();
		case NOTHING: return null;
		}
		
		return null;
	}
	
	public void enterLobby(Player player) {
		ObjectsFactory.setPrefix(player, KitType.NOTHING);
		this.isInLobby = true;
		player.setExp(0.0f);
		player.setGameMode(GameMode.ADVENTURE);
		player.getInventory().setHeldItemSlot(4);
		player.setHealth(20);
		Location position = ObjectsFactory.getLobbyLoc();
		player.teleport(position);
		for (Iterator<PotionEffect> i = player.getActivePotionEffects().iterator(); i.hasNext();) {
			player.removePotionEffect(i.next().getType());
		}
		player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 255, false, false, false));
		player.setArrowsInBody(0);
		player.setFireTicks(0);
		this.giveLobbyItems(player);
		if (this.crtA instanceof UnicornKit) ((UnicornKit) this.crtA).delete();
		if (this.crtB instanceof UnicornKit) ((UnicornKit) this.crtB).delete();
		player.setAllowFlight(true);
		this.refreshStats(player);
	}
	
	private void giveLobbyItems(Player player) {
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(0, ObjectsFactory.getSpectate());
		inventory.setItem(4, this.menu.getOpener());
		if (Queue.getInstance().isQueued(player)) {
			inventory.setItem(7, ObjectsFactory.getStarter(true));
			inventory.setChestplate(ObjectsFactory.getQueueChestplate());
			inventory.setItem(1, ObjectsFactory.getQueuedSign());
			inventory.setItem(2, ObjectsFactory.getQueuedSign());
			inventory.setItem(3, ObjectsFactory.getQueuedSign());
			inventory.setItem(5, ObjectsFactory.getQueuedSign());
			inventory.setItem(6, ObjectsFactory.getQueuedSign());
			inventory.setItem(8, ObjectsFactory.getQueuedSign());
		}
		else {
			inventory.setItem(8, ObjectsFactory.getStarter(false));
		}
	}
	
	public void joinLeaveQueue(Player player) {
		Queue queue = Queue.getInstance();
		if (queue.isQueued(player)) {
			queue.logoutPlayer(player);
			this.giveLobbyItems(player);
		}
		else {
			queue.loginPlayer(player);
			this.giveLobbyItems(player);
		}
		queue.startTimer();
	}
	
	public boolean getQueue(Player player) {
		return Queue.getInstance().isQueued(player);
	}
	
	public void enterSpectateMode(Player player) {
		player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.VOICE, 100, 1);
		this.leaveJumpnRun(player, false);
		player.setGameMode(GameMode.SPECTATOR);
		PlayerInventory inv = player.getInventory();
		inv.clear();
		player.sendTitle("§cSpectator Mode", "§6open §cAND §6close inventory to exit", 0, 30, 20);
	}
	
	public void enterJumpnRun(Player player) {
		if (this.isJumpnRunning) return;
		this.isJumpnRunning = true;
		player.setAllowFlight(false);
		player.setExp(1.0f);
		player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.VOICE, 100, 1);
	}
	
	public void leaveJumpnRun(Player player, boolean sound) {
		if (!this.isJumpnRunning) return;
		this.isJumpnRunning = false;
		player.setAllowFlight(true);
		player.setExp(0.0f);
		if (sound) player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.VOICE, 100, 0);
	}
	
	public void winJumpnRun(Player player, String color) {
		if (!this.isJumpnRunning) return;
		this.leaveJumpnRun(player, false);
		player.sendTitle("§6You finished", "§6the " + color + " §6run" , 0, 80, 20);
		Bukkit.broadcastMessage("§c" + player.getName() + " §6finished the " + color + " §6run");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.VOICE, 100, 1);
	}
	
	public boolean isJumpnRunning() {
		return this.isJumpnRunning;
	}
	
	public KitMenu getMenu() {
		return this.menu;
	}
	
	public boolean isInLobby() {
		return this.isInLobby;
	}
	
	public String getKitState() {
		String result = "";
		if (this.crtA == null) {
			result += "Kit A ";
		}
		if (this.crtB == null) {
			result += "Kit B";
		}
		if (result.equals("")) {
			result = "nothing";
		}
		return result;
	}
	
//	Stats
	private int killsLast = 0;
	private int killsBest = 0;
	private int killsTotal = 0;
	private int deaths = 0;
	private int winTotal = 0;
	private int winStreak = 0;
	private int winStreakBest = 0;
	private int games = 0;
	private boolean showStats = true;
	
	public void refreshStats(Player player) {
		if (this.killsLast > this.killsBest) this.killsBest = this.killsLast;
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		if (!this.showStats) {
			player.setScoreboard(board);
			return;
		}
		Objective obj = board.registerNewObjective("stats", "dummy", "§6Playing §c§lLiveTwice§r§6:");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		List<Score> scores = new ArrayList<Score>();
		if (Queue.getInstance().isIngame(player)) {
			String kit = KitType.kitToString(this.kitA);
			if (this.crt == 'B') kit = KitType.kitToString(this.kitB);
			String kitB = KitType.kitToString(this.kitB);
			if (!this.crtBAlive) kitB = "§r§l§oDead";
			if (this.crt == 'B') {
				kitB = KitType.kitToString(this.kitA);
				if (!this.crtAAlive) kitB = "§r§l§oDead";
			}
			kit = kit.substring(4);
			kitB = kitB.substring(4);
			int playersIn = Queue.getInstance().playersIn();
			scores.add(obj.getScore("§6Kit:                 §c§l" + kit));
			scores.add(obj.getScore("§6Other Kit:         §c§l" + kitB));
			scores.add(obj.getScore(" "));
			scores.add(obj.getScore("§6Kills:               §c§l" + this.killsLast));
			scores.add(obj.getScore("§6Kills Best:         §c§l" + this.killsBest));
			scores.add(obj.getScore("  "));
			scores.add(obj.getScore("§6There are §c§l" + playersIn + " §r§6left."));
			scores.add(obj.getScore("§7Type /s hide to hide this"));
		}
		else {
			scores.add(obj.getScore("§6§lKills:"));
			scores.add(obj.getScore("  §6Last Game:        §c§l" + this.killsLast));
			scores.add(obj.getScore("  §6Best Game:        §c§l" + this.killsBest));
			scores.add(obj.getScore("  §6Total:              §c§l" + this.killsTotal + " "));
			scores.add(obj.getScore("§6§lDeaths:          §c§l" + this.deaths));
			scores.add(obj.getScore("§6§lWins:"));
			scores.add(obj.getScore("  §6Streak:            §c§l" + this.winStreak));
			scores.add(obj.getScore("  §6Best Streak:     §c§l" + this.winStreakBest));
			scores.add(obj.getScore("  §6Total:              §c§l" + this.winTotal));
			scores.add(obj.getScore("§6§lGames played:  §c§l" + this.games));
			scores.add(obj.getScore(" "));
			scores.add(obj.getScore("§6You are in the §c§lLobby§r§6."));
			scores.add(obj.getScore("§7Type /s hide to hide this"));
		}
		for (int i = 0; i < scores.size(); i++) {
			scores.get(i).setScore(scores.size() - i);
		}
		player.setScoreboard(board);
	}
	
	public void killedStats(Player player) {
		if (Queue.getInstance().isIngame(player)) {
			this.killsLast++;
			this.killsTotal++;
			player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.VOICE, 100, 1);
			player.sendMessage("§c+1 kill");
		}
		this.refreshStats(player);
	}
	
	public void diedStats(Player player) {
		if (Queue.getInstance().isIngame(player)) {
			this.deaths++;
		}
		this.refreshStats(player);
	}
	
	public void winStats(Player player) {
		this.winTotal++;
		this.winStreak++;
		if (this.winStreakBest < this.winStreak) this.winStreakBest = this.winStreak;
		this.refreshStats(player);
	}
	
	public void gameStartsStats(Player player) {
		this.games++;
		this.killsLast = 0;
		this.refreshStats(player);
	}
	
	public void loseStats(Player player) {
		this.winStreak = 0;
	}
	
	public void showStats(Player player) {
		this.showStats = true;
		this.refreshStats(player);
	}
	
	public void hideStats(Player player) {
		this.showStats = false;
		this.refreshStats(player);
	}
}
