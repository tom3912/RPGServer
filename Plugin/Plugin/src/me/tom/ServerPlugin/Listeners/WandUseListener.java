package me.tom.ServerPlugin.Listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;

public class WandUseListener implements Listener 
{
	private static Master plugin;
	
	public WandUseListener(Master plugin) 
	{
	this.plugin = plugin;
	
	Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler 
	public void onHit(EntityDamageByEntityEvent event) 
	{
		if(event.getDamager() instanceof Player)
		{
			Player player = (Player)event.getDamager();
			if(event.getEntity() instanceof LivingEntity) 
			{
				LivingEntity entity = (LivingEntity)event.getEntity();	
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.wand.getItemMeta()))
					{
						if(entity.getScoreboardTags().contains("adminwand"))	
						{
							entity.setGlowing(false);
							entity.setHealth(entity.getHealth()+event.getDamage());
							entity.removeScoreboardTag("adminwand");
						}
						else 
						{
					    entity.setGlowing(true);
						entity.addScoreboardTag("adminwand");
						entity.setHealth(entity.getHealth()+event.getDamage());
						}
					}
				}
			}
		}
	}
	
	@EventHandler 
	public void onRightClick(PlayerInteractEvent event) 
	{
		Player player = event.getPlayer();
		World world = player.getWorld();
		Location location = player.getLocation();
		boolean c = player.getScoreboardTags().contains("leftclick");
		if (player.isSneaking() == false) 
		{
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) 
			{
				Location blocklocation = event.getClickedBlock().getLocation();
				blocklocation.setY(blocklocation.getBlockY()+1);
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.wand.getItemMeta()))
					{
						for (Iterator<LivingEntity> e = world.getLivingEntities().iterator(); e.hasNext();)
						{
				            LivingEntity entity = e.next();
				            if (entity.getScoreboardTags().contains("adminwand"))
				            {
				                entity.teleport(blocklocation);
				                entity.removeScoreboardTag("adminwand");
				                entity.setGlowing(false);
				            }
						}
						player.removeScoreboardTag("leftclick");
					}
				}		
			}
			if(event.getAction() == Action.RIGHT_CLICK_AIR) 
			{
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.wand.getItemMeta()))
					{
						for (Iterator<LivingEntity> e = world.getLivingEntities().iterator(); e.hasNext();)
						{
				            LivingEntity entity = e.next();
				            if (entity.getScoreboardTags().contains("adminwand"))
				            {
				                entity.teleport(location);
				                entity.removeScoreboardTag("adminwand");
				                entity.setGlowing(false);
				            }
						}
						player.removeScoreboardTag("leftclick");
					}
				}		
			}
		}
		if (player.isSneaking() == true) 
		{
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) 
			{
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.wand.getItemMeta()))
					{
						location.setY(-100);
						for (Iterator<LivingEntity> e = world.getLivingEntities().iterator(); e.hasNext();)
						{
				            LivingEntity entity = e.next();
				            if (entity.getScoreboardTags().contains("adminwand"))
				            {
				                entity.teleport(location);
				                entity.damage(entity.getHealth());
				            }
						}
						player.removeScoreboardTag("leftclick");
					}
				}		
			}
			if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) 
			{
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.wand.getItemMeta()))
					{
						if (c == true) 
						{
							player.removeScoreboardTag("leftclick");
						}
						else if (c == false) 
						{
							player.addScoreboardTag("leftclick");
						}
						
						for (Iterator<LivingEntity> e = world.getLivingEntities().iterator(); e.hasNext();)
						{
				            LivingEntity entity = e.next();
				            if(c==true) 
				            {
					            if (entity instanceof Player)
					            {
					                entity.removeScoreboardTag("adminwand");
					                entity.setGlowing(false);
					            }
					            else 
					            {
					                entity.removeScoreboardTag("adminwand");
					                entity.setGlowing(false);
					            }
				            }
				            else if (c==false) 
				            {
					            if (entity instanceof Player)
					            {
					                entity.removeScoreboardTag("adminwand");
					                entity.setGlowing(false);
					            }	
					            else 
					            {
					                entity.addScoreboardTag("adminwand");
					                entity.setGlowing(true);
					            }
				            }
						}
					}
				}		
			}	
		}		
	}
}
