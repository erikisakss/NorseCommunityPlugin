package norsecommunityplugin.norsecommunityplugin.Items;

import net.kyori.adventure.text.Component;

public class ItemBlueprint {
    private String name;
    private String type;
    private String rarity;
    private String material;
    private int level;
    private int requiredLevel;
    private double price;

    public ItemBlueprint(String name, String type, String rarity, String material, int level, int requiredLevel, double price) {
        this.name = name;
        this.type = type;
        this.rarity = rarity;
        this.level = level;
        this.requiredLevel = requiredLevel;
        this.price = price;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRarity() {
        return rarity;
    }

    public String getMaterial() {
        return material;
    }
    public int getLevel() {
        return level;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
