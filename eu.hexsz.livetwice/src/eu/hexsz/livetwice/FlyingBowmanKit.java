package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlyingBowmanKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack elytra = new ItemStack(Material.ELYTRA);
		elytra = super.setUnbreakable(elytra);
		inventory.setItem(EquipmentSlot.CHEST, elytra);
		
		ItemStack bow = new ItemStack(Material.BOW);
		bow = super.setUnbreakable(bow);
		inventory.setItem(0, bow);
		
		ItemStack stick = new ItemStack(Material.STICK);
		stick = super.setEnchantet(stick, Enchantment.KNOCKBACK, 5);
		inventory.setItem(2, stick);
			
		inventory.setItem(5, new ItemStack(Material.ARROW, 64));
		inventory.setItem(6, new ItemStack(Material.ARROW, 64));
		
		inventory.setItem(7, new ItemStack(Material.FIREWORK_ROCKET, 30));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
