package norsecommunityplugin.norsecommunityplugin.HealthSystem;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HealthRegenTask implements Runnable{
    private PlayerProfileManager playerProfileManager;
    private NorseCommunityPlugin plugin;
    private HealthSystem healthSystem;

    public HealthRegenTask(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
        this.healthSystem = HealthSystem.getInstance(this.plugin);
    }

    @Override
    public void run(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
            if (profile != null) {
                double regenAmount = profile.getMaxHP() * 0.02; // Amount of HP to regenerate
                healthSystem.healPlayer(player, regenAmount);

            }
        }
    }


}
