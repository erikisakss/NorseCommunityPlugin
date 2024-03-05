package norsecommunityplugin.norsecommunityplugin.commands.Testing;

import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthSystem;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHP implements CommandExecutor {

    private PlayerProfileManager playerProfileManager;
    private NorseCommunityPlugin plugin;
    private HealthSystem healthSystem;


    public SetHP(NorseCommunityPlugin plugin) {
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
        this.plugin = plugin;
        this.healthSystem = HealthSystem.getInstance(this.plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /sethp <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        double hp;
        try {
            hp = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid number for HP.");
            return true;
        }

        PlayerProfile profile = playerProfileManager.getProfile(target.getUniqueId());
        profile.setMaxHP(hp);
        healthSystem.healPlayer(target, hp);
        playerProfileManager.updateProfile(profile); // Assuming you have a method to update profiles in PlayerProfileManager.
        sender.sendMessage("Set " + target.getName() + "'s HP to " + hp + ".");
        return true;
    }
}
