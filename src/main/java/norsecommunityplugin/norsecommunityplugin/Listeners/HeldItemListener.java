package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Items.ItemBlueprint;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.ItemManager;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfile;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeldItemListener implements Listener {

    private NorseCommunityPlugin plugin;
    private ItemManager itemManager;
    private PlayerProfileManager playerProfileManager;

    public HeldItemListener(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = ItemManager.getInstance(plugin);
        this.playerProfileManager = PlayerProfileManager.getInstance(plugin);
    }

    // Add event handlers here
    //TODO: Add event handlers for other worn items like armor, accessories, etc.
    @EventHandler
    public void onPlayerHeldItemChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        resetStats(player); // Reset stats when changing held item

        if (newItem != null && newItem.getType() != Material.AIR) {
            int damageBonus = itemManager.calculateDamageForHeldWeapon(player, newItem);
            applyDamageBonus(player, damageBonus);
        } else {
            resetStats(player); // No valid item held, reset stats
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Player player = (Player) event.getWhoClicked();
            updateArmorStats(player);
        }, 1L); // Delay to ensure inventory updates
    }

    private void updateArmorStats(Player player) {
        int totalProtection = itemManager.calculateProtectionForHeldArmor(player);
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
        if (profile != null) {
            profile.setProtection(totalProtection);
        }
    }

    private void resetStats(Player player) {
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
        if (profile != null) {
            profile.setDamage(0);
            // Reset other stats as needed
        }
    }

    private void applyDamageBonus(Player player, int damageBonus) {
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
        if (profile != null) {
            profile.setDamage(profile.getDamage() + damageBonus);
        }
    }

}
