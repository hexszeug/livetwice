package eu.hexsz.livetwice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;
		player.sendMessage("§6------LiveTwice Info------");
		player.sendMessage("§6 LiveTwice is a PvP minigame.");
		player.sendMessage("§6 You can play it here.");
		return true;
	}
}
