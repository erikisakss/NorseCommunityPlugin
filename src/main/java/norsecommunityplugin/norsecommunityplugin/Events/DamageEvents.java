package norsecommunityplugin.norsecommunityplugin.Events;

import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthSystem;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
            double customDamage = 200; // Custom damage
            healthSystem.takeDamage(player, customDamage); // Apply custom damage
        }
    }
}
