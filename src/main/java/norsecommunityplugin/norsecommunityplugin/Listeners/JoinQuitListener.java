package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {

    private NorseCommunityPlugin plugin;
    private PlayerConfig playerConfig;

    public JoinQuitListener(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        playerConfig = PlayerConfig.getInstance(plugin);

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        //Has not joined before



    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){

    }
}
