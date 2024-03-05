package norsecommunityplugin.norsecommunityplugin.Events;

import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthSystem;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageEvents implements Listener {

    private NorseCommunityPlugin plugin;
    private PlayerProfileManager playerProfileManager;
    private HealthSystem healthSystem;

    public DamageEvents(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
        this.healthSystem = HealthSystem.getInstance(this.plugin);
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true); // Cancel standard fall damage

            Player player = (Player) event.getEntity();
            double customDamage = 50; // Custom damage
            healthSystem.takeDamage(player, customDamage); // Apply custom damage
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            PlayerProfile attackerProfile = playerProfileManager.getProfile(attacker.getUniqueId());
            PlayerProfile victimProfile = playerProfileManager.getProfile(victim.getUniqueId());

            double attackerDamage = attackerProfile.getDamage();
            double attackerStrength = attackerProfile.getStrength();
            double attackerDexterity = attackerProfile.getDexterity();

            double victimProtection = victimProfile.getProtection();

            double finalDamage = attackerDamage * (1 + (attackerStrength / 100));

            victimProtection -= attackerDexterity;

            if (victimProtection < 0) {
                victimProtection = 0;
            }

            double protectionRatio = 1 + (victimProtection / 100);

            finalDamage = finalDamage / protectionRatio;
            victim.getWorld().spawnParticle(org.bukkit.Particle.SMOKE_NORMAL, victim.getLocation().add(0, 1, 0), 50, 0.25, 0.25, 0.25, 0);
            victim.getWorld().spawnParticle(org.bukkit.Particle.CRIT, victim.getLocation().add(0, 1, 0), 50, 0.25, 0.25, 0.25, 0);
            event.setCancelled(true);
            healthSystem.takeDamage(victim, finalDamage);
            //Add effects, particles, sounds, etc.


        }


    }
}
