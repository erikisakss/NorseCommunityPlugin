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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        // Replace getPlayer() with getWhoClicked()
        if(event.getWhoClicked() instanceof Player &&
                event.getClickedInventory().equals(guiManager.getUpgradeInventory())) {
            int slot = event.getSlot();
            List<Integer> unclickableSlots = Arrays.asList(0, 1, 2, 3,4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 23, 25, 26, 27,31,32,33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44);
            // If clicked slot is result slot, assume user wants to take result item
            if(unclickableSlots.contains(slot)) {
                event.setCancelled(true);
                //processUpgradeSlots(event.getClickedInventory());
            }
        } else {
            // cancel event to prevent item movement inside GUI
           // event.setCancelled(true);
            // run update functionality
           // processUpgradeSlots(event.getClickedInventory());
        }
    }
    private void processUpgradeSlots(Inventory upgradeInventory) {
        // Defining Slot Indices
        int placeholderIndex = 0;
        int resultIndex = 8;
        int[] itemAndScrollSlots = IntStream.rangeClosed(1, 7).toArray();

        ItemStack scroll = null;
        ItemStack itemToUpgrade = null;
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        NamespacedKey typeKey = new NamespacedKey(plugin, "item_type");
        int scrollSlot = -1;
        int itemSlot = -1;

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

        ItemStack upgradedItem = itemUpgrader.upgradeItem(itemToUpgrade, scroll);
        if (upgradedItem != null) {
            // Create new scroll stack if scroll amount was greater than 1
            if (scroll.getAmount() > 1) {
                ItemStack newScroll = scroll.clone();
                newScroll.setAmount(scroll.getAmount() - 1);
                upgradeInventory.setItem(scrollSlot, newScroll);
            } else {
                upgradeInventory.setItem(scrollSlot, new ItemStack(Material.AIR));
            }

            upgradeInventory.setItem(itemSlot, new ItemStack(Material.AIR));
            upgradeInventory.setItem(resultIndex, upgradedItem); // set result in slot 9 (index 8)
        }

        // You don't need to set to AIR if upgrade failed as user can modify items and try again
    }
}





   /* @EventHandler
    public void onCraftItem(InventoryClickEvent event) {
        if (!(event.getWhoClicked().getOpenInventory().getTopInventory() instanceof CraftingInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        CraftingInventory craftingInventory = (CraftingInventory) event.getWhoClicked().getOpenInventory().getTopInventory();
        Bukkit.getScheduler().runTaskLater(plugin, () -> updateCraftingResult(craftingInventory, player), 1L);

    } */

  /*  private void updateCraftingResult(CraftingInventory craftingInventory) {
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

        if (scroll == null || itemToUpgrade == null) {
            return;
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

    }  */

