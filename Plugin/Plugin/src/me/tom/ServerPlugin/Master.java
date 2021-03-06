package me.tom.ServerPlugin;

import java.security.PublicKey;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.tom.ServerPlugin.Commands.DebugCommand;
import me.tom.ServerPlugin.Commands.GiveBloodthirsterCommand;
import me.tom.ServerPlugin.Commands.GiveWandCommand;
import me.tom.ServerPlugin.Items.ItemManager;
import me.tom.ServerPlugin.Listeners.BloodthirsterListener;
import me.tom.ServerPlugin.Listeners.JoinListener;
import me.tom.ServerPlugin.Listeners.UseClassBookListener;
import me.tom.ServerPlugin.Listeners.UseClassSelectListener;
import me.tom.ServerPlugin.Listeners.WandUseListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.stats.ServerStatisticManager;

public class Master extends JavaPlugin 
{
	public Master plugin;
	public Integer ManaTimer = 0;
	public Integer UpDateTimer = 0;
	public Integer HealTimer = 0;
	public Integer HologramCount = 0;
	
	@Override 
	public void onEnable() 
	{
		saveDefaultConfig();		
		plugin = this;

		World world = Bukkit.getServer().getWorlds().get(0);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		world.setGameRule(GameRule.NATURAL_REGENERATION, false);
		world.setGameRule(GameRule.MOB_GRIEFING, false);
		
		//Items
		ItemManager.init();
		
		//Commands
		new DebugCommand(this);
		new GiveWandCommand(this);
		new GiveBloodthirsterCommand(this);
		
		//Listeners
		new BloodthirsterListener(this);
		new JoinListener(this);		
		new WandUseListener(this);		
		new UseClassBookListener(this);
		new UseClassSelectListener(this);
		
		new BukkitRunnable()
		{
		    @Override
		    public void run()
		    {
		    	HealTimer += 1;
		    	
		    	if(Bukkit.getOnlinePlayers().equals(null)) 
		    	{
		    		return;
		    	}
		    	
		    	for (Player online : Bukkit.getOnlinePlayers()) 
				{
			    	String gui = ""; 
		    		PersistentDataContainer data = online.getPersistentDataContainer();
		    		//Check for essential items
		    		if (online.getInventory().getItem(8) == null) 
		    		{
		    			if (JoinListener.getClass(online) == "None") 
			    		{
			    			online.getInventory().setItem(8, ItemManager.classbook);
			    	    }
		    			if (JoinListener.getClass(online) != "None") 
			    		{
			    			online.getInventory().setItem(8, ItemManager.playermasteritem);
			    			JoinListener.setPlayerData(online);
			    		}
		    		}
		    		else 
		    		{
			    		if (!online.getInventory().getItem(8).getItemMeta().getDisplayName().equals(ItemManager.classbook.getItemMeta().getDisplayName()) && JoinListener.getClass(online) == "None") 
			    		{
			    			online.getInventory().setItem(8, ItemManager.classbook);
			    			
			    		}
			    		
			    		if (!online.getInventory().getItem(8).getItemMeta().getDisplayName().equals(ItemManager.playermasteritem.getItemMeta().getDisplayName()) && JoinListener.getClass(online) != "None") 
			    		{
			    			online.getInventory().setItem(8, ItemManager.playermasteritem);
			    			JoinListener.setPlayerData(online);			    			
			    		}
		    		}	
		 
		    		//Generate mana
		    		if (JoinListener.getClass(online) != "None") 
					{
		    			Integer mana = JoinListener.getPlayerMana(online);
		    			Integer maxmana = JoinListener.getPlayerMaxMana(online);
		    		
		    			if(mana < maxmana) 
		    			{
		    				JoinListener.addMana(online);
		    				ManaTimer = 0;
		    			}		    				   				    			
		    			
			    		//XPSYSTEM
			    		if (JoinListener.getPlayerXP(online) >= JoinListener.getPlayerLevel(online)*JoinListener.getPlayerLevel(online)*20 && JoinListener.getPlayerLevel(online) < 20) 
						{
			    			JoinListener.levelUp(online);
						}
			    		
		    			Integer level = JoinListener.getPlayerLevel(online);
			    	
						
						double bHealth = 20;
						double bArmour = 0;
						double bAttack = 2;			
						double bAttackS = 4;	
			    		
			    		//WARRIOR ATTRIBUTES
			    		if(JoinListener.getClass(online) == "Warrior") 
			    		{
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill11"), PersistentDataType.INTEGER) == 1)
							{
			    				bAttack += 1;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill12"), PersistentDataType.INTEGER) == 1)
							{
			    				bAttack += 2;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill13"), PersistentDataType.INTEGER) == 1)
							{
			    				bAttackS += 1;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill14"), PersistentDataType.INTEGER) == 1)
							{
			    				bHealth += 1;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill15"), PersistentDataType.INTEGER) == 1)
							{
			    				bHealth += 1;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill16"), PersistentDataType.INTEGER) == 1)
							{
			    				bHealth += 2;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill17"), PersistentDataType.INTEGER) == 1)
							{
			    				bHealth += 3;
							}
			    			if(data.get(new NamespacedKey(plugin, "WarriorSkill18"), PersistentDataType.INTEGER) == 1)
							{
			    				bArmour += 1;
							}   	
			    			
			    			//WARRIOR ATTRIBUTES
			    			online.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(bHealth + Math.round(level/2) + 3);
			    			online.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(bArmour);
			    			online.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(bAttack + Math.round(level/5));
			    			online.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(bAttackS);
			    			JoinListener.setMaxMana(online, level);
			    			
			    			double health = online.getHealth();
							double mhealth = online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
						
							if(data.get(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER) == 0 && data.get(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER) == 0 && data.get(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER) == 0) 
							{				
								if(health/mhealth <= 0.25)
								{
									online.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
									online.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0));
								}
							}
								
							if(data.get(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER) == 0 && data.get(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER) == 0)
							{
								if(health/mhealth <= 0.3)
								{
									online.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
									online.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1));
								}
							}
								
							if(data.get(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER) == 0) 
							{
								if(health/mhealth <= 0.4) 
								{
									online.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
									online.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 2));
								}
							}
								
							if(data.get(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER) == 1) 
							{
								if(health/mhealth <= 0.5) 
								{
									online.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
									online.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 3));
								}
							}
							
							if(data.get(new NamespacedKey(plugin, "WarriorSkill5"), PersistentDataType.INTEGER) == 1) 
							{
								if(data.get(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER) >= 300 && UpDateTimer >= 20)
								{
									if(health/mhealth <= 0.25) 
									{
										online.removePotionEffect(PotionEffectType.REGENERATION);
										online.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 4));
										online.removePotionEffect(PotionEffectType.ABSORPTION);
										online.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 300, 4));
										data.set(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER, 0);															
									}
								}
								else if(data.get(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER) < 300 && UpDateTimer >= 20)
								{
									data.set(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER) + 1);																	
								}
							}
			    		}

					}   			    			
		    		//Send player gui string above hotbar
		    		
		    		double health = online.getHealth();
		    		Long vhealth = Math.round(health);
		    		double mhealth = online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		    		Long vmhealth = Math.round(mhealth);
	    			Integer mana = JoinListener.getPlayerMana(online);
	    			Integer maxmana = JoinListener.getPlayerMaxMana(online);
	    			Integer level = JoinListener.getPlayerLevel(online);
	    			
		    		gui += ChatColor.GOLD + " Lvl " + ChatColor.WHITE + level + ChatColor.GRAY + "  : " + ChatColor.RED + " ??? " + ChatColor.WHITE + vhealth + "/" + vmhealth + ChatColor.GRAY + "  : " + ChatColor.AQUA + " ??? " + ChatColor.WHITE + mana + "/" + maxmana;

		    		List<LivingEntity> entities = world.getLivingEntities();
		    		for (LivingEntity entity : entities)
		    		{
		    			if(entity instanceof ArmorStand) 
		    			{
		    				ArmorStand hologram = (ArmorStand) entity;
		    				PersistentDataContainer datah = hologram.getPersistentDataContainer();
		    					
		    				if(datah.has(new NamespacedKey(plugin, "isHologram"), PersistentDataType.INTEGER)) 
		    				{
		    				    HologramCount += 1;
		    				    if(HologramCount > 20) 
		    				    {
			    					hologram.remove();
			    					HologramCount = 0;
		    				    }
		    				}
		    			}
		    		}
		    					    		
		    		if(HealTimer >= 5) 
		    		{
		    			if (online.getHealth() < online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) 
		    			{
		    				try
		    				{
		    					online.setHealth(online.getHealth() + 1);			
		    				}
		    				catch (Exception e)
		    				{
		    					online.setHealth(online.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		    				}
		    			}
		    			HealTimer = 0;
		    		}
		    		
	
		    			online.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(gui));	
		    			JoinListener.createBoard(online);
		    			UpDateTimer = 0;  
		    			if(data.has(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING)) 
		    			{
		    				if(data.get(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING) != "None" && data.get(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER) != null) 
		    				{		    					
		    					String activeQuest = data.get(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING);
		    					Integer questTimer = data.get(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER);
		    					String Region = data.get(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING);
		    					
		    					if(activeQuest == "ReturnToVillage") 
	    						{	    						
		    						if(questTimer == 0) 
		    						{
		    							online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);	    		
		    							online.sendMessage("Jason: I have not seen you around here before...");
		    						}
		    						
		    						if(questTimer == 3) 
		    						{
		    							online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);	    		
		    							online.sendMessage("Jason: You look exhausted, there is a village just up the road...");
		    						}
		    						
		    						if(questTimer == 6) 
		    						{
		    							online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);	    		
		    							online.sendMessage("Jason: Just follow the path and be carefull!");
		    						}
		    						
		    						if(questTimer == 9) 
		    						{
		    							online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);	    		
		    							online.sendTitle(" ", ChatColor.GOLD + "New Quest: Escape the forrest!" , 20, 40, 20);
		    						}
		    						
		    						//Reached Village
		    						if(Region == "The Village") 
		    						{
		    							online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);	
		    							online.sendTitle(" ", ChatColor.GREEN + "Quest Complete: Escape the forrest!" , 20, 40, 20);
		    							data.set(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER, -3);
		    							data.set(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING, "ReachJarvis");
		    						}
		    					}
		    					if(activeQuest == "ReachJarvis") 
	    						{
		    						if(questTimer == 0) 
		    						{
		    							online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);	    		
		    							online.sendTitle(" ", ChatColor.GOLD + "New Quest: Talk to Jarvis" , 20, 40, 20);
		    						}
	    						}
		    					
		    					data.set(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER) + 1);
		    					
		    				}
		    			}
		    		}
				
		    }
		}.runTaskTimer(this, 0L, 20L);
	}	
}
