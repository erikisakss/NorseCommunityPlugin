package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Abilities.Ability;
import norsecommunityplugin.norsecommunityplugin.Abilities.Warrior.StompAbility;
import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class WarriorAbilityListener implements Listener {

    private NorseCommunityPlugin plugin;
    private PlayerProfileManager playerProfileManager;

    public WarriorAbilityListener(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.playerProfileManager = PlayerProfileManager.getInstance(this.plugin);
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());

        //IF player is a warrior class and sneaks and holds a stick
        if (player.isSneaking() && player.getInventory().getItemInMainHand().getType().equals(Material.STICK) && profile.getPlayerClass().equals("Warrior")) {
            Ability stomp = new StompAbility(player, plugin);
            stomp.activate();
        }
        // Ability activation code
    }

}
