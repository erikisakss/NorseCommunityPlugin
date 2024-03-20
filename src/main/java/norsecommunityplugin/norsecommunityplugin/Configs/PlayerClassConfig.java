package norsecommunityplugin.norsecommunityplugin.Configs;

import jdk.internal.joptsimple.internal.Classes;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;

public class PlayerClassConfig extends Config{

    private static PlayerClassConfig instance;

    public PlayerClassConfig(NorseCommunityPlugin plugin) {
        super(plugin, "PlayerClassData.yml");

        createDefaultConfig();
    }

    public static synchronized PlayerClassConfig getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new PlayerClassConfig(plugin);
        }
        return instance;
    }

    //Fill config with default values
    public void createDefaultConfig() {
        //If the file doesn't contain the classes section, fill it with default values
        if (!contains("Classes")) {
            set("Classes.Warrior.hpMultiplier", 1.2);
            set("Classes.Warrior.damageMultiplier", 1.1);
            set("Classes.Priest.hpMultiplier", 1.1);
            set("Classes.Priest.damageMultiplier", 0.9);
            set("Classes.Assassin.hpMultiplier", 0.8);
            set("Classes.Assassin.damageMultiplier", 1.5);
            set("Classes.Mage.hpMultiplier", 0.9);
            set("Classes.Mage.damageMultiplier", 1.3);
            saveConfig();
        }
    }

    public double getClassHP(String className) {
        String path = "Classes." + className + ".hpMultiplier";
        return getDouble(path);
    }

    public double getClassDamage(String className) {
        String path = "Classes." + className + ".damageMultiplier";
        return getDouble(path);
    }

}
