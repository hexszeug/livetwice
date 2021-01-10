package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class MapCommand implements TabExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if(commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (!player.isOp()) {
				player.sendMessage("§cYou are not allowed to perform this command.");
				return false;
			}
		}
		MapLoader mapLoader = MapLoader.getInstance();
		String mapName = "";
		for(int i = 0; i < args.length; i++) {
			mapName += args[i];
			if (i < args.length - 1) mapName += " ";
		}
		mapLoader.loadMap(mapName);
		return true;
	}
	
	public List<String> onTabComplete(CommandSender commmandSender, Command command, String commandName, String[] args) {
		List<String> mapNames = new ArrayList<String>();
		if (args.length <= 0) {
			return mapNames;
		}
		mapNames = MapLoader.getInstance().getMapNameList();
		String input = "";
		for(int i = 0; i < args.length; i++) {
			input += args[i];
			if (i < args.length - 1) input += " ";
		}
		if (input.length() <= 0) {
			return mapNames;
		}
		List<String> result = new ArrayList<String>();
		for (String mapName : mapNames) {
			if (input.length() <= mapName.length()) {
				if (input.equalsIgnoreCase(mapName.substring(0, input.length()))) {
					String[] splitted = mapName.split(" ");
					String suggestion = "";
					for(int i = 0; i < splitted.length; i++) {
						if (i > args.length - 2) {
							suggestion += splitted[i];
							if (i < splitted.length - 1) {
								suggestion += " ";
							}
						}
					}
					result.add(suggestion);
				}
			}
		}
		return result;
	}
}
