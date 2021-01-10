package eu.hexsz.livetwice;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GhostKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword = super.setUnbreakable(sword);
		sword = super.setEnchantet(sword, Enchantment.DAMAGE_ALL, 3);
		inventory.setItem(0, sword);
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 3, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
