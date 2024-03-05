package norsecommunityplugin.norsecommunityplugin.managers;

import norsecommunityplugin.norsecommunityplugin.Configs.Config;
import norsecommunityplugin.norsecommunityplugin.Configs.PlayerClassConfig;
import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private NorseCommunityPlugin plugin;
    private List<Config> configs = new ArrayList<>();
    private PlayerConfig playerConfig;
    private PlayerClassConfig playerClassConfig;

    public ConfigManager(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
        playerConfig = PlayerConfig.getInstance(plugin);
        playerClassConfig = PlayerClassConfig.getInstance(plugin);
        configs.add(playerConfig);
        configs.add(playerClassConfig);
    }

    public void loadConfigs(){
        for (Config config : configs) {
            config.loadConfig();
        }
    }

    public void saveConfigs(){
        for (Config config : configs) {
            config.saveConfig();
        }
    }

    public PlayerConfig getPlayerConfig() {
        return playerConfig;
    }
}
