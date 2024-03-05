package norsecommunityplugin.norsecommunityplugin.Abilities;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Ability {
    protected String name;
    protected Player player;
    protected int manaCost;
    protected int cooldown;
    protected Map<UUID, Long> cooldowns;
    protected NorseCommunityPlugin plugin;

    public Ability(String name, Player player, int manaCost, int cooldown, NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
        this.player = player;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
        this.cooldowns = new HashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupCooldowns();
            }
        }.runTaskTimerAsynchronously(plugin, 20, 20*60);
    }

    public abstract void activate();

    protected void startCooldown() {
        long cooldownTime = System.currentTimeMillis() + (cooldown * 1000);
        cooldowns.put(player.getUniqueId(), cooldownTime);
    }

    protected boolean isOnCooldown() {
        Long cooldownTime = cooldowns.get(player.getUniqueId());
        if (cooldownTime == null) {
            return false;
        }
        return cooldownTime > System.currentTimeMillis();
    }

    private void cleanupCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.entrySet().removeIf(entry -> currentTime >= entry.getValue());
    }
}
