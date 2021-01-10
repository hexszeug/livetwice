package eu.hexsz.livetwice;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PrinceKit extends AbstractKit {
	
	public void refill(Location position, Player player) {
		super.refill(position, player);
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(super.inv);
		
		ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
		helmet = super.setUnbreakable(helmet);
		inventory.setItem(EquipmentSlot.HEAD, helmet);
		
		ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
		chestplate = super.setUnbreakable(chestplate);
		inventory.setItem(EquipmentSlot.CHEST, chestplate);
		
		ItemStack leggins = new ItemStack(Material.GOLDEN_LEGGINGS);
		leggins = super.setUnbreakable(leggins);
		inventory.setItem(EquipmentSlot.LEGS, leggins);
		
		ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
		boots = super.setUnbreakable(boots);
		inventory.setItem(EquipmentSlot.FEET, boots);
		
		ItemStack chargedCrossbow = new ItemStack(Material.CROSSBOW);
		chargedCrossbow = super.setUnbreakable(chargedCrossbow);
		CrossbowMeta meta = (CrossbowMeta) chargedCrossbow.getItemMeta();
		meta.addChargedProjectile(new ItemStack(Material.ARROW));
		chargedCrossbow.setItemMeta((ItemMeta) meta);
		
		ItemStack crossbow = new ItemStack(Material.CROSSBOW);
		crossbow = super.setUnbreakable(crossbow);
		
		inventory.setItem(0, crossbow);
		inventory.setItem(1, crossbow);
		inventory.setItem(2, crossbow);
		inventory.setItem(3, crossbow);
		inventory.setItem(4, crossbow);
		inventory.setItem(5, crossbow);
		inventory.setItem(6, crossbow);
		inventory.setItem(7, crossbow);
		
		inventory.setItem(0, chargedCrossbow);
		inventory.setItem(1, chargedCrossbow);
		inventory.setItem(2, chargedCrossbow);
		inventory.setItem(3, chargedCrossbow);
		inventory.setItem(4, chargedCrossbow);
		inventory.setItem(5, chargedCrossbow);
		inventory.setItem(6, chargedCrossbow);
		inventory.setItem(7, chargedCrossbow);
		
		inventory.setItem(9, new ItemStack(Material.ARROW, 64));
		inventory.setItem(10, new ItemStack(Material.ARROW, 64));
		inventory.setItem(11, new ItemStack(Material.ARROW, 64));
		inventory.setItem(12, new ItemStack(Material.ARROW, 64));
		
		super.inv = inventory.getContents();
		inventory.clear();
	}
	
	public void equip(Player player) {
		super.equip(player);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, Integer.MAX_VALUE, 1, false, false, true));
	}
	
	public void save(Player player) {
		super.save(player);
	}
}
