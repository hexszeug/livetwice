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

public class SmartGuyKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);

		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.ELYTRA);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		boots = super.setColoredArmor(boots, Color.RED);
		inventory.setItem(EquipmentSlot.FEET, boots);

		ItemStack offhand = new ItemStack(Material.FISHING_ROD);
		offhand = super.setUnbreakable(offhand);
		inventory.setItem(EquipmentSlot.OFF_HAND, offhand);
		
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword = super.setUnbreakable(sword);
		inventory.setItem(0, sword);
		
		ItemStack axe = new ItemStack(Material.STONE_AXE);
		axe = super.setUnbreakable(axe);
		inventory.setItem(2, axe);

		inventory.setItem(1, new ItemStack(Material.FIREWORK_ROCKET, 64));
		inventory.setItem(3, new ItemStack(Material.BLAZE_SPAWN_EGG, 8));
		inventory.setItem(4, new ItemStack(Material.CREEPER_SPAWN_EGG, 16));
		inventory.setItem(5, new ItemStack(Material.SPRUCE_DOOR, 64));
		inventory.setItem(6, new ItemStack(Material.RED_WOOL, 64));
		inventory.setItem(7, new ItemStack(Material.SLIME_BLOCK, 64));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
