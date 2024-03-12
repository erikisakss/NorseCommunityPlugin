package norsecommunityplugin.norsecommunityplugin.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.N;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiManager {
    private static GuiManager instance;
    private ItemManager itemManager;
    Inventory upgradeInventory;
    NorseCommunityPlugin plugin;

    public GuiManager(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = ItemManager.getInstance(plugin);
    }

    public static synchronized GuiManager getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new GuiManager(plugin);
        }
        return instance;
    }

    public void openUpgradeGUI (Player player) {
       upgradeInventory = Bukkit.createInventory(player, 45, Component.text("Upgrade Items").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).asComponent());
       ItemStack placeholder = itemManager.getItem("Blessed_Scroll");
       ItemStack glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
         ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
         ItemMeta blackMeta = blackGlass.getItemMeta();
         blackMeta.displayName(Component.text(" ").color(NamedTextColor.BLACK).decoration(TextDecoration.BOLD, false));
         ItemMeta glassMeta = glass.getItemMeta();
            glassMeta.displayName(Component.text("Upgrade!").color(NamedTextColor.GREEN).decoration(TextDecoration.BOLD, false));
       ItemMeta placeholderMeta = placeholder.getItemMeta();
       placeholderMeta.displayName(Component.text("Place your item and scroll here!").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
       placeholder.setItemMeta(placeholderMeta);
         glass.setItemMeta(glassMeta);
            blackGlass.setItemMeta(blackMeta);

         upgradeInventory.setItem(0, placeholder);
          //  upgradeInventory.setItem(8, glass);

        for(int i=0; i<=8; i++) {
            upgradeInventory.setItem(i, blackGlass);
        }
        upgradeInventory.setItem(9, blackGlass);
        upgradeInventory.setItem(13, blackGlass);
        upgradeInventory.setItem(14, blackGlass);
        upgradeInventory.setItem(15, blackGlass);
        upgradeInventory.setItem(16, blackGlass);
        upgradeInventory.setItem(17, blackGlass);
        upgradeInventory.setItem(18, blackGlass);
        upgradeInventory.setItem(22, blackGlass);
        upgradeInventory.setItem(23, blackGlass);
        upgradeInventory.setItem(25, blackGlass);
        upgradeInventory.setItem(26, blackGlass);
        upgradeInventory.setItem(27, blackGlass);
        upgradeInventory.setItem(31, blackGlass);
        upgradeInventory.setItem(32, blackGlass);
        upgradeInventory.setItem(33, blackGlass);
        upgradeInventory.setItem(34, blackGlass);
        upgradeInventory.setItem(35, blackGlass);
        for(int i=36; i<=44; i++) {
            upgradeInventory.setItem(i, blackGlass);
        }

            player.openInventory(upgradeInventory);
    }

    public Inventory getUpgradeInventory() {
        return upgradeInventory;
    }
}
