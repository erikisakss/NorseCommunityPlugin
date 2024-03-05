package norsecommunityplugin.norsecommunityplugin.Abilities.Warrior;

import norsecommunityplugin.norsecommunityplugin.Abilities.Ability;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class StompAbility extends Ability {



        public StompAbility(Player player, NorseCommunityPlugin plugin) {
            super("Stomp", player, 50, 10, plugin);
        }

        @Override
        public void activate() {
            if (isOnCooldown()) {
                long timeLeft = (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
                player.sendMessage("Ability is on cooldown for " + timeLeft + " seconds.");
                return;
            }
            player.getWorld().getNearbyEntities(player.getLocation(), 4, 4, 4).forEach(entity -> {
                if (entity instanceof LivingEntity && entity != player) {
                    Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                    entity.setVelocity(direction.multiply(-1));
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1, false, false, false));
                }
            });
            // Play sound and particles
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            startCooldown();

        }
}
