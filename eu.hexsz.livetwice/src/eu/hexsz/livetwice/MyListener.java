package eu.hexsz.livetwice;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MyListener implements Listener{
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.OFF_HAND) return;
		Player player = event.getPlayer();
		Action action = event.getAction();
		if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.RIGHT_CLICK_BLOCK)) return;
		ItemStack item = event.getItem();
		if (item == null) return;
		PlayerAgent agent = PlayerAgency.getInstance().get(player);
		if (item.equals(agent.getMenu().getOpener())) {
			event.setCancelled(true);
			agent.openMenu(player);
		}
		if (item.equals(ObjectsFactory.getSwitcher())) {
			if (agent.switchCrt(player, true)) {
				agent.startCooldown(player);
			}
			event.setCancelled(true);
		}
		if (item.equals(HealerKit.getHealerItem())) {
			if (agent.isSwitchable(player)) {
				agent.getCrt(false).addHealth(4);
				agent.switchCrt(player, false);
				agent.startCooldown(player);
			}
			event.setCancelled(true);
		}
		if (item.equals(ObjectsFactory.getStarter(agent.getQueue(player)))) {
			agent.joinLeaveQueue(player);
			event.setCancelled(true);
		}
		
		if (item.equals(ObjectsFactory.getKitPrewievQuitter())) {
			agent.enterLobby(player);
			event.setCancelled(true);
		}
		
		if (item.equals(ObjectsFactory.getSpectate())) {
			agent.enterSpectateMode(player);
			event.setCancelled(true);
		}
		if (agent.isInLobby()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!PlayerAgency.getInstance().has(player)) {
			PlayerAgency.getInstance().add(player);
		}
		PlayerAgency.getInstance().get(player).enterLobby(player);
		Queue.getInstance().hideShowPlayers();
		String msg = "§6§o" + event.getJoinMessage().substring(2) + ". Welcome " + player.getName() +"!";
		event.setJoinMessage(msg);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Queue.getInstance().isIngame(player)) {
			PlayerAgency.getInstance().get(player).loseStats(player);
		}
		Queue.getInstance().playerQuit(player);
		String msg = "§6§o" + event.getQuitMessage().substring(2) + ". He thought he must.";
		event.setQuitMessage(msg);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (event instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent eventByEntity = (EntityDamageByEntityEvent) event;
				if (eventByEntity.getDamager() instanceof Player) {
					Player damager = (Player) eventByEntity.getDamager();
					if (!Queue.getInstance().isIngame(damager)) event.setCancelled(true);
				}
				if (Queue.getInstance().isHideStart()) event.setCancelled(true);
			}
			if (PlayerAgency.getInstance().get(player).isInLobby()) event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if ((player.getKiller() instanceof Player)) {
			Player killer = player.getKiller();
			if (!killer.getUniqueId().equals(player.getUniqueId())) {
				PlayerAgent killerAgent = PlayerAgency.getInstance().get(killer);
				killerAgent.killedStats(killer);
			}
		}
		String deathMsg = "§6§l" + event.getDeathMessage() + ". He thought he must!";
		PlayerAgent agent = PlayerAgency.getInstance().get(player);
		agent.died(player);
		agent.diedStats(player);
		deathMsg = deathMsg.replaceAll(player.getName(), player.getDisplayName());
		event.setDeathMessage(deathMsg);
		event.setKeepLevel(false);
		BukkitRunnable runnable = new BukkitRunnable() {

			@Override
			public void run() {
				Queue.getInstance().wins();
				this.cancel();
			}
			
		};
		runnable.runTask(LiveTwicePlugin.pluginInstance);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.getGameMode() != GameMode.SPECTATOR) return;
		PlayerAgency.getInstance().get(player).enterLobby(player);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location loc = event.getTo();
		Player player = event.getPlayer();
		int lobbyY = ObjectsFactory.getLobbyLoc().getBlockY();
		if (!PlayerAgency.getInstance().get(player).isInLobby()) return;
		Material blockUnder = loc.clone().add(0, -1, 0).getBlock().getType();
		if (loc.getY() > lobbyY) {
			PlayerAgent agent = PlayerAgency.getInstance().get(player);
			if (blockUnder == Material.WARPED_PLANKS || blockUnder == Material.CRIMSON_PLANKS) {
				if(loc.getY() < lobbyY + 2) {
					agent.enterJumpnRun(player);
				}
			}
			if (blockUnder == Material.BARRIER) {
				agent.leaveJumpnRun(player, true);
			}
			if (blockUnder == Material.REDSTONE_BLOCK) {
				agent.winJumpnRun(player, "§4red");
			}
			if (blockUnder == Material.EMERALD_BLOCK) {
				agent.winJumpnRun(player, "§2green");
			}
		}
		if (player.getGameMode().equals(GameMode.SPECTATOR) && !player.isOp()) {
			if (loc.distance(ObjectsFactory.getLobbyLoc()) > 150) {
				player.teleport(ObjectsFactory.getLobbyLoc());
				player.sendMessage("§cPlease dont fly to far away!");
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		AbstractKit crt = PlayerAgency.getInstance().get(player).getCrt(true);
		if (!(crt instanceof UnicornKit)) return;
		if (!(entity instanceof LivingEntity)) return;
		LivingEntity livingEntity = (LivingEntity) entity;
		UnicornKit unicornCrt = (UnicornKit) crt;
		unicornCrt.hornAttack(player, livingEntity);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
}
