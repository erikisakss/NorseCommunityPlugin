package norsecommunityplugin.norsecommunityplugin.HealthSystem;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class HealthSystem {

    private NorseCommunityPlugin plugin;
    private PlayerProfileManager playerProfileManager;
    private static HealthSystem instance;

    public HealthSystem(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);

    }
    public static HealthSystem getInstance(NorseCommunityPlugin plugin){
        if (instance == null){
            instance = new HealthSystem(plugin);
        }
        return instance;
    }

    public void updatePlayerHealth(Player player){
        PlayerProfile profile = playerProfileManager.getOrCreateProfile(player);
        double maxHealth = calculateMaxHealth(profile);
        profile.setMaxHP(maxHealth);

        if (profile.getCurrentHP() > maxHealth){
            profile.setCurrentHP(maxHealth);
        }

        updateHealthBar(player, maxHealth, profile.getCurrentHP());
    }

    public void takeDamage(Player player, double damage){
        PlayerProfile profile = playerProfileManager.getOrCreateProfile(player);

        //TODO: Calculate damage value based on armor, resistances, armor penetration, etc.


        double newHealth = profile.getCurrentHP() - damage;

        if (newHealth <= 0) {
            // Handle death and respawn logic
            profile.setCurrentHP(0); // Set current HP to 0 to indicate death
            checkForDeathAndRespawn(player); // Custom method to handle respawn
            player.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, player.getLocation(), 50, new org.bukkit.Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 1));
        } else {
            // Update health normally
            profile.setCurrentHP(newHealth);
            updateHealthBar(player, profile.getMaxHP(), newHealth);
        }
    }

    //Calculate damage based on player stats
    public int calculateDamage(PlayerProfile profile){


        return 0;
    }

    public double calculateMaxHealth(PlayerProfile profile){
        int level = profile.getLevel();
        //Temporary fix for testing purposes
        if (profile.getMaxHP() > plugin.getConfig().getInt("Levels." + level + ".HP")){
            return profile.getMaxHP();
        } else {
            return plugin.getConfig().getInt("Levels." + level + ".HP");
        }
        //TODO: Add more factors to the health calculation (e.g. class, armor, etc.)
    }

    public void healPlayer(Player player, double amount){
        PlayerProfile profile = playerProfileManager.getOrCreateProfile(player);
        double newHealth = Math.min(profile.getMaxHP(), profile.getCurrentHP() + amount);
        profile.setCurrentHP(newHealth);
        updateHealthBar(player, profile.getMaxHP(), newHealth);
    }

    private void updateHealthBar(Player player, double maxHealth, double currentHealth){
        double healthPercentage = currentHealth / maxHealth;
        player.setHealthScale(20);
        player.setHealthScaled(true);
        player.setHealth(healthPercentage * player.getHealthScale());
    }

    public void checkForDeathAndRespawn(Player player){
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
        if (profile.getCurrentHP() <= 0) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                player.spigot().respawn(); // Respawn the player.
                player.teleport(player.getWorld().getSpawnLocation()); // Or any custom spawn location.
                profile.setCurrentHP(profile.getMaxHP()); // Resets health to max.
                updateHealthBar(player, profile.getMaxHP(), profile.getCurrentHP()); // Updates health bar.
                // Additional logic for resetting hunger, effects, etc., as needed.
            });
        }
    }

}
