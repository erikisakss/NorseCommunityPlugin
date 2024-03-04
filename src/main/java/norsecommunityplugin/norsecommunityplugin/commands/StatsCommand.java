package norsecommunityplugin.norsecommunityplugin.commands;


import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class StatsCommand implements CommandExecutor {

    private PlayerProfileManager playerProfileManager;
    private NorseCommunityPlugin plugin;

    public StatsCommand(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase("stats")) {
            Player targetPlayer = null;

            if (args.length > 0) {
                // Fetches the player by name if specified
                targetPlayer = Bukkit.getPlayerExact(args[0]);
                if (targetPlayer == null) {
                    sender.sendMessage("Player not found.");
                    return true;
                }
            } else if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            // Retrieve player's profile and display stats
            PlayerProfile profile = playerProfileManager.getProfile(targetPlayer.getUniqueId());
            if (profile != null) {
                int xpNeeded = this.plugin.getConfig().getInt("Levels." + profile.getLevel() + ".XP");
                sender.sendMessage("Stats for " + targetPlayer.getName() + ":");
                sender.sendMessage("Level: " + profile.getLevel());
                sender.sendMessage("XP: " + profile.getXP() + "/" + xpNeeded);
                sender.sendMessage("HP: " + profile.getCurrentHP() + "/" + profile.getMaxHP());
                // Add more stats as needed
            } else {
                sender.sendMessage("Could not find stats for " + targetPlayer.getName());
            }
        }
        return true;
    }
}
