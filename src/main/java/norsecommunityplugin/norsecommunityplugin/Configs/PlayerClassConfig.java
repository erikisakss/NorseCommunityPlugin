package norsecommunityplugin.norsecommunityplugin.Configs;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;

public class PlayerClassConfig extends Config{

    private static PlayerClassConfig instance;

    public PlayerClassConfig(NorseCommunityPlugin plugin) {
        super(plugin, "PlayerClassData.yml");
    }

    public static synchronized PlayerClassConfig getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new PlayerClassConfig(plugin);
        }
        return instance;
    }

}
