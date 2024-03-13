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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

public class CraftingListener implements Listener {

    private ItemUpgrader itemUpgrader;
    private GuiManager guiManager;
    private NorseCommunityPlugin plugin;
    ItemStack[] upgradedItem;
    int resultSlot = 24;
    int scrollSlot = -1;
    int itemSlot = -1;
    ItemStack scroll = null;
    ItemStack itemToUpgrade = null;
    List<Integer> itemAndScrollSlots = Arrays.asList(10,11,12,19,20,21,28,29,30);
    List<Integer> unclickableSlots = Arrays.asList(0, 1, 2, 3,4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 23, 25, 26, 27,31,32,33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44);
    public CraftingListener(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemUpgrader = ItemUpgrader.getInstance(plugin);
        this.guiManager = GuiManager.getInstance(plugin);
    }

    @EventHandler
    public void onTableOpen(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            if(block.getType().equals(Material.CRAFTING_TABLE)) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                guiManager.openUpgradeGUI(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Check if the closed inventory is the upgrade inventory
        if(event.getInventory().equals(guiManager.getUpgradeInventory())) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();

            // Loop through the upgrade inventory slots
            for(int i : itemAndScrollSlots) {
                ItemStack item = inventory.getItem(i);

                // Check if the slot is not empty
                if(item != null && item.getType() != Material.AIR) {
                    // Attempt to add the item back to the player's inventory
                    HashMap<Integer, ItemStack> couldNotAdd = player.getInventory().addItem(item);

                    // If player's inventory is full, drop the item at the player's location
                    if(!couldNotAdd.isEmpty()) {
                        couldNotAdd.values().forEach(itm -> player.getWorld().dropItemNaturally(player.getLocation(), itm));
                    }

                    // Clear the slot in the upgrade inventory to prevent duplication
                    inventory.setItem(i, new ItemStack(Material.AIR));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        // Replace getPlayer() with getWhoClicked()
        if(event.getWhoClicked() instanceof Player &&
                event.getClickedInventory().equals(guiManager.getUpgradeInventory())) {
            int slot = event.getSlot();

            // If clicked slot is result slot, assume user wants to take result item
            if(unclickableSlots.contains(slot)) {
                event.setCancelled(true);
                //processUpgradeSlots(event.getClickedInventory());
            }

            if(itemAndScrollSlots.contains(slot)) {
                event.setCancelled(false);
                //processUpgradeSlots(event.getClickedInventory(), itemAndScrollSlots, resultSlot);

                if (event.getClickedInventory().getItem(resultSlot) != null) {
                    event.getClickedInventory().setItem(resultSlot, new ItemStack(Material.AIR));
                }

                Bukkit.getScheduler().runTaskLater(plugin, () -> processUpgradeSlots(event.getClickedInventory(), itemAndScrollSlots, resultSlot), 1L);

                //If

            }

            if (slot == resultSlot) {
                event.setCancelled(false);

                if (upgradedItem != null && upgradedItem.length > 1) {
                    event.setCancelled(true);
                    Bukkit.getLogger().info("Upgraded Item: " + upgradedItem[1]);
                    Bukkit.getLogger().info("Upgraded Item: " + upgradedItem[0]);
                    Bukkit.getLogger().info("Upgraded item length: " + upgradedItem.length);
                    // Create new scroll stack if scroll amount was greater than 1
                    if (scroll.getAmount() > 1) {
                        ItemStack newScroll = scroll.clone();
                        newScroll.setAmount(scroll.getAmount() - 1);
                        event.getClickedInventory().setItem(scrollSlot, newScroll);
                    } else {
                        event.getClickedInventory().setItem(scrollSlot, new ItemStack(Material.AIR));
                    }

                    // Remove the item to upgrade
                    event.getClickedInventory().setItem(itemSlot, new ItemStack(Material.AIR));
                    event.getClickedInventory().setItem(resultSlot, upgradedItem[1]);
                    event.setCancelled(false);
                } else if (upgradedItem != null) {
                        // Create new scroll stack if scroll amount was greater than 1
                        if (scroll.getAmount() > 1) {
                            ItemStack newScroll = scroll.clone();
                            newScroll.setAmount(scroll.getAmount() - 1);
                            event.getClickedInventory().setItem(scrollSlot, newScroll);
                        } else {
                            event.getClickedInventory().setItem(scrollSlot, new ItemStack(Material.AIR));
                        }

                        // Remove the item to upgrade
                        event.getClickedInventory().setItem(itemSlot, new ItemStack(Material.AIR));
                        event.setCancelled(false);
                }

            }

        }
    }
    private void processUpgradeSlots(Inventory upgradeInventory, List<Integer> itemAndScrollSlots, int resultSlot) {
        // Defining Slot Indices

        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        NamespacedKey typeKey = new NamespacedKey(plugin, "item_type");
        scroll = null;
        itemToUpgrade = null;
       // int scrollSlot = -1;
        // int itemSlot = -1;

        for (int i : itemAndScrollSlots) {
            ItemStack item = upgradeInventory.getItem(i);
            if (item == null) continue;
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(scrollTypeKey, PersistentDataType.STRING)) {
                scroll = item;
                scrollSlot = i;
            } else if (container.has(typeKey, PersistentDataType.STRING)){
                itemToUpgrade = item;
                itemSlot = i;
            }
        }

        if (scroll == null || itemToUpgrade == null) {
            return;
        }

        upgradedItem = itemUpgrader.upgradeItem(itemToUpgrade, scroll);
        Bukkit.getLogger().info("Upgraded Item: " + upgradedItem[0]);

        if (upgradedItem != null) {

            //
            upgradeInventory.setItem(resultSlot, upgradedItem[0]); // set result in slot 9 (index 8)
        }

        // You don't need to set to AIR if upgrade failed as user can modify items and try again
    }
}

