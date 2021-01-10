package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

@Deprecated
public class KitCommand implements TabExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (args.length < 2) {
			return false;
		}
		if (commandSender instanceof Player && args[0].length() == 1) {
			Player player = (Player) commandSender;
			KitType kit;
			switch(args[1]) {
			case "flying_bowman":
				kit = KitType.FLYING_BOWMAN;
				break;
			case "knight":
				kit = KitType.KNIGHT;
				break;
			case "turtle_master":
				kit = KitType.TURTLE_MASTER;
				break;
			case "elf":
				kit = KitType.ELF;
				break;
			case "assassin":
				kit = KitType.ASSASSIN;
				break;
			case "squire":
				kit = KitType.SQUIRE;
				break;
			case "tank":
				kit = KitType.TANK;
				break;
			case "ghost":
				kit = KitType.GHOST;
				break;
			case "giant":
				kit = KitType.GIANT;
				break;
			case "dwarf":
				kit = KitType.DWARF;
				break;
			case "healer":
				kit = KitType.HEALER;
				break;
			case "smart_guy":
				kit = KitType.SMART_GUY;
				break;
			case "builder":
				kit = KitType.BUILDER;
				break;
			case "prince":
				kit = KitType.PRINCE;
				break;
			case "unicorn":
				kit = KitType.UNICORN;
				break;
			default:
				player.sendMessage(args[1] + " is not a kit!");
				return false;
			}
			PlayerAgency.getInstance().get(player).setKit(args[0].charAt(0), kit, player);
			player.sendMessage("Changed kit " + args[0] + " to " + args[1] + ".");
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender commmandSender, Command command, String commandName, String[] args) {
		List<String> result = new ArrayList<String>();
		if (args.length == 1) {
			result.add("a");
			result.add("b");
		}
		if (args.length == 2) {
			result.add("flying_bowman");
			result.add("knight");
			result.add("turtle_master");
			result.add("elf");
			result.add("assassin");
			result.add("squire");
			result.add("tank");
			result.add("ghost");
			result.add("giant");
			result.add("dwarf");
			result.add("healer");
			result.add("smart_guy");
			result.add("builder");
			result.add("prince");
			result.add("unicorn");
		}
		
		return result;
	}
}