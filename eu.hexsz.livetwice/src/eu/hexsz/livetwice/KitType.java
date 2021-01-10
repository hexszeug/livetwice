package eu.hexsz.livetwice;
import java.util.HashMap;

import org.bukkit.Material;

public enum KitType {
	FLYING_BOWMAN, KNIGHT, TURTLE_MASTER, ELF, ASSASSIN, SQUIRE, TANK, GHOST, GIANT, DWARF, HEALER, SMART_GUY, BUILDER, PRINCE, UNICORN, NOTHING;
	
	
	public static HashMap<KitType, String> getKitStringMap() {
		HashMap<KitType, String> kitString = new HashMap<KitType, String>();
		kitString.put(KitType.FLYING_BOWMAN, "§r§lFlying Bowman");
		kitString.put(KitType.ASSASSIN, "§r§lAssassin");
		kitString.put(KitType.BUILDER, "§r§lBuilder");
		kitString.put(KitType.DWARF, "§r§lDwarf");
		kitString.put(KitType.ELF, "§r§lElf");
		kitString.put(KitType.GHOST, "§r§lGhost");
		kitString.put(KitType.GIANT, "§r§lGiant");
		kitString.put(KitType.HEALER, "§r§lHealer");
		kitString.put(KitType.KNIGHT, "§r§lKnight");
		kitString.put(KitType.PRINCE, "§r§lPrince");
		kitString.put(KitType.SMART_GUY, "§r§lSmart Guy");
		kitString.put(KitType.SQUIRE, "§r§lSquire");
		kitString.put(KitType.TANK, "§r§lTank");
		kitString.put(KitType.TURTLE_MASTER, "§r§lTurtle Master");
		kitString.put(KitType.UNICORN, "§r§lUnicorn");
		kitString.put(KitType.NOTHING, "§r§lNothing");
		return kitString;
	}
	
	public static String kitToString(KitType kit) {
		return KitType.getKitStringMap().get(kit);
	}
	
	public static KitType stringToKit(String string) {
		HashMap<KitType, String> map = KitType.getKitStringMap();
		for (KitType key : map.keySet()) {
			if (string == map.get(key)) {
				return key;
			}
		}
		return KitType.NOTHING;
	}
	
	public static Material kitToMaterial(KitType kit) {
		switch(kit) {
		case ASSASSIN: return Material.LEATHER_HELMET;
		case BUILDER: return Material.COBBLESTONE;
		case DWARF: return Material.DIAMOND_PICKAXE;
		case ELF: return Material.BOW;
		case FLYING_BOWMAN: return Material.STICK;
		case GHOST: return Material.WHITE_STAINED_GLASS;
		case GIANT: return Material.TOTEM_OF_UNDYING;
		case HEALER: return Material.POTION;
		case KNIGHT: return Material.IRON_SWORD;
		case PRINCE: return Material.CROSSBOW;
		case SMART_GUY: return Material.REDSTONE;
		case SQUIRE: return Material.SHIELD;
		case TANK: return Material.TNT;
		case TURTLE_MASTER: return Material.TURTLE_HELMET;
		case UNICORN: return Material.END_ROD;
		default:
			return Material.BARRIER;
		}
	}
}
