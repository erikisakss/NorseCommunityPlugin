package norsecommunityplugin.norsecommunityplugin.Items;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class ArmorBlueprint extends ItemBlueprint{

    private String itemGrade;
    private Map<Integer, Integer> protection;
    private ArrayList<String> statBonusTypes;
    private Map<Integer, Integer> statBonus;


    public ArmorBlueprint(String name, String type, String rarity, String material, int level, int requiredLevel, double price, String itemGrade, Map<Integer, Integer> protection, ArrayList<String> statBonusTypes, Map<Integer, Integer> statBonus) {
        super(name, type, rarity, material, requiredLevel, level, price);
        this.itemGrade = itemGrade;
        this.protection = protection;
        this.statBonusTypes = statBonusTypes;
        this.statBonus = statBonus;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public int getProtection(int level) {
        return protection.get(level);
    }

    public ArrayList<String> getStatBonusTypes() {
        return statBonusTypes;
    }

    public int getStatBonus(int level) {
        return statBonus.get(level);
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public void setProtection(int protection) {
        this.protection.put(this.getLevel(), protection);
    }




}
