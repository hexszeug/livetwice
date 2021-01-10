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

public class TurtleMasterKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.TURTLE_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		boots = super.setUnbreakable(boots);
		boots = super.setColoredArmor(boots, Color.AQUA);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack trident = new ItemStack(Material.TRIDENT);
		trident = super.setUnbreakable(trident);
		trident = super.setEnchantet(trident, Enchantment.LOYALTY, 3);
		inventory.setItem(0, trident);

		ItemStack trident2 = new ItemStack(Material.TRIDENT);
		trident2 = super.setUnbreakable(trident2);
		trident2 = super.setEnchantet(trident2, Enchantment.RIPTIDE, 3);
		inventory.setItem(1, trident2);
		
		inventory.setItem(7, new ItemStack(Material.SCUTE));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONDUIT_POWER, Integer.MAX_VALUE, 1, false, true, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1, false, true, true));
	}
	
	public void save(Player player) {
		//here save custom items
		super.save(player);
	}
}
