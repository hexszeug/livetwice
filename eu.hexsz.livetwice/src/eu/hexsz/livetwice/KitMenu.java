package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class KitMenu implements Listener {
	
	private Inventory inventory;
	
	public void open(Player player, int state, KitType kitA, KitType kitB) {
		this.inventory = this.getMenu(state, kitA, kitB);
		player.openInventory(this.inventory);
		
	}
	
	private Inventory getMenu(int state, KitType kitA, KitType kitB) {
		Inventory inventory = Bukkit.createInventory(null, 45, "Kit Menu");
		int i = 1;
		for (KitType kit : KitType.values()) {
			List<String> lore = new ArrayList<String>();
			if (kit != KitType.NOTHING) {
				if (kitA == kit) {
					lore.add("§r§l§dSelected as kit §cA!");
				}
				else {
					lore.add("§r§l§fClick to select this as kit §cA!");
				}
				lore.add("§r§l§fRightclick to preview!");
			}
			else {
				if (kitA == kit) {
					lore.add("§r§l§dSelected as kit §cA! §dPlease change that!");
				}
			}
			inventory.setItem(i, this.giveItem(kit, "A", kitA == kit, 1, lore));
			if (i == 8) {
				i = 9;
			}
			i++;
		}
		i = 28;
		for (KitType kit : KitType.values()) {
			List<String> lore = new ArrayList<String>();
			if (kit != KitType.NOTHING) {
				if (kitB == kit) {
					lore.add("§r§l§dSelected as kit §cB!");
				}
				else {
					lore.add("§r§l§fClick to select this as kit §cB!");
				}
				lore.add("§r§l§fRightclick to preview!");
			}
			else {
				if (kitB == kit) {
					lore.add("§r§l§dSelected as kit §cB! §dPlease change that!");
				}
			}
			inventory.setItem(i, this.giveItem(kit, "B", kitB == kit, 1, lore));
			if (i == 35) {
				i = 36;
			}
			i++;
		}
		return inventory;
	}
	
	private ItemStack giveItem(KitType kit, String kitId, boolean choosen, Integer amount, List<String> lore) {
		Material material = KitType.kitToMaterial(kit);
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		String color = "§f";
		if (choosen) {
			meta.addEnchant(Enchantment.MENDING, 1, true);
			color = "§d";
		}
		String name = KitType.kitToString(kit);
		meta.setDisplayName(color + name);
		PersistentDataContainer data = meta.getPersistentDataContainer();
		data.set(this.getIdKey(), PersistentDataType.STRING, kitId);
		data.set(this.getKitKey(), PersistentDataType.STRING, KitType.kitToString(kit));
		item.setItemMeta(meta);
		return item;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() != this.inventory) return;
		event.setCancelled(true);
		ItemStack item = event.getCurrentItem();
		if (item == null || item.getType() == Material.AIR) return;
		Player player = (Player) event.getWhoClicked();
		ItemMeta meta = item.getItemMeta();
		PersistentDataContainer data = meta.getPersistentDataContainer();
		if (!data.has(this.getIdKey(), PersistentDataType.STRING) || !data.has(this.getKitKey(), PersistentDataType.STRING)) return;
		KitType kit = KitType.stringToKit(data.get(this.getKitKey(), PersistentDataType.STRING));
		PlayerAgent agent = PlayerAgency.getInstance().get(player);
		if (event.isLeftClick()) {
			agent.setKit(data.get(this.getIdKey(), PersistentDataType.STRING).charAt(0), kit, player);
			agent.openMenu(player);;
		}
		if (event.isRightClick()) {
			if (item.getType() == Material.BARRIER) return;
			if (item.getType() == KitType.kitToMaterial(KitType.UNICORN)) {
				player.sendMessage("§cCannot preview Unicorn. Ingame use is fine.");
				return;
			}
			player.getInventory().clear();
			AbstractKit kitObj = agent.createCrtByKit(kit);
			kitObj.refill(ObjectsFactory.getLobbyLoc(), player);
			kitObj.equip(player);
			player.setGameMode(GameMode.ADVENTURE);
			player.setAllowFlight(true);
			player.getInventory().setItem(8, ObjectsFactory.getKitPrewievQuitter());
		}
	}
	
	public ItemStack getOpener() {
		ItemStack item = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		meta.getPersistentDataContainer().set(KitMenu.getOpenKey(), PersistentDataType.STRING, "1b");
		meta.setDisplayName("§r§l§7Open Kit Menu");
		meta.setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}
	
	private NamespacedKey getIdKey() {
		return new NamespacedKey(LiveTwicePlugin.pluginInstance, "kitId");
	}
	
	private NamespacedKey getKitKey() {
		return new NamespacedKey(LiveTwicePlugin.pluginInstance, "kit");
	}
	
	public static NamespacedKey getOpenKey() {
		return new NamespacedKey(LiveTwicePlugin.pluginInstance, "kitMenu");
	}
}
