package eu.hexsz.livetwice;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QueueCommand implements CommandExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) return false;
		Player player = (Player) commandSender;
		if (!player.isOp()) {
			((Player)commandSender).sendMessage("§cYou are not allowed to use this command!");
			return true;
		}
		if (LiveTwicePlugin.pluginInstance.gameIsRunning) return true;
		int online = Bukkit.getOnlinePlayers().size();
		int queueds = Queue.getInstance().getQueuedsCount();
		int minPlayers = Queue.getInstance().getMinPlayers();
		List<String> text = Queue.getInstance().getNotQueuedsPlusKitState();
		text.add(queueds + " / " + minPlayers + " (min) / " + online + " (on) are in.");
		player.sendMessage(text.toArray(new String[0]));
		return true;
	}
}
