package norsecommunityplugin.norsecommunityplugin.Items;

public class ScrollBlueprint extends ItemBlueprint{

    private String scrollType;
    private String itemGrade;
    private String stat;

    public ScrollBlueprint(String name, String type, String rarity, String material, int level, int requiredLevel, double price, String scrollType, String itemGrade, String stat) {
        super(name, type, rarity, material, requiredLevel, level, price);
        this.scrollType = scrollType;
        this.itemGrade = itemGrade;
        this.stat = stat;
    }


    public String getScrollType() {
        return scrollType;
    }

    public String getItemGrade() {
        return itemGrade;
    }

    public String getStat() {
        return stat;
    }

    public void setScrollType(String scrollType) {
        this.scrollType = scrollType;
    }

    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }


}
