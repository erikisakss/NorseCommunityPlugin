package norsecommunityplugin.norsecommunityplugin.commands.Testing;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import norsecommunityplugin.norsecommunityplugin.managers.ItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveItem implements CommandExecutor {
    private final NorseCommunityPlugin plugin;

    public GiveItem(NorseCommunityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /giveitem <item_name> <amount>");
            return true;
        }

        String itemName = args[0];
        Player player = (Player) sender;
        ItemManager itemManager = ItemManager.getInstance(plugin);

        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("The amount must be a number.");
            return true;
        }

        // Get the ItemStack for the item name provided
        ItemStack item = itemManager.getItem(itemName);

        if (item == null) {
            player.sendMessage("The item " + itemName + " does not exist.");
            return true;
        }

        item.setAmount(amount);

        // Give the item to the player
        player.getInventory().addItem(item);
        player.sendMessage("Given " + itemName + " to " + player.getName());
        return true;
    }
}
