package eu.hexsz.livetwice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class AliveCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (commandSender instanceof Player) {
			Player player = (Player) commandSender;
			if (PlayerAgency.getInstance().has(player)) {
				PlayerAgency.getInstance().get(player).reliveCrts(player);
				return true;
			}
		}
		return false;
	}

}
