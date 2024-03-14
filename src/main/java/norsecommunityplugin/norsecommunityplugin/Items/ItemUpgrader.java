package norsecommunityplugin.norsecommunityplugin.Items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemUpgrader {

    private static ItemUpgrader instance;
    private ItemManager itemManager;
    private NorseCommunityPlugin plugin;
    private Map<Integer, Double> LowToLow = new HashMap<>();
    private Map<Integer, Double> MidToLow = new HashMap<>();
    private Map<Integer, Double> HighToLow = new HashMap<>();
    private Map<Integer, Double> BlessedToLow = new HashMap<>();
    private Map<Integer, Double> MidToMid = new HashMap<>();
    private Map<Integer, Double> HighToMid = new HashMap<>();
    private Map<Integer, Double> BlessedToMid = new HashMap<>();
    private Map<Integer, Double> HighToHigh = new HashMap<>();
    private Map<Integer, Double> BlessedToHigh = new HashMap<>();

    public ItemUpgrader(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = ItemManager.getInstance(plugin);
        // Initialize the upgrade success rates from config
        for (int i = 1; i < 10; i++) {
            LowToLow.put(i, plugin.getConfig().getDouble("UpgradeRates.LowScrollLowGrade." + i));
            MidToLow.put(i, plugin.getConfig().getDouble("UpgradeRates.LowScrollMidGrade." + i));
            HighToLow.put(i, plugin.getConfig().getDouble("UpgradeRates.LowScrollHighGrade." + i));
            BlessedToLow.put(i, plugin.getConfig().getDouble("UpgradeRates.BlessedScrollLowGrade." + i));
            MidToMid.put(i, plugin.getConfig().getDouble("UpgradeRates.MidScrollMidGrade." + i));
            HighToMid.put(i, plugin.getConfig().getDouble("UpgradeRates.MidScrollHighGrade." + i));
            BlessedToMid.put(i, plugin.getConfig().getDouble("UpgradeRates.BlessedScrollMidGrade." + i));
            HighToHigh.put(i, plugin.getConfig().getDouble("UpgradeRates.HighScrollHighGrade." + i));
            BlessedToHigh.put(i, plugin.getConfig().getDouble("UpgradeRates.BlessedScrollHighGrade." + i));
            Bukkit.getLogger().info("Blessed to low: " + BlessedToLow.get(i));
        }

    }

    public static synchronized ItemUpgrader getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new ItemUpgrader(plugin);
        }
        return instance;
    }

    public ItemStack[] upgradeItem(ItemStack item, ItemStack scroll) {
        // Get the item blueprint
        ItemMeta meta = item.getItemMeta();
        NamespacedKey typeKey = new NamespacedKey(plugin, "item_type");
        String type = meta.getPersistentDataContainer().get(typeKey, PersistentDataType.STRING);
        Bukkit.getLogger().info("Type: " + type);

        if (type == null) {
            return null;
        }

        if (type.equalsIgnoreCase("Weapon")) {
          //  Bukkit.getLogger().info("Upgrading weapon");
            NamespacedKey weaponKey = new NamespacedKey(plugin, "weapon_id");
            //Bukkit.getLogger().info("Weapon Key: " + weaponKey);
            String weaponId = meta.getPersistentDataContainer().get(weaponKey, PersistentDataType.STRING);
            //Bukkit.getLogger().info("Weapon ID: " + weaponId);
            WeaponBlueprint blueprint = (WeaponBlueprint) itemManager.getItemBlueprint(weaponId);
            //Bukkit.getLogger().info("Blueprint: " + blueprint);
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

        return null;

    }

    private ItemStack[] upgradeWeapon(ItemStack item, ItemStack scroll, WeaponBlueprint blueprint) {
        // Get the weapon's current level
     //   Bukkit.getLogger().info("Upgrading weapon");
        ItemStack upgradedItem = item.clone();
        ItemMeta meta = upgradedItem.getItemMeta();
        NamespacedKey levelKey = new NamespacedKey(plugin, "weapon_level");
     //   Bukkit.getLogger().info("Level Key: " + levelKey);
        int level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
   //     Bukkit.getLogger().info("Level: " + level);
        //Check if the scroll is the correct type and grade (Blessed grade works on all weapons)
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        String scrollType = scroll.getItemMeta().getPersistentDataContainer().get(scrollTypeKey, PersistentDataType.STRING);
      //  Bukkit.getLogger().info("Scroll Type: " + scrollType);
        NamespacedKey scrollGradeKey = new NamespacedKey(plugin, "scroll_grade");
        String scrollGrade = scroll.getItemMeta().getPersistentDataContainer().get(scrollGradeKey, PersistentDataType.STRING);
     //   Bukkit.getLogger().info("Scroll Grade: " + scrollGrade);

        if (!Objects.equals(scrollType, "Upgrade")) {
            //Bukkit.getLogger().info("Scroll type is not upgrade");
            return null;
        }

        if (scrollGrade == null) {
           // Bukkit.getLogger().info("Scroll grade is null");
            return null;
        }

        // Check if the scroll is the correct grade, if the scroll is not blessed, check if the scroll grade is the same as the weapon grade
        if (!scrollGrade.equalsIgnoreCase("Blessed") && !scrollGrade.equalsIgnoreCase(blueprint.getItemGrade())) {
         //   Bukkit.getLogger().info("Scroll grade is not blessed or the same as the weapon grade");
            return null;
        }

        int newLevel = level + 1;
        // Check if the weapon is at max level
        if (newLevel > 10) {
            return null;
        }

        if (newLevel > 6) {
            //Make item look enchanted
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        //Hide enchantmen

        }

        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);
       // Bukkit.getLogger().info("Old Name: " + upgradedItem.displayName());
        Component newName = itemManager.updateDisplayNameWithNewLevel(item.displayName(), newLevel, blueprint.getRarity());
        //Bukkit.getLogger().info("New Name: " + newName);
        meta.displayName(newName);
        upgradedItem.setItemMeta(meta);

        upgradeWeaponStats(upgradedItem, blueprint, newLevel);
        Bukkit.getLogger().info("Item Grade: " + blueprint.getItemGrade());
        Bukkit.getLogger().info("Scroll Grade: " + scrollGrade);
        Bukkit.getLogger().info("Success rate in hashmap: " + BlessedToLow.get(level));

        boolean success = checkUpgradeSuccess(level, blueprint.getItemGrade(), scrollGrade);
        Bukkit.getLogger().info("Success: " + success);

        if (!success) {
            return new ItemStack[]{upgradedItem, new ItemStack(Material.COAL, 1)};
        }

        return new ItemStack[]{upgradedItem};
    }

    private void upgradeWeaponStats(ItemStack item, WeaponBlueprint blueprint, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        // Get the lore
      //  Bukkit.getLogger().info("Upgrading weapon stats");
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(blueprint.getType()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(blueprint.getPlayerClass()).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        if (blueprint.getRequiredLevel() > 1) {
            lore.add(Component.text("Level: " + blueprint.getRequiredLevel() + "+").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());

        Integer baseDamage = blueprint.getAttackPower().get(newLevel);
      //  Bukkit.getLogger().info("Base damage: " + baseDamage);
        lore.add(Component.text("Damage: " + baseDamage).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        // Example debugging log
      //  Bukkit.getLogger().info("Creating lore for: " + blueprint.getName() + " with attack speed: " + blueprint.getAttackSpeed());

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

    private ItemStack[] upgradeArmor(ItemStack item, ItemStack scroll, ArmorBlueprint blueprint) {
        // Get the armor's current level
        ItemStack upgradedItem = item.clone();
        ItemMeta meta = upgradedItem.getItemMeta();
        NamespacedKey levelKey = new NamespacedKey(plugin, "armor_level");
        int level = meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER);
        Bukkit.getLogger().info("Level: " + level);

        //Check if the scroll is the correct type and grade (Blessed grade works on all armor)
        NamespacedKey scrollTypeKey = new NamespacedKey(plugin, "scroll_type");
        String scrollType = scroll.getItemMeta().getPersistentDataContainer().get(scrollTypeKey, PersistentDataType.STRING);
        NamespacedKey scrollGradeKey = new NamespacedKey(plugin, "scroll_grade");
        String scrollGrade = scroll.getItemMeta().getPersistentDataContainer().get(scrollGradeKey, PersistentDataType.STRING);

        if (!Objects.equals(scrollType, "Upgrade")) {
            return null;
        }

        if (scrollGrade == null) {
            return null;
        }

        // Check if the scroll is the correct grade, if the scroll is not blessed, check if the scroll grade is the same as the armor grade
        if (!scrollGrade.equalsIgnoreCase("Blessed") && !scrollGrade.equalsIgnoreCase(blueprint.getItemGrade())) {
            return null;
        }

        int newLevel = level + 1;
        Bukkit.getLogger().info("New Level: " + newLevel);
        // Check if the armor is at max level
        if (newLevel > 10) {
            return null;
        }

        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, newLevel);
        Component newName = itemManager.updateDisplayNameWithNewLevel(item.displayName(), newLevel, blueprint.getRarity());

        meta.displayName(newName);
        upgradedItem.setItemMeta(meta);
        Bukkit.getLogger().info("Level in meta: " + meta.getPersistentDataContainer().get(levelKey, PersistentDataType.INTEGER));

        upgradeArmorStats(upgradedItem, blueprint, newLevel);

        boolean success = checkUpgradeSuccess(level, blueprint.getItemGrade(), scrollGrade);

        if (!success) {
            return new ItemStack[]{upgradedItem, new ItemStack(Material.COAL, 1)};
        }

        return new ItemStack[]{upgradedItem};
    }

    private void upgradeArmorStats(ItemStack item, ArmorBlueprint blueprint, int newLevel) {
        ItemMeta meta = item.getItemMeta();
        // Get the lore
        ArrayList<Component> lore = new ArrayList<>();
        lore.add(Component.text(blueprint.getType()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        if (blueprint.getRequiredLevel() > 1) {
            lore.add(Component.text("Level: " + blueprint.getRequiredLevel() + "+").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        }


        int baseProtection = blueprint.getProtection(newLevel);
        lore.add(Component.text("Protection: " + baseProtection).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));

        lore.add(Component.empty());

        NamespacedKey statBonusKey = new NamespacedKey(plugin, "armor_stat_type");
        String statBonusType = meta.getPersistentDataContainer().get(statBonusKey, PersistentDataType.STRING);
        if (statBonusType != null) {
            int statBonus = blueprint.getStatBonus(newLevel);
            lore.add(Component.text(statBonusType + " Bonus: " + statBonus).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());

        lore.add(Component.text("Item Grade: " + blueprint.getItemGrade() + " Class").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());

        meta.lore(lore);
        Bukkit.getLogger().info("Setting lore");
        item.setItemMeta(meta);
    }

    private boolean checkUpgradeSuccess(int level, String itemGrade, String scrollGrade) {
        double successRate = 0;
        Bukkit.getLogger().info("Item grade: " + itemGrade);
        Bukkit.getLogger().info("Scroll grade: " + scrollGrade);
        switch (itemGrade) {
            case "Low":
                Bukkit.getLogger().info("Item grade is low");
                switch (scrollGrade) {
                    case "Low":
                        successRate = LowToLow.get(level);
                        break;
                    case "Mid":
                        successRate = MidToLow.get(level);
                        break;
                    case "High":
                        successRate = HighToLow.get(level);
                        break;
                    case "Blessed":
                        Bukkit.getLogger().info("Blessed to low");
                        successRate = BlessedToLow.get(level);
                        Bukkit.getLogger().info("Success Rate: " + successRate);
                        break;
                }
                break;
            case "Mid":
                Bukkit.getLogger().info("Item grade is mid");
                switch (scrollGrade) {
                    case "Mid":
                        successRate = MidToMid.get(level);
                        break;
                    case "High":
                        successRate = HighToMid.get(level);
                        break;
                    case "Blessed":
                        successRate = BlessedToMid.get(level);
                        break;
                }
                break;
            case "High":
                Bukkit.getLogger().info("Item grade is high");
                switch (scrollGrade) {
                    case "High":
                        successRate = HighToHigh.get(level);
                        break;
                    case "Blessed":
                        successRate = BlessedToHigh.get(level);
                        break;
                }
                break;

            default:
                Bukkit.getLogger().info("Item grade is not low, mid, or high");
                break;
        }

        return successRate >= Math.random();

    }
}
