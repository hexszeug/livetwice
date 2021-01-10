package eu.hexsz.livetwice;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DwarfKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		boots = super.setColoredArmor(boots, Color.BLACK);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack shield = new ItemStack(Material.SHIELD);
		shield = super.setUnbreakable(shield);
		inventory.setItem(EquipmentSlot.OFF_HAND, shield);
		
		ItemStack axe = new ItemStack(Material.DIAMOND_AXE);
		axe = super.setUnbreakable(axe);
		inventory.setItem(0, axe);
		

		ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
		pickaxe = super.setUnbreakable(pickaxe);
		inventory.setItem(4, pickaxe);
		
		inventory.setItem(1, new ItemStack(Material.DIAMOND));
		inventory.setItem(2, new ItemStack(Material.EMERALD));
		inventory.setItem(3, new ItemStack(Material.LAPIS_LAZULI));
		
		inventory.setItem(7, new ItemStack(Material.WATER_BUCKET));
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
