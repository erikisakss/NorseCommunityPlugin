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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StealthAbility extends Ability {

    private static ConcurrentHashMap<Player, Boolean> stealthedPlayers = new ConcurrentHashMap<>();
    private static ProtocolManager protocolManager;
    private BukkitTask messageTask = null;
    private static boolean listenerSetup = false;

    private static void ensureListenerSetup(NorseCommunityPlugin plugin) {
        if (listenerSetup || plugin == null) return;

        protocolManager = ProtocolLibrary.getProtocolManager();
        setupWeaponHidingListener(plugin);
        listenerSetup = true;
    }

    public static void setupWeaponHidingListener(NorseCommunityPlugin plugin) {

        if (protocolManager == null) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }

        Bukkit.getLogger().info("Setting up StealthAbility equipment hiding listener");

        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                int entityId = packet.getIntegers().read(0);

                // Leta efter en stealthad spelare med detta entity ID
                Player observer = event.getPlayer(); // Spelaren som kommer få paketet
                Player stealthedPlayer = null;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getEntityId() == entityId && stealthedPlayers.containsKey(player) && stealthedPlayers.get(player)) {
                        Bukkit.getLogger().info("Caught equipment packet for stealthed player: " + player.getName());
                        stealthedPlayer = player;
                        break;
                    }
                }

                // Om paketet är för en stealthad spelare och mottagaren inte är samma spelare
                if (stealthedPlayer != null && !observer.equals(stealthedPlayer)) {
                    // I ProtocolLib 5.x används en lista av par för utrustningsslots
                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment = packet.getSlotStackPairLists().read(0);

                    // Skapa en ny, tom lista för utrustning
                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> emptyEquipment = new ArrayList<>();

                    // För varje utrustning, lägg till en tom version (null eller AIR)
                    for (Pair<EnumWrappers.ItemSlot, ItemStack> pair : equipment) {
                        emptyEquipment.add(new Pair<>(pair.getFirst(), new ItemStack(Material.AIR)));
                    }

                    // Sätt den tomma listan till paketet
                    packet.getSlotStackPairLists().write(0, emptyEquipment);
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
            ensureListenerSetup(plugin);
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



            forceEquipmentUpdate(player);

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

            forceEquipmentUpdate(player);

            if (messageTask != null) {
                messageTask.cancel();
                messageTask = null;
            }
        }

    private void forceEquipmentUpdate(Player player) {
        // Skapa en task som körs nästa tick för att ge din pakethändelse en chans att registrera sig
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Metod 1: Använd ProtocolLib för att manuellt skicka utrustningspaket
            for (Player observer : Bukkit.getOnlinePlayers()) {
                if (observer.equals(player)) continue;

                try {
                    // Skapa paket för varje utrustningsslot
                    PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
                    packet.getIntegers().write(0, player.getEntityId());

                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipment = new ArrayList<>();
                    // Lägg till spelarens faktiska utrustning (eller tom för stealthade spelare)
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, player.getInventory().getItemInOffHand()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.FEET, player.getInventory().getBoots()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, player.getInventory().getLeggings()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, player.getInventory().getChestplate()));
                    equipment.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, player.getInventory().getHelmet()));

                    packet.getSlotStackPairLists().write(0, equipment);

                    // Skicka paketet
                    protocolManager.sendServerPacket(observer, packet);
                } catch (Exception e) {
                    Bukkit.getLogger().severe("Error sending equipment packet: " + e.getMessage());
                }
            }

            // Metod 2 (reserv): Simulera en inventorieändring för att få Minecraft att skicka utrustningspaketen
            // Detta är en fallback om den direkta paketskickningen inte fungerar
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            int currentSlot = player.getInventory().getHeldItemSlot();

            // Byta till en annan slot och sedan tillbaka
            int newSlot = (currentSlot + 1) % 9;
            player.getInventory().setHeldItemSlot(newSlot);

            // Byt tillbaka nästa tick
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.getInventory().setHeldItemSlot(currentSlot);
            }, 1L);
        }, 1L);
    }
}
