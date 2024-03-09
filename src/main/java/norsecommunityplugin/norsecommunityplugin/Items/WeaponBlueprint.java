package norsecommunityplugin.norsecommunityplugin.Items;

import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Map;

public class WeaponBlueprint extends ItemBlueprint {
    private String itemGrade;
    private String playerClass;
    private String attackSpeed;
    private double effectiveRange;
    private Map<Integer, Integer> attackPower;

    private ArrayList<String> extraDamageTypes;
    private Map<Integer, Integer> extraDamageUpgrade;

    public WeaponBlueprint(String name, String type, String rarity, int requiredLevel, String material, double price, int level, String itemGrade, String playerClass, String attackSpeed, double effectiveRange, Map<Integer, Integer> attackPower, ArrayList<String> extraDamageTypes, Map<Integer, Integer> extraDamageUpgrade) {
        super(name, type, rarity, material, level, requiredLevel, price);
        this.itemGrade = itemGrade;
        this.playerClass = playerClass;
        this.attackSpeed = attackSpeed;
        this.effectiveRange = effectiveRange;
        this.attackPower = attackPower;
        this.extraDamageTypes = extraDamageTypes;
        this.extraDamageUpgrade = extraDamageUpgrade;
    }

    public int calculateTotalDamage(int level) {
        int totalDamage = 0;
        totalDamage += attackPower.get(level);
        Bukkit.getLogger().info("Attack Power: " + attackPower.get(level));
        //Add extra damage for each type in extra damage map using extraDamageUpgrade map
        for (String type : extraDamageTypes) {
            totalDamage += extraDamageUpgrade.get(level);
        }
        Bukkit.getLogger().info("Total Damage: " + totalDamage);
        return totalDamage;

    }

    public String getItemGrade() {
        return itemGrade;
    }

    public String getPlayerClass() {

        return playerClass;
    }

    public String getAttackSpeed() {
        return attackSpeed;
    }

    public int getExtraDamageUpgrade(int level) {
        return extraDamageUpgrade.get(level);
    }

    public Map<Integer, Integer> getAttackPower() {
        return attackPower;
    }

    public double getEffectiveRange() {
        return effectiveRange;
    }

    public Map<Integer, Integer> getExtraDamageUpgrade() {
        return extraDamageUpgrade;
    }

    public ArrayList<String> getExtraDamageTypes() {
        return extraDamageTypes;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public void setPlayerClass(String playerClass) {
        this.playerClass = playerClass;
    }

    public void setAttackSpeed(String attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setEffectiveRange(double effectiveRange) {
        this.effectiveRange = effectiveRange;
    }

    public void setAttackPower(Map<Integer, Integer> attackPower) {
        this.attackPower = attackPower;
    }


    public void setExtraDamageUpgrade(Map<Integer, Integer> extraDamageUpgrade) {
        this.extraDamageUpgrade = extraDamageUpgrade;
    }

    public void setAttackPower(int level, int power) {
        attackPower.put(level, power);
    }


    public void setExtraDamageUpgrade(int level, int damage) {
        extraDamageUpgrade.put(level, damage);
    }

    public int getAttackPower(int level) {
        return attackPower.get(level);
    }




}
