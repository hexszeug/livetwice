package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class AssassinKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword = super.setUnbreakable(sword);
		inventory.setItem(0, sword);
		
		ItemStack potion = new ItemStack(Material.POTION);
		potion = super.setPotion(potion, PotionType.INVISIBILITY);
		inventory.setItem(2, potion);
		inventory.setItem(3, potion);
		inventory.setItem(4, potion);
		inventory.setItem(5, potion);
		inventory.setItem(6, potion);
		inventory.setItem(7, potion);
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 4, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
