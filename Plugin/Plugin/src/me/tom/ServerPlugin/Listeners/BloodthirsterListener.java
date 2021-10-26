package me.tom.ServerPlugin.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.Message;

import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;
import me.tom.ServerPlugin.Utils.LoreManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BloodthirsterListener implements Listener 
{
	private static Master plugin;
		
	public BloodthirsterListener(Master plugin) 
	{
	this.plugin = plugin;
	
	Bukkit.getPluginManager().registerEvents(this, plugin);
	}
		
	@EventHandler 
	public void onLeftClick(EntityDamageByEntityEvent event) 
	{
		if(event.getDamager() instanceof Player)
		{
			Player player = (Player)event.getDamager();
			if(event.getEntity() instanceof LivingEntity) 
			{
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ItemManager.bloodthirster.getItemMeta().getDisplayName()))
					{
						double Health = player.getHealth();
						double Damage = event.getDamage()/2;
						double MaxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						if(Health + Damage >= MaxHealth) 
						{
							player.setHealth(MaxHealth);
						}
						else 
						{
							player.setHealth(Health + Damage);
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
		if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
		{
			if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ItemManager.bloodthirster.getItemMeta().getDisplayName()))
			{	
				if(player.hasPotionEffect(PotionEffectType.ABSORPTION) == false) 
				{
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND) 
					{
						if(!event.getClickedBlock().getBlockData().getMaterial().isInteractable()) 
						{
							ItemStack is = player.getInventory().getItemInMainHand();
							ItemMeta meta = is.getItemMeta();
											
							List<String> lore = new ArrayList<>();
							lore = player.getInventory().getItemInMainHand().getItemMeta().getLore();
										
							Integer[] returnvalues = LoreManager.getSoulValue(player,lore);
							if (returnvalues[0] > 0) 
							{
								player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200+(returnvalues[0]*8), Math.round(returnvalues[0]/5)-Math.round(returnvalues[0]/50)));
								lore.set(returnvalues[1], "§4Souls: [0/50]");
							}	
							meta.setLore(lore);
							is.setItemMeta(meta);
						}
							
					}
					if(event.getAction() == Action.RIGHT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND) 
					{
						ItemStack is = player.getInventory().getItemInMainHand();
						ItemMeta meta = is.getItemMeta();
											
						List<String> lore = new ArrayList<>();
						lore = player.getInventory().getItemInMainHand().getItemMeta().getLore();
										
						Integer[] returnvalues = LoreManager.getSoulValue(player,lore);
						if (returnvalues[0] > 0) 
						{
							player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200+(returnvalues[0]*8), Math.round(returnvalues[0]/5)-Math.round(returnvalues[0]/50)));
							lore.set(returnvalues[1], "§4Souls: [0/50]");
						}	
						meta.setLore(lore);
						is.setItemMeta(meta);
					}
				}
			}
		}

	}
	@EventHandler 
	public void onKill(EntityDeathEvent event) 
	{
		if(event.getEntity() instanceof LivingEntity) 
		{
			LivingEntity entity = event.getEntity();
			if(entity.getKiller() instanceof Player && entity.getKiller() != null)
			{
				Player player = (Player)entity.getKiller();
				if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
				{
					if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ItemManager.bloodthirster.getItemMeta().getDisplayName()))
					{
						ItemStack is = player.getInventory().getItemInMainHand();
						ItemMeta meta = is.getItemMeta();
						
						List<String> lore = new ArrayList<>();
						lore = player.getInventory().getItemInMainHand().getItemMeta().getLore();
						
						Integer[] returnvalues = LoreManager.getSoulValue(player,lore);
						
						returnvalues[0]++;
						
						if(returnvalues[0] < 50) 
						{
							lore.set(returnvalues[1], "§4Souls: [" + returnvalues[0] + "/50]");
						}
						else 
						{
							lore.set(returnvalues[1], "§4Souls: [Max]");
						}	
						meta.setLore(lore);
						is.setItemMeta(meta);
					}
				}
			}
		}
	}				
}
