package norsecommunityplugin.norsecommunityplugin.LevelingSystem;

import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthSystem;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerLevelManager;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

public class LevelHandler implements Listener {

    private PlayerProfileManager playerProfileManager;
    NorseCommunityPlugin plugin;
    private static LevelHandler instance = null;
    private PlayerConfig playerConfig;
    private HealthSystem healthSystem;

    private LevelHandler(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        playerConfig = PlayerConfig.getInstance(plugin);
        playerProfileManager = PlayerProfileManager.getInstance(plugin);
        healthSystem = HealthSystem.getInstance(plugin);
        logWithLocation("LevelHandler has been enabled");
       // plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public static LevelHandler getInstance(NorseCommunityPlugin plugin){
        if (instance == null){
            instance = new LevelHandler(plugin);
            Bukkit.getPluginManager().registerEvents(instance, plugin);
        }
        return instance;
    }

    public void logWithLocation(String message) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        // Index 1 eftersom index 0 är getStackTrace-metoden själv
        StackTraceElement element = stackTrace[1]; // Ändra index om det behövs baserat på djupet i anropskedjan
        String className = element.getClassName();
        String methodName = element.getMethodName();
        int lineNumber = element.getLineNumber();

        Bukkit.getLogger().info(String.format("[%s:%d] %s: %s", className, lineNumber, methodName, message));
    }



    public void LevelSystemDisable() {
          playerProfileManager.savePlayerProfiles();

        Bukkit.getLogger().info("Leveling system has been disabled.");

        // Rensa HashMap för att frigöra minne

    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()){

            Bukkit.getLogger().info("Player has not played before");
           playerConfig.createPlayerInfo(player.getUniqueId());
           PlayerProfile profile = playerProfileManager.getOrCreateProfile(player);
           int xpNeeded = this.plugin.getConfig().getInt("Levels.1.XP");


            this.ScoreBoard(player, profile.getLevel(), profile.getXP(), xpNeeded);
        }else {
            PlayerProfile profile = playerProfileManager.getOrCreateProfile(player);
            Bukkit.getLogger().info("Player has played before");

            int level = profile.getLevel();
            int xp = profile.getXP();

            int xpNeeded = this.plugin.getConfig().getInt("Levels."+ level + ".XP");
            //player.sendMessage("Welcome back!, you are level " + level);
            this.ScoreBoard(player, level, xp, xpNeeded);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        PlayerProfile profile = playerProfileManager.getProfile(uuid);
        playerProfileManager.updateProfile(profile);

    }

    private void ScoreBoard(Player player, int level, int xp, int xpNeeded){
        Scoreboard scoreboard = this.plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Test", "dummy");
        objective.setDisplayName("§9Player Level");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score PlayerLevel = objective.getScore("§6Level: " + level);
        PlayerLevel.setScore(1);
        Score PlayerXP = objective.getScore("§2XP: " + xp);
        PlayerXP.setScore(0);
        Score PlayerXPNeeded = objective.getScore("§4XP Needed: " + xpNeeded);
        PlayerXPNeeded.setScore(0);


        player.setScoreboard(scoreboard);
    }


    public void EXPCheck(Player player) {
        boolean leveledUp = false; // Track if the player leveled up
        int level = playerProfileManager.getProfile(player.getUniqueId()).getLevel();
        int xp = playerProfileManager.getProfile(player.getUniqueId()).getXP();

        while (true) {
            int xpNeeded = this.plugin.getConfig().getInt("Levels." + level + ".XP");
            Bukkit.getLogger().info("XP needed for level " + level + ": " + xpNeeded);
            Bukkit.getLogger().info("Current XP: " + xp);

            // Update player's experience bar
            player.setExp(Math.min(1.0f, ((float) xp / (float) xpNeeded)));

            if (xp >= xpNeeded) {
                leveledUp = true; // Player leveled up
                level++; // Increase level
                xp -= xpNeeded; // Subtract XP needed for the level-up
                playerProfileManager.getProfile(player.getUniqueId()).setLevel(level);
                playerProfileManager.getProfile(player.getUniqueId()).setXP(xp);
                // Log level up event
                Bukkit.getLogger().info("Player leveled up to: " + level);
            } else {
                // If not enough XP for another level, break the loop
                break;
            }
        }

        if (leveledUp) {
            // If the player leveled up, update their health and score board once
            healthSystem.updatePlayerHealth(player);
            int newXPNeeded = this.plugin.getConfig().getInt("Levels." + level + ".XP");
            player.setLevel(level);
            player.setExp(Math.min(1.0f, ((float) xp / (float) newXPNeeded)));
            this.ScoreBoard(player, level, xp, newXPNeeded);
            player.sendMessage("You are now level " + level);
        }
    }

}

