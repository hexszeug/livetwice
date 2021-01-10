package eu.hexsz.livetwice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class SwitcherCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			player.getInventory().setItem(8, ObjectsFactory.getSwitcher());
		}
		return true;
	}
}
