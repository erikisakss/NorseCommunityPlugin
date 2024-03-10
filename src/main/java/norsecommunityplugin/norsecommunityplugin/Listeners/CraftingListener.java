package norsecommunityplugin.norsecommunityplugin.Listeners;

import norsecommunityplugin.norsecommunityplugin.Items.ItemUpgrader;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CraftingListener implements Listener {

    private ItemUpgrader itemUpgrader;
    private GuiManager guiManager;
    private NorseCommunityPlugin plugin;
    public CraftingListener(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemUpgrader = ItemUpgrader.getInstance(plugin);
        this.guiManager = GuiManager.getInstance(plugin);
    }

    @EventHandler
    public void onCraftItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked().getOpenInventory().getTopInventory() instanceof CraftingInventory)) {
            return;
        }
        CraftingInventory craftingInventory = (CraftingInventory) event.getWhoClicked().getOpenInventory().getTopInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> updateCraftingResult(craftingInventory), 1L);

    }

    private void updateCraftingResult(CraftingInventory craftingInventory) {
        ItemStack[] matrix = craftingInventory.getMatrix();
        ItemStack scroll = null;
        ItemStack itemToUpgrade = null;
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        NamespacedKey typeKey = new NamespacedKey(plugin, "item_type");
        int scrollSlot = -1;
        int itemSlot = -1;

        for (int i = 0; i < matrix.length; i++) {
            ItemStack item = matrix[i];
            if (item == null) continue;
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(scrollTypeKey, PersistentDataType.STRING)) {
                scroll = item;
                scrollSlot = i;
                Bukkit.getLogger().info("Scrollslot: " + scrollSlot);
              //  Bukkit.getLogger().info("Scroll: " + scroll);
            } else if (container.has(typeKey, PersistentDataType.STRING)){
                itemToUpgrade = item;
              //  Bukkit.getLogger().info("Item: " + itemToUpgrade);
                itemSlot = i;
            }
        }

        //Bukkit.getLogger().info("Scroll and item found");
       // Bukkit.getLogger().info("Scroll after loop: " + scroll);
        //Bukkit.getLogger().info("Item after loop: " + itemToUpgrade);

        if (scroll != null && itemToUpgrade != null) {
            Bukkit.getLogger().info("Scroll and item found");
           // Bukkit.getLogger().info("Scroll Amount: " + scroll.getAmount());
            ItemStack upgradedItem = itemUpgrader.upgradeItem(itemToUpgrade, scroll);
            if (upgradedItem != null) {

                // Remove the scroll and item from the crafting grid
                for (int i = 0; i < matrix.length; i++) {
                    if (matrix[i] != null) {
                        if(matrix[i].equals(scroll)) {
                            Bukkit.getLogger().info("Scroll amount: " + matrix[i].getAmount());
                            if (matrix[i].getAmount() > 1) {
                                ItemStack newScroll = matrix[i].clone();
                                newScroll.setAmount(matrix[i].getAmount() - 1);
                                Bukkit.getLogger().info("Scroll amount after: " + newScroll.getAmount());
                                craftingInventory.setItem(i, newScroll);
                            } else {
                                craftingInventory.setItem(i, new ItemStack(Material.AIR));
                                scroll = null;
                                scrollSlot = -1;
                            }
                            continue;
                        }
                        if(matrix[i].equals(itemToUpgrade)) {
                            craftingInventory.setItem(i, new ItemStack(Material.AIR));
                            itemToUpgrade = null;
                            itemSlot = -1;
                            break;
                        }
                    }
                }
                craftingInventory.setResult(upgradedItem);
            } else {
                craftingInventory.setResult(new ItemStack(Material.AIR));
            }
        } else {
            craftingInventory.setResult(new ItemStack(Material.AIR));
        }

    }
}
