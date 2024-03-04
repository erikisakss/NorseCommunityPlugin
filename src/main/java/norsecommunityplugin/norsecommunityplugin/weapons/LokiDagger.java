package norsecommunityplugin.norsecommunityplugin.weapons;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;



import java.util.UUID;


public class LokiDagger {

    public static ItemStack createLokiDagger(){
        ItemStack lokiDagger = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta lokiDaggerMeta = lokiDagger.getItemMeta();
        lokiDaggerMeta.setDisplayName("Loki's Dagger");
        lokiDaggerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lokiDaggerMeta.setUnbreakable(true);
        lokiDaggerMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier(UUID.randomUUID() , "Loki's Dagger", 50, AttributeModifier.Operation.ADD_NUMBER));
        lokiDaggerMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,
                new AttributeModifier(UUID.randomUUID(), "Loki's Dagger", 2.4, AttributeModifier.Operation.ADD_SCALAR));
        lokiDagger.setItemMeta(lokiDaggerMeta);
        return lokiDagger;
    }

    public static void registerLokiDagger(){
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerInteract(PlayerInteractEvent event) {
                Player player = event.getPlayer();
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.getType() == Material.DIAMOND_SWORD
                        && item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                        && item.getItemMeta().getDisplayName().equals("Loki's Dagger")) {
                    // Add custom behavior for Loki's Dagger here

                }
            }
        }, NorseCommunityPlugin.getPlugin(NorseCommunityPlugin.class));
    }
}
