package norsecommunityplugin.norsecommunityplugin.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import norsecommunityplugin.norsecommunityplugin.Configs.ItemsConfig;
import norsecommunityplugin.norsecommunityplugin.Items.AccessoryBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.ArmorBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.ItemBlueprint;
import norsecommunityplugin.norsecommunityplugin.Items.WeaponBlueprint;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemManager {

    private NorseCommunityPlugin plugin;
    private ItemsConfig itemsConfig;
    private PlayerProfileManager playerProfileManager;
    private Map<String, ItemStack>  itemCache;
    private Map<String, ItemBlueprint> itemBlueprintCache;
    private static ItemManager instance;

    public ItemManager(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        itemsConfig = ItemsConfig.getInstance(plugin);
        playerProfileManager = PlayerProfileManager.getInstance(plugin);
        itemCache = new HashMap<>();
        itemBlueprintCache = new HashMap<>();
    }

    public int getTotalDamageForHeldWeapon(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        ItemMeta meta = heldItem.getItemMeta();

        if (meta != null) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(plugin, "weapon_level");
            if (data.has(key, PersistentDataType.INTEGER)) {
                int weaponLevel = data.get(key, PersistentDataType.INTEGER);
                String weaponId = meta.getDisplayName().substring(0, meta.getDisplayName().indexOf(" ("));

            }
        }
        return 0; // Return 0 if no weapon is held or if it has no damage info
    }

    public static synchronized ItemManager getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new ItemManager(plugin);
        }
        return instance;
    }

    public ItemStack getItem(String itemName) {
        // Get an item from the cache
        return itemCache.get(itemName);
    }

    public int calculateDamageForHeldWeapon(Player player, ItemStack heldItem) {
        PlayerProfile profile = playerProfileManager.getProfile(player.getUniqueId());
        Bukkit.getLogger().info("Calculating damage for player: " + player.getName());
        Bukkit.getLogger().info("Held item: " + heldItem);
        if (heldItem.hasItemMeta()) {
            Bukkit.getLogger().info("Item has item meta");
            ItemMeta meta = heldItem.getItemMeta();
            NamespacedKey levelKey = new NamespacedKey(plugin, "weapon_level");
            Bukkit.getLogger().info("Level key: " + levelKey);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(levelKey, PersistentDataType.INTEGER)) {
                int weaponLevel = data.get(levelKey, PersistentDataType.INTEGER);
                WeaponBlueprint blueprint = getHeldWeaponBlueprint(player, heldItem);
                Bukkit.getLogger().info("Weapon blueprint: " + blueprint);
                if (blueprint != null && blueprint.getPlayerClass().equalsIgnoreCase(profile.getPlayerClass())) {
                    Bukkit.getLogger().info("Calculating total damage for weapon: " + blueprint.getName());
                    return blueprint.calculateTotalDamage(weaponLevel);
                }
            }
        }
        return 0; // Return 0 if no weapon is held or if the blueprint is not found or if the player's class does not match the weapon's class
    }

    public int calculateProtectionForHeldArmor(Player player) {
        int totalProtection = 0;

        // Iterate over each armor slot
        ItemStack[] armorContents = player.getInventory().getArmorContents();
        for (ItemStack armorPiece : armorContents) {
            if (armorPiece != null && armorPiece.hasItemMeta()) {
                ItemMeta meta = armorPiece.getItemMeta();
                NamespacedKey idKey = new NamespacedKey(plugin, "armor_id");
                NamespacedKey levelKey = new NamespacedKey(plugin, "armor_level");
                PersistentDataContainer data = meta.getPersistentDataContainer();
                if (data.has(idKey, PersistentDataType.STRING) && data.has(levelKey, PersistentDataType.INTEGER)) {
                    String armorId = data.get(idKey, PersistentDataType.STRING);
                    int armorLevel = data.get(levelKey, PersistentDataType.INTEGER);
                    ArmorBlueprint blueprint = (ArmorBlueprint) itemBlueprintCache.get(armorId);
                    if (blueprint != null) {
                        // Add the protection from this armor piece, adjusted for its level
                        totalProtection += blueprint.getProtection(armorLevel);
                    }
                }
            }
        }

        return totalProtection;
    }

    public WeaponBlueprint getHeldWeaponBlueprint(Player player, ItemStack heldItem) {
        if (heldItem.hasItemMeta()) {
            ItemMeta meta = heldItem.getItemMeta();
            NamespacedKey idKey = new NamespacedKey(plugin, "weapon_id");
            Bukkit.getLogger().info("ID key: " + idKey);
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (data.has(idKey, PersistentDataType.STRING)) {
                String weaponId = data.get(idKey, PersistentDataType.STRING);
                return (WeaponBlueprint) itemBlueprintCache.get(weaponId);
            }
        }
        return null;
    }
    public void preloadItems() {
        // Preload all items from the config file
        Bukkit.getLogger().info("Preloading items...");
        for (String itemName : itemsConfig.getSection("items")) {
            ItemBlueprint blueprint = itemsConfig.getItemBlueprint(itemName);
            if (blueprint != null) {
                itemBlueprintCache.put(itemName, blueprint);
            }
            ItemStack item = createItemStackFromBlueprint(blueprint);
            if (item != null) {
                Bukkit.getLogger().info("Preloading item: " + itemName);
                itemCache.put(itemName, item);
            }
        }
        Bukkit.getLogger().info("Preloading items complete.");
    }

    public ItemStack createItemStackFromBlueprint(ItemBlueprint blueprint) {
        // Create an item stack from the blueprint

        if (blueprint == null) return null;

        switch (blueprint.getType()) {
            case "Weapon":
                return createWeaponItemStack((WeaponBlueprint) blueprint);
            case "Armor":
                return null; // Similarly for armor
            case "Accessory":
                return null; // Similarly for accessory
            default:
                return null;
        }
    }

    private ItemStack createWeaponItemStack(WeaponBlueprint blueprint) {

        ItemStack item = new ItemStack(Material.valueOf(blueprint.getMaterial()));
        // Set the item's display name, lore, etc.
        ItemMeta meta = item.getItemMeta();
        meta.displayName(formatRarity(blueprint.getName() + " (+" + blueprint.getLevel() + ")", blueprint.getRarity()));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        List<Component> lore = new ArrayList<>();

        //Remove italic from the lore
        lore.add(Component.text(blueprint.getType()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text(blueprint.getPlayerClass()).color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        if (blueprint.getRequiredLevel() > 1) {
            lore.add(Component.text("Level: " + blueprint.getRequiredLevel() + "+").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false));
        }
        lore.add(Component.empty());

        Integer baseDamage = blueprint.getAttackPower().get(1);
        Bukkit.getLogger().info("Base damage: " + baseDamage);
        lore.add(Component.text("Damage: " + baseDamage).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        // Example debugging log
        Bukkit.getLogger().info("Creating lore for: " + blueprint.getName() + " with attack speed: " + blueprint.getAttackSpeed());

        lore.add(Component.text("Attack Speed: " + blueprint.getAttackSpeed()).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));

        for (String type : blueprint.getExtraDamageTypes()) {
            lore.add(formatExtraDamage(type, blueprint.getExtraDamageUpgrade().get(1)));
        }
        lore.add(Component.empty());

        lore.add(Component.text("Item Grade: " + blueprint.getItemGrade() + " Class").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());

        meta.lore(lore);

        AttributeModifier attackSpeedModifier = new AttributeModifier("generic.attackSpeed", mapAttackSpeedToValue(blueprint.getAttackSpeed()), AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);

        NamespacedKey levelKey = new NamespacedKey(plugin, "weapon_level");
        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, blueprint.getLevel());

        NamespacedKey idKey = new NamespacedKey(plugin, "weapon_id");
        meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, blueprint.getName());


        item.setItemMeta(meta);




        return item;
    }

    public ItemStack createArmorItemStack(String armorBlueprint) {

        ArmorBlueprint blueprint = (ArmorBlueprint) itemBlueprintCache.get(armorBlueprint);
        ItemStack item = new ItemStack(Material.valueOf(blueprint.getMaterial()));
        ItemMeta meta = item.getItemMeta();
        String name = blueprint.getName();
        name = name.replace("_", " ");
        meta.displayName(formatRarity(name + " (+" + blueprint.getLevel() + ")", blueprint.getRarity()));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(blueprint.getType()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.text("Protection: " + blueprint.getProtection(1)).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());

        Random random = new Random();
        int randomIndex = random.nextInt(blueprint.getStatBonusTypes().size());
        String statType = blueprint.getStatBonusTypes().get(randomIndex);
        int statBonus = blueprint.getStatBonus(blueprint.getLevel());
        lore.add(Component.text(statType + " Bonus: " + statBonus).color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());

        lore.add(Component.text("Item Grade: " + blueprint.getItemGrade() + " Class").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        lore.add(Component.empty());
        lore.add(Component.empty());

        meta.lore(lore);

        NamespacedKey statTypeKey = new NamespacedKey(plugin, "armor_stat_type");
        meta.getPersistentDataContainer().set(statTypeKey, PersistentDataType.STRING, statType);

        NamespacedKey levelKey = new NamespacedKey(plugin, "armor_level");
        meta.getPersistentDataContainer().set(levelKey, PersistentDataType.INTEGER, blueprint.getLevel());

        NamespacedKey idKey = new NamespacedKey(plugin, "armor_id");
        meta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, blueprint.getName());

        item.setItemMeta(meta);
        return item;
    }

    private Component formatExtraDamage(String type, int damage) {
        // Format the extra damage based on its type
        switch (type) {
            case "Flame":
                return Component.text(type + " Damage: " + damage).color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false);
            case "Glacier":
                return Component.text(type + " Damage: " + damage).color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false);
            case "Poison":
                return Component.text(type + " Damage: " + damage).color(NamedTextColor.DARK_GREEN).decoration(TextDecoration.ITALIC, false);
            case "Lightning":
                return Component.text(type + " Damage: " + damage).color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false);
            default:
                return Component.text(type + " Damage: " + damage).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false);
        }
    }

    private double mapAttackSpeedToValue(String attackSpeed) {
        switch (attackSpeed.toLowerCase()) {
            case "Very Slow":
                return -3.0; // Example value, adjust based on your game's balance
            case "Slow":
                return -2.0;
            case "Normal":
                return 0.0;
            case "Fast":
                return 2.0;
            case "Very Fast":
                return 3.0;
            default:
                return 0.0; // Default to normal speed if unspecified
        }
    }

    private Component formatRarity(String name, String rarity) {
        // Format the item's name based on its rarity
        switch (rarity) {
            case "Common":
                return Component.text(name).color(NamedTextColor.DARK_PURPLE);
            case "Uncommon":
                return Component.text(name).color(net.kyori.adventure.text.format.NamedTextColor.GREEN);
            case "Rare":
                return Component.text(name).color(net.kyori.adventure.text.format.NamedTextColor.BLUE);
            case "Epic":
                return Component.text(name).color(NamedTextColor.GOLD);
            case "Legendary":
                return Component.text(name).color(NamedTextColor.LIGHT_PURPLE);
            default:
                return Component.text(name).color(net.kyori.adventure.text.format.NamedTextColor.WHITE);
        }
    }
}
