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

public class SetClass implements CommandExecutor {

    private PlayerProfileManager playerProfileManager;
    private NorseCommunityPlugin plugin;
    private HealthSystem healthSystem;

    public SetClass(NorseCommunityPlugin plugin) {
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
        this.healthSystem = HealthSystem.getInstance(this.plugin);
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("Usage: /setclass <player> <class>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        String playerClass = args[1];
        // Ensure that playerClass is one of the valid classes
        if (!isValidClass(playerClass)) {
            sender.sendMessage("Invalid class. Valid classes are: Warrior, Priest, Assassin, Mage.");
            return true;
        }

        PlayerProfile profile = playerProfileManager.getOrCreateProfile(target);
        profile.setPlayerClass(playerClass);

        healthSystem.updatePlayerHealth(target);
        Bukkit.getLogger().info("Called updatePlayerHealth");
        playerProfileManager.updateProfile(profile);
        sender.sendMessage("Set " + target.getName() + "'s class to " + playerClass + ".");
        return true;
    }

    private boolean isValidClass(String playerClass) {
        return playerClass.equalsIgnoreCase("Warrior") ||
                playerClass.equalsIgnoreCase("Priest") ||
                playerClass.equalsIgnoreCase("Assassin") ||
                playerClass.equalsIgnoreCase("Mage");
    }



}
