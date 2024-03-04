package norsecommunityplugin.norsecommunityplugin.Tasks;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class KeepDayTask extends BukkitRunnable {


    NorseCommunityPlugin plugin;

    public KeepDayTask(NorseCommunityPlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public void run() {
        Bukkit.getServer().getWorld("world").setTime(6L);
    }
}
