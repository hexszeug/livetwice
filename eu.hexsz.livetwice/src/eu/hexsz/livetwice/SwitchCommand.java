package eu.hexsz.livetwice;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Deprecated
public class SwitchCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (commandSender instanceof Player) {
			Bukkit.broadcastMessage("Jemand hat per command geswitcht");
			Player player = (Player) commandSender;
			PlayerAgency.getInstance().get(player).switchCrt(player, false);
		}
		return true;
	}
}
