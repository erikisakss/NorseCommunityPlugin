package norsecommunityplugin.norsecommunityplugin.commands;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import static norsecommunityplugin.norsecommunityplugin.Utils.Utils.color;

public  class ClassMenu implements Listener, CommandExecutor {


    private String classMenuName = color("&eChoose your class");
    NorseCommunityPlugin plugin;


    public ClassMenu(NorseCommunityPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            int slot = 10;

            Player player = (Player) sender;
            Inventory classMenu = Bukkit.createInventory(player, 9*3, classMenuName);



            player.openInventory(classMenu);

        }
        return true;
    }

}
