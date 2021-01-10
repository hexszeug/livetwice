package eu.hexsz.livetwice;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Structure;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class LTMap {
	
	private String name;
	private String file;
	private String author;
	private Location pos;
	private Logger log;
	
	public LTMap(String name, String fileName, String author, Location position, Logger logger) {
		this.name = name;
		this.file = fileName;
		this.author = author;
		this.pos = position.clone();
		this.log = logger;
	}
	
	public boolean copyFileIntoWorld() {
		File dir = new File("./world/generated/minecraft/structures/");
		InputStream is = getClass().getClassLoader().getResourceAsStream(this.file);
		this.log.info("Copying map \"" + this.name + "\" (" + this.file + ") (made by " + this.author + ") from liveTwice.jar into \"" + dir.getPath() + "\"...");
		if (is == null) {
			this.log.warning(this.file + " does not exist. Ignoring it.");
			return false;
		}
		if (!dir.exists()) {
			this.log.info("Destination directory does not exist. Creating it.");
			dir.mkdirs();
		}
		try {
			Files.copy(is, Paths.get(dir.getPath() + "/" + this.file), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			this.log.warning("Failed.");
			return false;
		}
		this.log.info("Sucessfully copied.");
		return true;
	}
	
	public void load() {
		this.log.info("Loading Map " + this.name + "...");
		String structureName = this.file.substring(0, this.file.length()-4);
		Location loc = this.pos.clone();
		loc.add(0, -1, 0);
		Block structureBlock = loc.getBlock();
		Material structureOldBlock = structureBlock.getType();
		structureBlock.setType(Material.STRUCTURE_BLOCK);
		Structure structureMeta = (Structure) structureBlock.getState();
		structureMeta.setUsageMode(UsageMode.LOAD);
		structureMeta.setIgnoreEntities(false);
		structureMeta.setIntegrity(1.0f);
		structureMeta.setStructureName(structureName);
		structureMeta.setBoundingBoxVisible(false);
		structureMeta.update();
		loc.add(1, 0, 0);
		Block redstoneBlock = loc.getBlock();
		Material redstoneOldBlock = redstoneBlock.getType();
		redstoneBlock.setType(Material.REDSTONE_BLOCK);
		structureBlock.setType(structureOldBlock);
		redstoneBlock.setType(redstoneOldBlock);
		this.log.info("Finished.");
	}
	
	public void loadWithCredits() {
		this.load();
		World world = ObjectsFactory.getWorld();
		Location loc = this.pos.clone().add(24, 48.5, 21);
		double distance = 0.25;
		ArmorStand map = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
		this.settingsArmorstand(map, "§r§6§lMap");
		ArmorStand name = (ArmorStand) world.spawnEntity(loc.add(0, -distance, 0), EntityType.ARMOR_STAND);
		this.settingsArmorstand(name, "§r§c§l" + this.name);
		ArmorStand builders = (ArmorStand) world.spawnEntity(loc.add(0, -distance, 0), EntityType.ARMOR_STAND);
		this.settingsArmorstand(builders, "§r§6Builder(s)");
		ArmorStand authors = (ArmorStand) world.spawnEntity(loc.add(0, -distance, 0), EntityType.ARMOR_STAND);
		this.settingsArmorstand(authors, "§r§c" + this.author);
	}
	
	private void settingsArmorstand(ArmorStand as, String name) {
		as.setCustomNameVisible(true);
		as.setCustomName(name);
		as.setInvulnerable(true);
		as.setGravity(false);
		as.setInvisible(true);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
}
