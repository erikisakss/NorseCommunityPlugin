package norsecommunityplugin.norsecommunityplugin.Configs;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;

import java.util.UUID;

public class PlayerConfig extends Config{

    private static PlayerConfig instance;

    public PlayerConfig(NorseCommunityPlugin plugin) {
        super(plugin, "PlayerData.yml");
    }

    public static synchronized PlayerConfig getInstance(NorseCommunityPlugin plugin) {
        if (instance == null) {
            instance = new PlayerConfig(plugin);
        }
        return instance;
    }

    public void createPlayerInfo(UUID uuid) {
        // Create a new player info in the config file with standard values
        String path = "Players." + uuid.toString();
        set(path + ".Level", 1);
        set(path + ".XP", 0);
        setMaxHP(uuid, 100);
        setCurrentHP(uuid, 100);
        setMaxMana(uuid, 100);
        setCurrentMana(uuid, 100);
        setProtection(uuid, 0);
        setDamage(uuid, 0);
        setStrength(uuid, 0);
        setWisdom(uuid, 0);
        setIntelligence(uuid, 0);
        setDexterity(uuid, 0);
        setNation(uuid, "None");
        setClan(uuid, "None");
        setPlayerClass(uuid, "None");

        saveConfig();
    }

    public int getLevel(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Level";
        return getInt(path);
    }

    public int getExp(UUID uuid) {
        String path = "Players." + uuid.toString() + ".XP";
        return getInt(path);
    }

    public String getNation(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Nation";
        return getString(path);
    }

    public String getClan(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Clan";
        return getString(path);
    }

    public String getPlayerClass(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Class";
        return getString(path);
    }

    public double getMaxHP(UUID uuid) {
        String path = "Players." + uuid.toString() + ".MaxHP";
        return getDouble(path);
    }

    public double getMaxMana(UUID uuid) {
        String path = "Players." + uuid.toString() + ".MaxMana";
        return getDouble(path);
    }

    public double getCurrentHP(UUID uuid) {
        String path = "Players." + uuid.toString() + ".CurrentHP";
        return getDouble(path);
    }

    public double getCurrentMana(UUID uuid) {
        String path = "Players." + uuid.toString() + ".CurrentMana";
        return getDouble(path);
    }

    public int getProtection(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Protection";
        return getInt(path);
    }

    public int getDamage(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Damage";
        return getInt(path);
    }


    public int getStrength(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Strength";
        return getInt(path);
    }

    public int getWisdom(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Wisdom";
        return getInt(path);
    }

    public int getIntelligence(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Intelligence";
        return getInt(path);
    }

    public int getDexterity(UUID uuid) {
        String path = "Players." + uuid.toString() + ".Dexterity";
        return getInt(path);
    }

    public void setProtection(UUID uuid, int protection) {
        String path = "Players." + uuid.toString() + ".Protection";
        set(path, protection);
    }

    public void setDamage(UUID uuid, int damage) {
        String path = "Players." + uuid.toString() + ".Damage";
        set(path, damage);
    }

    public void setStrength(UUID uuid, int strength) {
        String path = "Players." + uuid.toString() + ".Strength";
        set(path, strength);
    }

    public void setWisdom(UUID uuid, int wisdom) {
        String path = "Players." + uuid.toString() + ".Wisdom";
        set(path, wisdom);
    }

    public void setIntelligence(UUID uuid, int intelligence) {
        String path = "Players." + uuid.toString() + ".Intelligence";
        set(path, intelligence);
    }

    public void setDexterity(UUID uuid, int dexterity) {
        String path = "Players." + uuid.toString() + ".Dexterity";
        set(path, dexterity);
    }

    public void setMaxHP(UUID uuid, double maxHP) {
        String path = "Players." + uuid.toString() + ".MaxHP";
        set(path, maxHP);
    }

    public void setMaxMana(UUID uuid, double maxMana) {
        String path = "Players." + uuid.toString() + ".MaxMana";
        set(path, maxMana);
    }

    public void setCurrentHP(UUID uuid, double currentHP) {
        String path = "Players." + uuid.toString() + ".CurrentHP";
        set(path, currentHP);
    }

    public void setCurrentMana(UUID uuid, double currentMana) {
        String path = "Players." + uuid.toString() + ".CurrentMana";
        set(path, currentMana);
    }

    public void setLevel(UUID uuid, int level) {
        String path = "Players." + uuid.toString() + ".Level";
        set(path, level);

    }

    public void setExp(UUID uuid, int exp) {
        String path = "Players." + uuid.toString() + ".XP";
        set(path, exp);

    }

    public void setNation(UUID uuid, String nation) {
        String path = "Players." + uuid.toString() + ".Nation";
        set(path, nation);

    }

    public void setClan(UUID uuid, String clan) {
        String path = "Players." + uuid.toString() + ".Clan";
        set(path, clan);

    }

    public void setPlayerClass(UUID uuid, String playerClass) {
        String path = "Players." + uuid.toString() + ".Class";
        set(path, playerClass);

    }

    public void removePlayerInfo(UUID uuid) {
        String path = "Players." + uuid.toString();
        set(path, null);

    }

    public void save() {
        saveConfig();
    }

}
