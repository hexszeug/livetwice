package eu.hexsz.livetwice;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public abstract class AbstractKit {
	private double health = 20;
	private Location position = ObjectsFactory.getIngameLoc();
	protected ItemStack[] inv;
	
	public void addHealth(int amount) {
		this.health += amount;
		if (this.health > 20) this.health = 20;
	}
	
	public void refill(Location position, Player player) {
		this.health = 20;
		this.position = position;
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(8, ObjectsFactory.getSwitcher());
		this.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		player.setHealth(this.health);
		player.setGameMode(GameMode.SURVIVAL);
		player.teleport(position);
		player.getInventory().setContents(this.inv);
//		PlayerInventory inventory = player.getInventory();
//		inventory.setItem(8, ObjectsFactory.getSwitcher());
		player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 255, false, false, false));
	}
	
	public void save(Player player) {
		this.health = player.getHealth();
		this.position = player.getLocation();
		PlayerInventory inventory = player.getInventory();
		this.inv = inventory.getContents();
		inventory.clear();
		for (Iterator<PotionEffect> i = player.getActivePotionEffects().iterator(); i.hasNext();) {
			player.removePotionEffect(i.next().getType());
		}
	}
	
	protected ItemStack setUnbreakable(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}
	
	protected ItemStack setEnchantet(ItemStack item, Enchantment enchantment, int amount) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(enchantment, amount, true);
		item.setItemMeta(meta);
		return item;
	}
	
	protected ItemStack setNamed(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	protected ItemStack setColoredArmor(ItemStack item, Color color) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			LeatherArmorMeta leatherMeta = (LeatherArmorMeta) meta;
			leatherMeta.setColor(color);
			meta = (ItemMeta) leatherMeta;
			item.setItemMeta(meta);
		}
		return item;
	}
	
	protected ItemStack setPotion(ItemStack item, PotionType type) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) meta;
			potionMeta.setBasePotionData(new PotionData(type));
			item.setItemMeta((ItemMeta) potionMeta);
		}
		return item;
	}
	
	protected int getAmountOf(PlayerInventory inventory, Material material) {
		HashMap<Integer, ? extends ItemStack> allStacks = inventory.all(material);
		Collection<? extends ItemStack> allAmounts = allStacks.values();
		int result = 0;
		for (Iterator<? extends ItemStack> i = allAmounts.iterator(); i.hasNext();) {
			result += i.next().getAmount();
		}
		return result;
	}
}
