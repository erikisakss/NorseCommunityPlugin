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
import java.util.Objects;

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
        NamespacedKey typeKey = new NamespacedKey(plugin, "item_type");
        String type = meta.getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);
        Bukkit.getLogger().info("Type: " + type);

        if (type == null) {
            return item;
        }

        if (type.equalsIgnoreCase("Weapon")) {
            Bukkit.getLogger().info("Upgrading weapon");
            NamespacedKey weaponKey = new NamespacedKey(plugin, "weapon_id");
            Bukkit.getLogger().info("Weapon Key: " + weaponKey);
            String weaponId = meta.getPersistentDataContainer().get(weaponKey, PersistentDataType.STRING);
            Bukkit.getLogger().info("Weapon ID: " + weaponId);
            WeaponBlueprint blueprint = (WeaponBlueprint) itemManager.getItemBlueprint(weaponId);
            Bukkit.getLogger().info("Blueprint: " + blueprint);
            return upgradeWeapon(item, scroll, blueprint);
        } else if (type.equalsIgnoreCase("Armor")) {
            NamespacedKey armorKey = new NamespacedKey(plugin, "armor_id");
            Bukkit.getLogger().info("Armor Key: " + armorKey);
            String armorId = meta.getPersistentDataContainer().get(armorKey, PersistentDataType.STRING);
            Bukkit.getLogger().info("Armor ID: " + armorId);
            ArmorBlueprint blueprint = (ArmorBlueprint) itemManager.getItemBlueprint(armorId);
            Bukkit.getLogger().info("Blueprint: " + blueprint);
            return upgradeArmor(item, scroll, blueprint);
        }

        return item;

    }

    private ItemStack upgradeWeapon(ItemStack item, ItemStack scroll, WeaponBlueprint blueprint) {
        // Get the weapon's current level
        Bukkit.getLogger().info("Upgrading weapon");
        ItemStack upgradedItem = item.clone();
        ItemMeta meta = upgradedItem.getItemMeta();
        NamespacedKey levelKey = new NamespacedKey(plugin, "weapon_level");
        Bukkit.getLogger().info("Level Key: " + levelKey);
        int level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        Bukkit.getLogger().info("Level: " + level);
        //Check if the scroll is the correct type and grade (Blessed grade works on all weapons)
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        String scrollType = scroll.getItemMeta().getPersistentDataContainer().get(scrollTypeKey, PersistentDataType.STRING);
        Bukkit.getLogger().info("Scroll Type: " + scrollType);
        NamespacedKey scrollGradeKey = new NamespacedKey(plugin, "scroll_grade");
        String scrollGrade = scroll.getItemMeta().getPersistentDataContainer().get(scrollGradeKey, PersistentDataType.STRING);
        Bukkit.getLogger().info("Scroll Grade: " + scrollGrade);

        if (!Objects.equals(scrollType, "Upgrade")) {
            Bukkit.getLogger().info("Scroll type is not upgrade");
            return item;
        }

        if (scrollGrade == null) {
            Bukkit.getLogger().info("Scroll grade is null");
            return item;
        }

        // Check if the scroll is the correct grade, if the scroll is not blessed, check if the scroll grade is the same as the weapon grade
        if (!scrollGrade.equalsIgnoreCase("Blessed") && !scrollGrade.equalsIgnoreCase(blueprint.getItemGrade())) {
            Bukkit.getLogger().info("Scroll grade is not blessed or the same as the weapon grade");
            return item;
        }

        int newLevel = level + 1;
        // Check if the weapon is at max level
        if (newLevel > 10) {
            return item;
        }

        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);
       // Bukkit.getLogger().info("Old Name: " + upgradedItem.displayName());
        Component newName = itemManager.updateDisplayNameWithNewLevel(item.displayName(), newLevel, blueprint.getRarity());
        //Bukkit.getLogger().info("New Name: " + newName);
        meta.displayName(newName);
        upgradedItem.setItemMeta(meta);

        upgradeWeaponStats(upgradedItem, blueprint, newLevel);



        return upgradedItem;
    }

    private void upgradeWeaponStats(ItemStack item, WeaponBlueprint blueprint, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        // Get the lore
        Bukkit.getLogger().info("Upgrading weapon stats");
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
        item.setItemMeta(meta);
    }

    private ItemStack upgradeArmor(ItemStack item, ItemStack scroll, ArmorBlueprint blueprint) {

        return item;
    }
}
