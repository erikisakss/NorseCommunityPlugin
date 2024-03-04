package norsecommunityplugin.norsecommunityplugin.LevelingSystem;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerLevelManager;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EXPGivers implements Listener {
    LevelHandler levelHandler;
    PlayerProfileManager playerProfileManager;
    NorseCommunityPlugin plugin;


    public EXPGivers(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.levelHandler = LevelHandler.getInstance(this.plugin);
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
        Bukkit.getLogger().info("EXPGivers is enabled");

    }
    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event){
        Bukkit.getLogger().info("EntityDeathEvent is enabled");
        Player player = event.getEntity().getKiller();
        Entity ent = event.getEntity();

        if(ent instanceof Creature){
            event.setDroppedExp(0);
        }


        if (player instanceof Player){


            Entity entity = event.getEntity();

            if (entity.getType() == EntityType.ZOMBIE){
                playerProfileManager.getProfile(player.getUniqueId()).setXP(playerProfileManager.getProfile(player.getUniqueId()).getXP() + 100);
                //Message to player that they got 100 XP in blue text
                player.sendMessage("§9You got 100 XP!");
                levelHandler.EXPCheck(player);

            }


        }
    }

    @EventHandler
    public void BlockBreak(BlockBreakEvent event){
        Bukkit.getLogger().info("BlockBreakEvent is enabled");
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() == block.getType().STONE){
            Bukkit.getLogger().info(player.getDisplayName() + " broke a stone block");
            playerProfileManager.getProfile(player.getUniqueId()).setXP(playerProfileManager.getProfile(player.getUniqueId()).getXP() + 10);
            player.sendMessage("§9You got 10 XP!");
            levelHandler.EXPCheck(player);
        }

    }
}
