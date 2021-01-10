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

public class BuilderKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		helmet = super.setUnbreakable(helmet);
		helmet = super.setColoredArmor(helmet, Color.BLUE);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		helmet = super.setColoredArmor(chestplate, Color.BLUE);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		helmet = super.setColoredArmor(leggins, Color.BLUE);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		helmet = super.setColoredArmor(boots, Color.BLUE);
		inventory.setItem(EquipmentSlot.FEET, boots);

		ItemStack sword = new ItemStack(Material.NETHERITE_PICKAXE);
		sword = super.setUnbreakable(sword);
		sword = super.setEnchantet(sword, Enchantment.KNOCKBACK, 6);
		inventory.setItem(0, sword);
		
		ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
		pickaxe = super.setUnbreakable(pickaxe);
		pickaxe = super.setEnchantet(pickaxe, Enchantment.DIG_SPEED, 10);
		inventory.setItem(1, pickaxe);
		
		inventory.setItem(2, new ItemStack(Material.SPRUCE_PLANKS, 64));
		inventory.setItem(3, new ItemStack(Material.COBBLESTONE, 64));
		inventory.setItem(4, new ItemStack(Material.DIRT, 64));
		inventory.setItem(5, new ItemStack(Material.NETHERRACK, 64));
		inventory.setItem(6, new ItemStack(Material.GRAVEL, 64));
		inventory.setItem(7, new ItemStack(Material.NETHERITE_BLOCK, 64));
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, true, true));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
