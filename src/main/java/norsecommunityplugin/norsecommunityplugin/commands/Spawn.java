package norsecommunityplugin.norsecommunityplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class Spawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase("spawn"))
        {
            if (sender instanceof Player)
            {
                //Getting the player from the sender and casting it to a player object
                Player player = (Player) sender;
                //Teleporting the player to spawn and sending a message to the player that they have been teleported to spawn
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage("You have been teleported to spawn!");
            }
        }
        return true;
    }
}
