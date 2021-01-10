package eu.hexsz.livetwice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class MenuCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			PlayerAgency.getInstance().get(player).openMenu(player);
		}
		return true;
	}
}
