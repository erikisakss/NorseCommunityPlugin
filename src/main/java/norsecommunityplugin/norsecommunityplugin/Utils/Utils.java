package norsecommunityplugin.norsecommunityplugin.Utils;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Utils {

    private static Logger logger = NorseCommunityPlugin.getPluginLogger();

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);

    }

    public static String decolor(String s){
        return ChatColor.stripColor(color(s));

    }

    public static void log(String... strings){
        for (String string : strings) {
            logger.info(string);
        }
    }

    public static void msgPlayer(Player player, String... strings){
        for (String string : strings) {
            player.sendMessage(color(string));
        }
    }

    public static ItemStack createItem(Material material, int amount, boolean glow, boolean unbreakable, String name, String... lore){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (name!=null){
            meta.setDisplayName(color(name));
        }
        if (lore != null){
            List<String> loreList = new ArrayList<>();
            for (String string : lore) {
                loreList.add(color(string));
            }
            meta.setLore(loreList);
        }



        if (glow){
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (unbreakable){
            meta.setUnbreakable(true);

        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack[] createArmor(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots){
        ItemStack[] armor = new ItemStack[4];
        armor[0] = helmet;
        armor[1] = chestplate;
        armor[2] = leggings;
        armor[3] = boots;
        return armor;
    }
}
