package norsecommunityplugin.norsecommunityplugin.Abilities;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
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

    protected NorseCommunityPlugin plugin;

    public Ability(String name, Player player, int manaCost, int cooldown, NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.name = name;
        this.player = player;
        this.manaCost = manaCost;
        this.cooldown = cooldown;

    }

    public abstract void activate();

}
