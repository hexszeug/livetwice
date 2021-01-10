package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SquireKit extends AbstractKit {

	public void refill(Location position, Player player) {
		super.refill(position, player);
		
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		inventory.setItem(EquipmentSlot.HEAD, new ItemStack(Material.CHAINMAIL_HELMET));
		inventory.setItem(EquipmentSlot.CHEST, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		inventory.setItem(EquipmentSlot.LEGS, new ItemStack(Material.CHAINMAIL_LEGGINGS));
		inventory.setItem(EquipmentSlot.FEET, new ItemStack(Material.CHAINMAIL_BOOTS));
		
		inventory.setItem(EquipmentSlot.OFF_HAND, new ItemStack(Material.SHIELD));
		
		inventory.setItem(0, new ItemStack(Material.IRON_SWORD));
		inventory.setItem(1, new ItemStack(Material.STONE_AXE));
		inventory.setItem(7, new ItemStack(Material.POPPY));
		
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
