package norsecommunityplugin.norsecommunityplugin.commands.Testing;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetProtection implements CommandExecutor {

    private PlayerProfileManager playerProfileManager;
    private NorseCommunityPlugin plugin;

    public SetProtection(NorseCommunityPlugin plugin) {
        this.playerProfileManager = norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager.getInstance(this.plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /setprotection <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        int protection;
        try {
            protection = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number for Damage.");
            return true;
        }

        PlayerProfile profile = playerProfileManager.getOrCreateProfile(target);
        profile.setProtection(protection);
        playerProfileManager.updateProfile(profile); // Assuming you have a method to update profiles in PlayerProfileManager.
        sender.sendMessage("Set " + target.getName() + "'s Protection to " + protection + ".");
        return true;
    }
}
