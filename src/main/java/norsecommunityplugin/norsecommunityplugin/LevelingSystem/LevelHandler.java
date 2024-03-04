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


    public void EXPCheck(Player player){
        int PlayerLevel = playerProfileManager.getProfile(player.getUniqueId()).getLevel();

        int xpNeeded = this.plugin.getConfig().getInt("Levels."+ PlayerLevel + ".XP");
        Bukkit.getLogger().info("XP needed: " + xpNeeded);

        int xp = playerProfileManager.getProfile(player.getUniqueId()).getXP();
        Bukkit.getLogger().info("XP: " + xp);
        player.setExp(Math.min(1.0f, Math.max(0.0f, ((float) xp / (float) xpNeeded) * 1.0f)));
        ScoreBoard(player, PlayerLevel, xp, xpNeeded);
        if (xp >= xpNeeded){
            int level = playerProfileManager.getProfile(player.getUniqueId()).getLevel();
            int newLevel = level + 1;
            playerProfileManager.getProfile(player.getUniqueId()).setLevel(newLevel);
            int newXP = xp - xpNeeded;
            playerProfileManager.getProfile(player.getUniqueId()).setXP(newXP);
            healthSystem.updatePlayerHealth(player);
            int newXPNeeded = this.plugin.getConfig().getInt("Levels."+ newLevel + ".XP");
            player.setLevel(newLevel);
            player.setExp(Math.min(1.0f, Math.max(0.0f, ((float) newXP / (float) newXPNeeded) * 1.0f)));
            this.ScoreBoard(player, newLevel, newXP, newXPNeeded);
            player.sendMessage("You are now level " + newLevel);
        }




    }
}

