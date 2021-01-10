package eu.hexsz.livetwice;

import org.bukkit.Location;
//import org.bukkit.Material;
import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GiantKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 6, false, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false, false));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
