package me.tom.ServerPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;

public class GiveWandCommand implements CommandExecutor
{
	private Master plugin;
	
	public GiveWandCommand(Master plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("givewand").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(!(sender instanceof Player)) 
		{
			sender.sendMessage("Not sure why the server is trying to type this command...");
			return true;
		}
		else 
		{
			Player p = (Player) sender;
			if(p.hasPermission("givewand.use"))  
			{
				p.getInventory().addItem(ItemManager.wand);
				return true;
			}
			else 
			{
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
				return false;
			}
		}
	}
}
