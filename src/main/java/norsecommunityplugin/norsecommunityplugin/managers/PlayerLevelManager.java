package norsecommunityplugin.norsecommunityplugin.managers;

public class PlayerLevelManager {

    private int Level;
    private int XP;

    public PlayerLevelManager(int level, int xp) {
        Level = level;
        XP = xp;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getXP() {
        return XP;
    }

    public void setXP(int xp) {
        XP = xp;
    }
}
