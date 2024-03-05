package norsecommunityplugin.norsecommunityplugin.managers;

import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private int level;
    private int xp;
    private int protection;
    private int dexterity;
    private int strength;
    private int intelligence;
    private int wisdom;

    private double maxHP;
    private double maxMana;
    private double currentHP;
    private double currentMana;
    private String nation;
    private String clan;
    private String playerClass;

    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
    }

    // Getter och setter för level, xp, nation, clan, playerClass...

    public UUID getUUID() {
        return uuid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXP() {
        return xp;
    }

    public void setXP(int xp) {
        this.xp = xp;
    }

    public int getProtection() {
        return protection;
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    // Metoder för att hantera nation, clan, playerClass...

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public String getPlayerClass() {

        return playerClass;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
    }

    // Metoder för att hantera maxHP, maxMana, currentHP, currentMana...

    public double getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(double maxHP) {
        this.maxHP = maxHP;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public double getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(double currentHP) {
        this.currentHP = currentHP;
    }

    public double getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(double currentMana) {
        this.currentMana = currentMana;
    }



    public void save() {
        // Spara spelarens profil i konfigurationsfilen

    }
}

