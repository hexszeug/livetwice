package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KnightKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.IRON_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		boots = super.setUnbreakable(boots);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword = super.setUnbreakable(sword);
		inventory.setItem(0, sword);
		
		ItemStack axe = new ItemStack(Material.IRON_AXE);
		axe = super.setUnbreakable(axe);
		inventory.setItem(2, axe);
		
		inventory.setItem(4, new ItemStack(Material.POPPY));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 1, false, false, true));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
