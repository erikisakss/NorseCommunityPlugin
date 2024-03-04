package norsecommunityplugin.norsecommunityplugin.commands;

import norsecommunityplugin.norsecommunityplugin.NorseCommunityPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import static norsecommunityplugin.norsecommunityplugin.Utils.Utils.color;

//Lore = description of item
public class NationMenu implements Listener, CommandExecutor{
    private String nationMenuName = color("&eChoose your nation");



    public NationMenu(NorseCommunityPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    @Override
    //This is the event that is called when a player types /nationmenu
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)){
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;
        int slot = 11;
        Inventory nationMenu = Bukkit.createInventory(player,9*3,nationMenuName);

        //This is the item that is added to the inventory
        //This is the item that is added to the inventory





        player.openInventory(nationMenu);

        return true;
    }
    //This is a method that is used to set the name and lore of an item


}
