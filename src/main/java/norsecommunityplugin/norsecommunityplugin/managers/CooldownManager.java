package norsecommunityplugin.norsecommunityplugin.managers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    private static final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public static boolean isOnCooldown(UUID playerId, String abilityName) {
        return cooldowns.getOrDefault(playerId, new HashMap<>()).getOrDefault(abilityName, 0L) > System.currentTimeMillis();
    }

    public static void setCooldown(UUID playerId, String abilityName, long duration) {
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>()).put(abilityName, System.currentTimeMillis() + duration * 1000);
    }

    public static void removeCooldown(UUID playerId, String abilityName) {
        if (cooldowns.containsKey(playerId)) {
            cooldowns.get(playerId).remove(abilityName);
        }
    }

    public static long getCooldown(UUID playerId, String abilityName) {
        return cooldowns.getOrDefault(playerId, new HashMap<>()).getOrDefault(abilityName, 0L);
    }

    public static void cleanupExpiredCooldowns() {
        long currentTime = System.currentTimeMillis();
        cooldowns.forEach((uuid, abilities) -> abilities.entrySet().removeIf(entry -> entry.getValue() <= currentTime));
    }
}
