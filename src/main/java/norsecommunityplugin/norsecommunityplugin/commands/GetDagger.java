package norsecommunityplugin.norsecommunityplugin.commands;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.weapons.LokiDagger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetDagger implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("dagger")) {
            player.getInventory().addItem(LokiDagger.createLokiDagger());
            player.sendMessage("You have received Loki's Dagger!");
            return true;
        }

        return false;
    }
}
