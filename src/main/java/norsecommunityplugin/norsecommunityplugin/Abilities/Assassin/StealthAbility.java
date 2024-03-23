package norsecommunityplugin.norsecommunityplugin.Abilities.Assassin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import norsecommunityplugin.norsecommunityplugin.Abilities.Ability;
import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.CooldownManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StealthAbility extends Ability {

    private static ConcurrentHashMap<Player, Boolean> stealthedPlayers = new ConcurrentHashMap<>();
    private final static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private BukkitTask messageTask = null;

    static {
        setupWeaponHidingListener(NorseCommunityPlugin.getPlugin());
    }

    public static void setupWeaponHidingListener(NorseCommunityPlugin plugin) {
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    // Get the entity ID from the packet
                    int entityId = event.getPacket().getIntegers().read(0);

                    // Try to find a player with the matching ID
                    Player subject = null;
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        if (p.getEntityId() == entityId) {
                            subject = p;
                            break;
                        }
                    }

                    // Proceed if we found a player and they are in the stealthedPlayers map
                    if (subject != null && stealthedPlayers.containsKey(subject.getUniqueId()) && stealthedPlayers.get(subject.getUniqueId())) {
                        // Depending on the version of ProtocolLib and Minecraft, the way to modify the packet will vary.
                        // Here's a generalized approach to clear the item:

                        // First, find out the slot being modified. This part might need adjustment for different MC versions.
                        // This example assumes you want to hide the main hand item.
                        int slot = event.getPacket().getIntegers().read(1); // This index might not be correct; adjust as needed.

                        // Check if the slot is for the main hand; you'll need to verify the slot index for your MC version.
                        if (slot == EnumWrappers.ItemSlot.MAINHAND.ordinal()) {
                            event.setCancelled(true); // One way to hide the item is to cancel the packet.
                        }
                    }
                }
            }
        });
    }

    public static boolean isStealthed(Player player) {
        return stealthedPlayers.containsKey(player);
    }

    public StealthAbility(Player player, NorseCommunityPlugin plugin) {
            super("Stealth", player, 50, 10, plugin);
            this.plugin = plugin;
        }

        @Override
        public void activate() {
            if (CooldownManager.isOnCooldown(player.getUniqueId(), this.name)) {
                long timeLeft = (CooldownManager.getCooldown(player.getUniqueId(), this.name) - System.currentTimeMillis()) / 1000;
                player.sendMessage("Ability is on cooldown for " + timeLeft + " seconds.");
                return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 1, false, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 2, false, false, false));
            stealthedPlayers.put(player, true);
            // Play sound and particles

            //Hide weapon in hand and tab list


            CooldownManager.setCooldown(player.getUniqueId(), this.name, this.cooldown);

            startStealthedMessageTask();
        }

        private void startStealthedMessageTask() {
            messageTask = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    player.sendMessage("Stealthed");
                }
            }, 0, 100);
        }

        @Override
        public void deactivate() {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.SPEED);

            stealthedPlayers.remove(player);
            if (messageTask != null) {
                messageTask.cancel();
                messageTask = null;
            }
        }
}
