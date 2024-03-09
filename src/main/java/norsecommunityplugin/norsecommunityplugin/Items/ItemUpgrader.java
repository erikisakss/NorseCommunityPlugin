package norsecommunityplugin.norsecommunityplugin.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ItemUpgrader {

    private static ItemUpgrader instance;
    private ItemManager itemManager;
    private NorseCommunityPlugin plugin;

    public ItemUpgrader(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = ItemManager.getInstance(plugin);
    }

    public static synchronized ItemUpgrader getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new ItemUpgrader(plugin);
        }
        return instance;
    }

    public ItemStack upgradeItem(ItemStack item, ItemStack scroll) {
        // Get the item blueprint
        ItemMeta meta = item.getItemMeta();
        NamespacedKey typeKey = new NamespacedKey(plugin, "type");
        String type = meta.getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);
        String itemGrade;

        if (type == "Weapon") {
            NamespacedKey weaponKey = new NamespacedKey(plugin, "weapon_id");
            String weaponId = meta.getPersistentDataContainer().get(weaponKey, PersistentDataType.STRING);
            WeaponBlueprint blueprint = (WeaponBlueprint) itemManager.getItemBlueprint(weaponId);
            return upgradeWeapon(item, scroll, blueprint);
        } else if (type == "Armor") {
            NamespacedKey armorKey = new NamespacedKey(plugin, "armor_id");
            String armorId = meta.getPersistentDataContainer().get(armorKey, PersistentDataType.STRING);
            ArmorBlueprint blueprint = (ArmorBlueprint) itemManager.getItemBlueprint(armorId);
            return upgradeArmor(item, scroll, blueprint);
        }

        return item;

    }

    private ItemStack upgradeWeapon(ItemStack item, ItemStack scroll, WeaponBlueprint blueprint) {
        // Get the weapon's current level
        ItemMeta meta = item.getItemMeta();
        NamespacedKey levelKey = new NamespacedKey(plugin, "weapon_level");
        int level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        //Check if the scroll is the correct type and grade (Blessed grade works on all weapons)
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        String scrollType = scroll.getItemMeta().getPersistentDataContainer().get(scrollTypeKey, PersistentDataType.STRING);
        NamespacedKey scrollGradeKey = new NamespacedKey(plugin, "scroll_grade");
        String scrollGrade = scroll.getItemMeta().getPersistentDataContainer().get(scrollGradeKey, PersistentDataType.STRING);

        if (scrollType != "Upgrade")
            return item;

        if (scrollGrade != blueprint.getItemGrade() && scrollType != "Blessed") {
            return item;
        }

        int newLevel = level + 1;
        // Check if the weapon is at max level
        if (newLevel > 10) {
            return item;
        }

        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);
        Component newName = itemManager.updateDisplayNameWithNewLevel(item.displayName(), newLevel, blueprint.getRarity());
        meta.displayName(newName);
        upgradeWeaponStats(item, blueprint, newLevel);
        item.setItemMeta(meta);

        return item;
    }

    private void upgradeWeaponStats(ItemStack item, WeaponBlueprint blueprint, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        // Get the lore
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(blueprint.getType()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(blueprint.getPlayerClass()).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        if (blueprint.getRequiredLevel() > 1) {
            lore.add(Component.text("Level: " + blueprint.getRequiredLevel() + "+").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());

        Integer baseDamage = blueprint.getAttackPower().get(newLevel);
        Bukkit.getLogger().info("Base damage: " + baseDamage);
        lore.add(Component.text("Damage: " + baseDamage).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        // Example debugging log
        Bukkit.getLogger().info("Creating lore for: " + blueprint.getName() + " with attack speed: " + blueprint.getAttackSpeed());

        lore.add(Component.text("Attack Speed: " + blueprint.getAttackSpeed()).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));

        for (String type : blueprint.getExtraDamageTypes()) {
            lore.add(itemManager.formatExtraDamage(type, blueprint.getExtraDamageUpgrade().get(newLevel)));
        }
        lore.add(Component.empty());

        lore.add(Component.text("Item Grade: " + blueprint.getItemGrade() + " Class").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());

        meta.lore(lore);
    }

    private ItemStack upgradeArmor(ItemStack item, ItemStack scroll, ArmorBlueprint blueprint) {

        return item;
    }
}
