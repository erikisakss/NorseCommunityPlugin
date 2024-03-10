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
       upgradeInventory = Bukkit.createInventory(player, 9, Component.text("Upgrade Items").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD).asComponent());
       ItemStack placeholder = itemManager.getItem("Blessed_Scroll");
       ItemMeta placeholderMeta = placeholder.getItemMeta();
       placeholderMeta.displayName(Component.text("Place your item and scroll here!").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));
       placeholder.setItemMeta(placeholderMeta);

         upgradeInventory.setItem(0, placeholder);

            player.openInventory(upgradeInventory);
    }

    public Inventory getUpgradeInventory() {
        return upgradeInventory;
    }
}
