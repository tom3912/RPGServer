package me.tom.ServerPlugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.tom.ServerPlugin.Master;

public class DebugCommand implements CommandExecutor
{
	private Master plugin;
	
	public DebugCommand(Master plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("debugcommand").setExecutor(this);
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
			if(p.hasPermission("debugcommand.use"))  
			{
				Location loc = p.getLocation();
				World w = p.getWorld();
				p.sendMessage("Hello "+ ChatColor.GREEN + p.getName() + ChatColor.WHITE + " in " + w.getName() + " at" + " x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ());
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
