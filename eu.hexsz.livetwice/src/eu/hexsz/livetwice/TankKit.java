package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TankKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.NETHERITE_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
		boots = super.setUnbreakable(boots);
		inventory.setItem(EquipmentSlot.FEET, boots);

		ItemStack shield = new ItemStack(Material.SHIELD);
		shield = super.setUnbreakable(shield);
		inventory.setItem(EquipmentSlot.OFF_HAND, shield);
		
		ItemStack sword = new ItemStack(Material.FLINT_AND_STEEL);
		sword = super.setUnbreakable(sword);
		inventory.setItem(0, sword);
		
		inventory.setItem(1, new ItemStack(Material.TNT, 16));
		inventory.setItem(2, new ItemStack(Material.OBSIDIAN, 64));
		
		ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
		pickaxe = super.setUnbreakable(pickaxe);
		inventory.setItem(3, pickaxe);
		
		inventory.setItem(4, new ItemStack(Material.BUCKET));
		inventory.setItem(5, new ItemStack(Material.WATER_BUCKET));
		inventory.setItem(6, new ItemStack(Material.LAVA_BUCKET));
		inventory.setItem(7, new ItemStack(Material.BUCKET));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
