package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Items.ItemUpgrader;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CraftingListener implements Listener {

    private ItemUpgrader itemUpgrader;
    private NorseCommunityPlugin plugin;
    public CraftingListener(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemUpgrader = ItemUpgrader.getInstance(plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        ItemStack[] matrix = event.getInventory().getMatrix();

        ItemStack scroll = null;
        ItemStack itemToUpgrade = null;
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");

        // Iterate over the crafting matrix to find the scroll and item
        for (ItemStack itemStack : matrix) {
            if (itemStack != null && itemStack.hasItemMeta()) {
                ItemMeta meta = itemStack.getItemMeta();
                PersistentDataContainer data = meta.getPersistentDataContainer();
                // Check if the current item is a scroll
                if (data.has(scrollTypeKey, PersistentDataType.STRING)) {
                    scroll = itemStack;
                } else {
                    // Assuming the other non-null item is the one to be upgraded
                    itemToUpgrade = itemStack;
                }
            }
        }

        // If both a scroll and an item were found in the matrix, attempt to upgrade
        if (scroll != null && itemToUpgrade != null) {
            ItemStack upgradedItem = itemUpgrader.upgradeItem(itemToUpgrade, scroll);
            if (upgradedItem != null) {
                event.setCurrentItem(upgradedItem);

                for (int i = 0; i < matrix.length; i++) {
                    if (matrix[i] != null && matrix[i].equals(scroll)) {
                        if (matrix[i].getAmount() > 1) {
                            matrix[i].setAmount(matrix[i].getAmount() - 1);
                        } else {
                            matrix[i] = null; // This effectively removes the scroll from the slot
                        }
                        break; // Assuming only one scroll is used
                    }
                }
                // Now set the matrix to update the crafting inventory
                event.getInventory().setMatrix(matrix);
                // You may need to handle inventory directly if more complex logic is required
            }
        }




    }
}
