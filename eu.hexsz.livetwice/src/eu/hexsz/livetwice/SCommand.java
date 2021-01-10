package eu.hexsz.livetwice;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class SCommand implements TabExecutor{

	public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
		if (!(commandSender instanceof Player)) {
			return true;
		}
		Player player = (Player) commandSender;
		if (args.length < 1) {
			if (!player.isOp()) {
				return false;
			}
			if (LiveTwicePlugin.pluginInstance.gameIsRunning) return true;
			if (Queue.getInstance().getQueuedsCount() < Queue.getInstance().getMinPlayers()) {
				player.sendMessage("There are not enought players in the queue.");
				(new QueueCommand()).onCommand(commandSender, command, commandName, args);
				return true;
			}
			Queue queue = Queue.getInstance();
			if (queue.maploadRunnable != null) if (!queue.maploadRunnable.isCancelled()) {
				player.sendMessage("Please wait till the new map is loaded!");
				return true;
			}
			queue.startGame();
			return true;
		}
		PlayerAgent agent = PlayerAgency.getInstance().get(player);
		if (args[0].equalsIgnoreCase("hide")) agent.hideStats(player);
		else if (args[0].equalsIgnoreCase("show")) agent.showStats(player);
		else if (args[0].equalsIgnoreCase("reset")) {
			PlayerAgency.getInstance().remove(player);
			player.kickPlayer("§6§lPlease rejoin after reseting your stats!");
		}
		else {
			return false;
		}
		return true;
		
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String commandName, String[] args) {
		List<String> result = new ArrayList<String>();
		if (args.length == 1) {
			result.add("hide");
			result.add("show");
			result.add("reset");
		}
		return result;
	}
}
