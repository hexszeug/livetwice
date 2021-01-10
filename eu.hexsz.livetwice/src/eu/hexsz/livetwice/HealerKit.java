package eu.hexsz.livetwice;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealerKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		helmet = super.setUnbreakable(helmet);
		helmet = super.setColoredArmor(helmet, Color.YELLOW);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		helmet = super.setColoredArmor(chestplate, Color.YELLOW);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		helmet = super.setColoredArmor(leggins, Color.YELLOW);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		helmet = super.setColoredArmor(boots, Color.YELLOW);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack star = HealerKit.getHealerItem();
		inventory.setItem(0, star);
		inventory.setItem(1, star);
		inventory.setItem(2, star);
		inventory.setItem(3, star);
		inventory.setItem(4, star);
		inventory.setItem(5, star);
		inventory.setItem(6, star);
		inventory.setItem(7, star);
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
	
	public static ItemStack getHealerItem() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("HEAL OTHER CRT");
		PersistentDataContainer customNBTData = meta.getPersistentDataContainer();
		customNBTData.set(new NamespacedKey(LiveTwicePlugin.pluginInstance, "healer"), PersistentDataType.STRING, "1b");
		item.setItemMeta(meta);
		return item;
	}
}
