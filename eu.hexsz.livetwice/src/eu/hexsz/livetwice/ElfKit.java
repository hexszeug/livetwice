package eu.hexsz.livetwice;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ElfKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		helmet = super.setUnbreakable(helmet);
		helmet = super.setColoredArmor(helmet, Color.GREEN);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		chestplate = super.setColoredArmor(chestplate, Color.GREEN);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		leggins = super.setColoredArmor(leggins, Color.GREEN);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		boots = super.setColoredArmor(boots, Color.GREEN);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack bread = new ItemStack(Material.BREAD);
		bread = super.setUnbreakable(bread);
		bread = setEnchantet(bread, Enchantment.DURABILITY, 3);
		bread = super.setNamed(bread, "Lembas");
		inventory.setItem(EquipmentSlot.OFF_HAND, bread);

		ItemStack bow = new ItemStack(Material.BOW);
		bow = super.setUnbreakable(bow);
		bow = super.setEnchantet(bow, Enchantment.ARROW_INFINITE, 1);
		inventory.setItem(0, bow);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword = super.setUnbreakable(sword);
		inventory.setItem(1, sword);
		
		inventory.setItem(3, new ItemStack(Material.WRITTEN_BOOK));
		
		ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 16);
		arrow = super.setPotion(arrow, PotionType.POISON);
		inventory.setItem(6, arrow);
		inventory.setItem(7, new ItemStack(Material.ARROW, 1));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
