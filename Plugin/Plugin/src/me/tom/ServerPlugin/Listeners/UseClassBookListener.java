package me.tom.ServerPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;
import net.minecraft.world.entity.Entity;

public class UseClassBookListener implements Listener 
{
	private static Master plugin;
	
	public UseClassBookListener (Master plugin) 
	{
	this.plugin = plugin;
	
	Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler 
	public void onRightClick(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) 
		{
			if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
			{
				if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.classbook.getItemMeta()))
				{
					PersistentDataContainer data = player.getPersistentDataContainer();
					
					if(data.has(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING))
					{	
						if(data.get(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING) == "SelectClass")
						{
							player.openInventory(UseClassSelectListener.selectClassinv);
						}
						else 
						{
							player.sendMessage("I am not sure what this does?");
						}
					}
				}
				if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ItemManager.playermasteritem.getItemMeta().getDisplayName()))
				{
					if(JoinListener.getClass(player) == "Warrior") 
					{
						UseClassSelectListener.createWarriorIvn(player);
						player.openInventory(UseClassSelectListener.warriorInv);
					}
				}
			}
		}
	}
	
	@EventHandler 
	public void bookdrop(PlayerDropItemEvent event) 
	{
		Player player = event.getPlayer();
		Item item = event.getItemDrop();
		
		if(item.getItemStack().getItemMeta().equals(ItemManager.classbook.getItemMeta())) 
		{
			item.remove();
			player.getInventory().setItem(8, item.getItemStack());
		}
		
		if(item.getItemStack().getItemMeta().getDisplayName().equals(ItemManager.playermasteritem.getItemMeta().getDisplayName())) 
		{
			item.remove();
			player.getInventory().setItem(8, item.getItemStack());
		}
	}	

}