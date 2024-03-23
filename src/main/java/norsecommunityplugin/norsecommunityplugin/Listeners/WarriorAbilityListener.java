package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Abilities.Ability;
import norsecommunityplugin.norsecommunityplugin.Abilities.Warrior.StompAbility;
import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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


        //If player_class key in item meta == getPlayerClass

        ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();

        NamespacedKey classKey = new NamespacedKey(plugin, "player_class");
        if (itemMeta != null && itemMeta.getPersistentDataContainer().has(classKey, PersistentDataType.STRING)) {
            String playerClass = itemMeta.getPersistentDataContainer().get(classKey, PersistentDataType.STRING);
            assert playerClass != null;
            if (playerClass.equals(profile.getPlayerClass()) && profile.getPlayerClass().equals("Warrior")) {
                Ability stomp = new StompAbility(player, plugin);
                stomp.activate();
            }
        }
    }

}
