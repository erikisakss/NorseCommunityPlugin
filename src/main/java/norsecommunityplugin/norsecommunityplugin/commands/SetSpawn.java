package norsecommunityplugin.norsecommunityplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class SetSpawn implements CommandExecutor{

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (command.getName().equalsIgnoreCase("setspawn"))
            {
                if (sender instanceof Player)
                {
                    //Getting the player from the sender and casting it to a player object
                    Player player = (Player) sender;
                    Location location = player.getLocation();
                    //Setting the spawn location to the players location and sending a message to the player that the spawn has been set

                    player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());

                    location.setYaw(player.getLocation().getYaw());
                    location.setPitch(player.getLocation().getPitch());
                    player.getWorld().setSpawnLocation(location);
                    //player should face the same direction as they were facing when they set the spawn


                    player.sendMessage("Spawn has been set!");
                }
            }
            return true;
        }

}
