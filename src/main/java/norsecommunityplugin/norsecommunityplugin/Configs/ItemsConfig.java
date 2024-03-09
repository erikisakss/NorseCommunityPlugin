package norsecommunityplugin.norsecommunityplugin.Configs;

import norsecommunityplugin.norsecommunityplugin.Items.ArmorBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.ItemBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.ScrollBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.WeaponBlueprint;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemsConfig extends Config{

        private static ItemsConfig instance;

        public ItemsConfig(NorseCommunityPlugin plugin) {
            super(plugin, "ItemData.yml");

        }

        public static synchronized ItemsConfig getInstance(NorseCommunityPlugin plugin) {
            if (instance == null) {
                instance = new ItemsConfig(plugin);
            }
            return instance;
        }

    public ItemBlueprint getItemBlueprint(String itemName) {
        ConfigurationSection itemSection = getConfigurationSection("items." + itemName);
        Bukkit.getLogger().info("ItemSection: " + itemSection);
        if (itemSection == null) return null;

        String type = itemSection.getString("type");
        Bukkit.getLogger().info("Type: " + type);
        switch (type) {
            case "Weapon":
                return getWeaponBlueprint(itemName);
            case "Armor":
                return getArmorBlueprint(itemName);
            case "Scroll":
                return getScrollBlueprint(itemName);
            case "Accessory":
                return null; // Similarly for accessory
        }
        return null;
    }

    public WeaponBlueprint getWeaponBlueprint(String itemName) {
            ConfigurationSection itemSection = getConfigurationSection("items." + itemName);
            ConfigurationSection upgradesSection = itemSection.getConfigurationSection("upgrades");
            Bukkit.getLogger().info("ItemSection: " + itemSection);
            if (itemSection == null || !itemSection.getString("type").equalsIgnoreCase("Weapon")) {
                return null;
            }
            String name = itemName;
            String type = itemSection.getString("type");
            String rarity = itemSection.getString("rarity");
            String material = itemSection.getString("material");
            int level = 1;
            int requiredLevel = itemSection.getInt("requiredLevel");
            double price = itemSection.getDouble("price");
            String itemGrade = itemSection.getString("itemGrade");
            String playerClass = itemSection.getString("playerClass");
            String attackSpeed = itemSection.getString("attackSpeed");
            double effectiveRange = itemSection.getDouble("effectiveRange");
            ArrayList<String> extraDamageTypes = (ArrayList<String>) itemSection.getList("damageTypes");
            if (extraDamageTypes == null) {
                extraDamageTypes = new ArrayList<>();
            }
            Map<Integer, Integer> attackPower = new HashMap<>();
            ConfigurationSection attackPowerSection = upgradesSection.getConfigurationSection("attackPower");
            for (String key : attackPowerSection.getKeys(false)) {
                attackPower.put(Integer.parseInt(key), attackPowerSection.getInt(key));
            }
            Map<Integer, Integer> extraDamageUpgrade = new HashMap<>();
            ConfigurationSection extraDamageUpgradeSection = upgradesSection.getConfigurationSection("damageUpgrades");
            for (String key : extraDamageUpgradeSection.getKeys(false)) {
                extraDamageUpgrade.put(Integer.parseInt(key), extraDamageUpgradeSection.getInt(key));
            }

            return new WeaponBlueprint(name, type, rarity, requiredLevel, material, price, level, itemGrade, playerClass, attackSpeed, effectiveRange, attackPower, extraDamageTypes, extraDamageUpgrade);
        }


        public ArmorBlueprint getArmorBlueprint(String itemName) {
            ConfigurationSection itemSection = getConfigurationSection("items." + itemName);
            ConfigurationSection upgradesSection = itemSection.getConfigurationSection("upgrades");
            if (itemSection == null || !itemSection.getString("type").equalsIgnoreCase("Armor")) {
                return null;
            }
            String name = itemName;
            int level = 1;
            String type = itemSection.getString("type");
            String rarity = itemSection.getString("rarity");
            String material = itemSection.getString("material");
            int requiredLevel = itemSection.getInt("requiredLevel");
            double price = itemSection.getDouble("price");
            String itemGrade = itemSection.getString("itemGrade");
            Map<Integer, Integer> protection = new HashMap<>();
            ConfigurationSection protectionSection = upgradesSection.getConfigurationSection("protection");
            for (String key : protectionSection.getKeys(false)) {
                protection.put(Integer.parseInt(key), protectionSection.getInt(key));
            }
            ArrayList<String> statBonusTypes = (ArrayList<String>) itemSection.getList("statBonusTypes");
            Map<Integer, Integer> statBonus = new HashMap<>();
            ConfigurationSection statBonusSection = upgradesSection.getConfigurationSection("statBonus");
            for (String key : statBonusSection.getKeys(false)) {
                statBonus.put(Integer.parseInt(key), statBonusSection.getInt(key));
            }

            return new ArmorBlueprint(name, type, rarity, material, level, requiredLevel, price, itemGrade, protection, statBonusTypes, statBonus);
        }

        public ScrollBlueprint getScrollBlueprint(String itemName) {
            ConfigurationSection itemSection = getConfigurationSection("items." + itemName);
            if (itemSection == null || !itemSection.getString("type").equalsIgnoreCase("Scroll")) {
                return null;
            }
            String name = itemName;
            int level = 1;
            String type = itemSection.getString("type");
            String rarity = itemSection.getString("rarity");
            String material = itemSection.getString("material");
            int requiredLevel = 0;
            double price = itemSection.getDouble("price");
            String scrollType = itemSection.getString("scrollType");
            String itemGrade = itemSection.getString("itemGrade");
            String stat = itemSection.getString("stat");

            return new ScrollBlueprint(name, type, rarity, material, level, requiredLevel, price, scrollType, itemGrade, stat);
        }

        public void createWeaponBlueprint(WeaponBlueprint weaponBlueprint) {
            String path = "items." + weaponBlueprint.getName();
            set(path + ".type", weaponBlueprint.getType());
            set(path + ".rarity", weaponBlueprint.getRarity());
            set(path + ".requiredLevel", weaponBlueprint.getRequiredLevel());
           // set(path + ".material", weaponBlueprint.getMaterial();
            set(path + ".price", weaponBlueprint.getPrice());
            set(path + ".itemGrade", weaponBlueprint.getItemGrade());
            set(path + ".playerClass", weaponBlueprint.getPlayerClass());
            set(path + ".attackSpeed", weaponBlueprint.getAttackSpeed());
            set(path + ".effectiveRange", weaponBlueprint.getEffectiveRange());
            set(path + ".damageTypes", weaponBlueprint.getExtraDamageTypes());
            set(path + ".attackPower", weaponBlueprint.getAttackPower());
            set(path + ".damageUpgrades", weaponBlueprint.getExtraDamageUpgrade());
            saveConfig();
        }

        //getAllItemNames
        public ArrayList<String> getAllItemNames() {
            ConfigurationSection itemsSection = getConfigurationSection("items");
            if (itemsSection == null) {
                return null;
            }
            return new ArrayList<>(itemsSection.getKeys(false));
        }


}
