package norsecommunityplugin.norsecommunityplugin;



import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.Events.DamageEvents;
import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthRegenTask;
import norsecommunityplugin.norsecommunityplugin.HealthSystem.HealthSystem;
import norsecommunityplugin.norsecommunityplugin.LevelingSystem.EXPGivers;

import norsecommunityplugin.norsecommunityplugin.LevelingSystem.LevelHandler;
import norsecommunityplugin.norsecommunityplugin.Listeners.GoodWeather;
import norsecommunityplugin.norsecommunityplugin.Listeners.JoinQuitListener;
import norsecommunityplugin.norsecommunityplugin.Tasks.KeepDayTask;
import norsecommunityplugin.norsecommunityplugin.commands.*;
import norsecommunityplugin.norsecommunityplugin.folders.DataFolder;
import norsecommunityplugin.norsecommunityplugin.managers.ConfigManager;
import norsecommunityplugin.norsecommunityplugin.managers.PlayerProfileManager;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


import org.bukkit.scheduler.BukkitTask;


import java.util.logging.Logger;


public final class NorseCommunityPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private static Logger logger;
    private PlayerProfileManager playerProfileManager;
    private LevelHandler levelHandler;
    private PlayerConfig playerConfig;

    private HealthSystem healthSystem;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("NorseCommunityPlugin has been enabled!");
        logger = getLogger();

        //Registering config
        saveDefaultConfig();
        playerConfig = PlayerConfig.getInstance(this);

        //Server settings
        Bukkit.getWorlds().get(0).setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.KEEP_INVENTORY, true);


        //Starting up the EXP system

        levelHandler = LevelHandler.getInstance(this);
        EXPGivers expGivers = new EXPGivers(this);
        Bukkit.getServer().getPluginManager().registerEvents(expGivers, this);

        //Registering HP system
        healthSystem = HealthSystem.getInstance(this);

        //Registering commands
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("setspawn").setExecutor(new SetSpawn());
        getCommand("nationmenu").setExecutor(new NationMenu(this));
        getCommand("classmenu").setExecutor(new ClassMenu(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("dagger").setExecutor(new GetDagger());


        //Events
        Bukkit.getServer().getPluginManager().registerEvents(new DamageEvents(this), this);

        //Starting tasks
        HealthRegenTask healthRegenTask = new HealthRegenTask(this);
        Bukkit.getScheduler().runTaskTimer(this, healthRegenTask, 0L, 100L);






        //Registering listeners
        getServer().getPluginManager().registerEvents(new GoodWeather(), this);
        BukkitTask keepDayTask = new KeepDayTask(this).runTaskTimer(this, 0L, 100L);

        //Registering managers
        configManager = new ConfigManager(this);
        configManager.loadConfigs();
        playerProfileManager = PlayerProfileManager.getInstance(this);



        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinQuitListener(this), this);
        //pluginManager.registerEvents(new InventoryListener(this), this);
        //Creating data folders
        DataFolder dataFolder = new DataFolder("NorseCommunityPlugin", "./plugins/");
        DataFolder nationsFolder = new DataFolder("Nations", "./plugins/NorseCommunityPlugin/");
    }

    @Override
    public void onDisable() {
        playerProfileManager.savePlayerProfiles();
        configManager.saveConfigs();

        if (levelHandler != null) {
            levelHandler.LevelSystemDisable();
        }

        Bukkit.getLogger().info("NorseCommunityPlugin has been disabled!");
    }

    public static Logger getPluginLogger(){
        return logger;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerProfileManager getPlayerProfileManager() {
        return playerProfileManager;
    }
}
