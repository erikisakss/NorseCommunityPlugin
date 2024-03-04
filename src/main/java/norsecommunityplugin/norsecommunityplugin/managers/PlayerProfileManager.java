package norsecommunityplugin.norsecommunityplugin.managers;

import norsecommunityplugin.norsecommunityplugin.Configs.PlayerConfig;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProfileManager {

    private NorseCommunityPlugin plugin;

    private static PlayerProfileManager instance = null;
    private PlayerConfig playerConfig;
    private Map<UUID, PlayerProfile> profiles = new HashMap<>();

    public PlayerProfileManager(NorseCommunityPlugin plugin){
        this.plugin = plugin;
        playerConfig = PlayerConfig.getInstance(plugin);
    }

    public static PlayerProfileManager getInstance(NorseCommunityPlugin plugin){
        if (instance == null){
            instance = new PlayerProfileManager(plugin);
        }
        return instance;
    }

    public PlayerProfile getOrCreateProfile(Player player){
        return profiles.computeIfAbsent(player.getUniqueId(), uuid -> {
            Bukkit.getLogger().info("Creating new profile for " + player.getName());
            //All info från configen
            Bukkit.getLogger().info("Loading profile data from config");
            Bukkit.getLogger().info("Level: " + playerConfig.getLevel(uuid));
            Bukkit.getLogger().info("XP: " + playerConfig.getExp(uuid));
            Bukkit.getLogger().info("Nation: " + playerConfig.getNation(uuid));
            Bukkit.getLogger().info("Clan: " + playerConfig.getClan(uuid));
            Bukkit.getLogger().info("Class: " + playerConfig.getPlayerClass(uuid));
            PlayerProfile profile = new PlayerProfile(uuid);
            // Ladda profildata från PlayerConfig eller sätt standardvärden
            profile.setLevel(playerConfig.getLevel(uuid));
            profile.setXP(playerConfig.getExp(uuid));
            profile.setMaxHP(playerConfig.getMaxHP(uuid));
            profile.setCurrentHP(playerConfig.getCurrentHP(uuid));
            profile.setMaxMana(playerConfig.getMaxMana(uuid));
            profile.setCurrentMana(playerConfig.getCurrentMana(uuid));
            profile.setNation(playerConfig.getNation(uuid));
            profile.setClan(playerConfig.getClan(uuid));
            profile.setPlayerClass(playerConfig.getPlayerClass(uuid));
            return profile;
        });
    }



    //Uppdatera befintlig profil i profiles-map
    public void updateProfile(PlayerProfile profile){
        profiles.put(profile.getUUID(), profile);

    }

    //Hämta profil från profiles-map
    public PlayerProfile getProfile(UUID uuid){
        return profiles.get(uuid);
    }

    public void savePlayerProfiles(){
        for (PlayerProfile profile : profiles.values()){
            playerConfig.setLevel(profile.getUUID(), profile.getLevel());
            playerConfig.setExp(profile.getUUID(), profile.getXP());
            playerConfig.setMaxHP(profile.getUUID(), profile.getMaxHP());
            playerConfig.setCurrentHP(profile.getUUID(), profile.getCurrentHP());
            playerConfig.setMaxMana(profile.getUUID(), profile.getMaxMana());
            playerConfig.setCurrentMana(profile.getUUID(), profile.getCurrentMana());
            playerConfig.setNation(profile.getUUID(), profile.getNation());
            playerConfig.setClan(profile.getUUID(), profile.getClan());
            playerConfig.setPlayerClass(profile.getUUID(), profile.getPlayerClass());
        }
        playerConfig.save();

        Bukkit.getLogger().info("Player profiles have been saved.");

        //Rensa profiles-map för att frigöra minne
        profiles.clear();
    }
}
