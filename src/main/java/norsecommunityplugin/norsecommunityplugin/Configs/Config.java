package norsecommunityplugin.norsecommunityplugin.Configs;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class Config extends YamlConfiguration {

    protected NorseCommunityPlugin plugin;
    protected String fileName;
    protected File file;

    public Config(NorseCommunityPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.file = new File(plugin.getDataFolder(), fileName);

    }

    public Set<String> getSection(String path){
        ConfigurationSection section = getConfigurationSection(path);
        if (section != null){
            return section.getKeys(false);
        }
        return new HashSet<>();
    }

    private void checkFile(){
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }
    }
    public void loadConfig(){
        checkFile();
        try {
            this.load(file);
            Bukkit.getLogger().info("Loaded " + fileName);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().info("Failed to load " + fileName);
        }

    }

    public void saveConfig(){
        checkFile();
        try {
            this.save(file);
            Bukkit.getLogger().info("Saved " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getLogger().info("Failed to save " + fileName);
        }

    }
}
