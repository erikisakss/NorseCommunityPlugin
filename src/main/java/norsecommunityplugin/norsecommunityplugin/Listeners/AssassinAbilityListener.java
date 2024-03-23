package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Abilities.Ability;
import norsecommunityplugin.norsecommunityplugin.Abilities.Assassin.StealthAbility;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class AssassinAbilityListener implements Listener{


    private NorseCommunityPlugin plugin;

    private PlayerProfileManager playerProfileManager;

    public AssassinAbilityListener(NorseCommunityPlugin plugin){
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
            if (playerClass.equals(profile.getPlayerClass()) && profile.getPlayerClass().equals("Assassin")) {
                Ability stealth = profile.getAbility("Stealth");
                if (stealth == null) {
                    stealth = new StealthAbility(player, plugin);
                    profile.addAbility(stealth);
                }

                stealth.activate();
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
            if (!Objects.equals(profile.getPlayerClass(), "Assassin")) {
                return;
            }
            Ability stealth = profile.getAbility("Stealth");
            if (StealthAbility.isStealthed(player) && stealth != null) {
                stealth.deactivate();
            }
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
            if (!Objects.equals(profile.getPlayerClass(), "Assassin")) {
                return;
            }
            Ability stealth = profile.getAbility("Stealth");
            if (StealthAbility.isStealthed(player) && stealth != null){
                stealth.deactivate();
            }
        }

    }





}
