package eu.hexsz.livetwice;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class UnicornKit extends AbstractKit {
	
	private BukkitRunnable hornAttackCooldownRunnable;
	private int hornAttackCooldown;
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.END_ROD);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		chestplate = super.setColoredArmor(chestplate, Color.WHITE);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		leggins = super.setColoredArmor(leggins, Color.WHITE);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		boots = super.setColoredArmor(boots, Color.WHITE);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		inventory.setItem(0, new ItemStack(Material.RED_DYE));
		inventory.setItem(1, new ItemStack(Material.ORANGE_DYE));
		inventory.setItem(2, new ItemStack(Material.PINK_DYE));
		inventory.setItem(3, new ItemStack(Material.YELLOW_DYE));
		inventory.setItem(4, new ItemStack(Material.LIME_DYE));
		inventory.setItem(5, new ItemStack(Material.GREEN_DYE));
		inventory.setItem(6, new ItemStack(Material.BLUE_DYE));
		inventory.setItem(7, new ItemStack(Material.PURPLE_DYE));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		if (this.hornAttackCooldownRunnable != null) {
			this.hornAttackCooldownRunnable.cancel();
		}
		this.hornAttackCooldown = 0;
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 8, false, false, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, false, false, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false, true));
	}
	
	public void save(Player player) {
		if (this.hornAttackCooldownRunnable != null) this.hornAttackCooldownRunnable.cancel();
		super.save(player);
	}
	
	public void hornAttack(Player player, LivingEntity entity) {
		if (this.hornAttackCooldown > 0) {
			if (this.hornAttackCooldown != 5) {
				player.sendMessage("You have to wait " + (this.hornAttackCooldown ) + " more Seconds to do this!");
			}
			return;
		}
		Location playerLoc = player.getLocation();
		Location entityLoc = entity.getLocation();
		entityLoc.setDirection(playerLoc.getDirection());
		player.teleport(entityLoc);
		entity.damage(9, player);
		this.hornAttackCooldown = 5;

		if (this.hornAttackCooldownRunnable != null) this.hornAttackCooldownRunnable.cancel();
		this.hornAttackCooldownRunnable = new BukkitRunnable() {
			private Player player;
			private UnicornKit crt;
			
			public BukkitRunnable setData(Player player, UnicornKit crt) {
				this.player = player;
				this.crt = crt;
				return this;
			}
			
			@Override
			public void run() {
				crt.countHornAttackCooldown(this.player);
			}
		}.setData(player, this);
		this.hornAttackCooldownRunnable.runTaskTimer(LiveTwicePlugin.pluginInstance, 0, 20);	
	}
	
	public void countHornAttackCooldown(Player player) {
		if (this.hornAttackCooldown <= 0) return;
		this.hornAttackCooldown--;
		player.sendTitle("", "" + this.hornAttackCooldown, 0, 25, 0);
	}
	
	public void delete() {
		if (this.hornAttackCooldownRunnable == null) return;
		this.hornAttackCooldownRunnable.cancel();
	}
}
