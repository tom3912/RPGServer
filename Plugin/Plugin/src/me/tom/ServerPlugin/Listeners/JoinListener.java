package me.tom.ServerPlugin.Listeners;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import io.netty.handler.codec.redis.SimpleStringRedisMessage;
import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;
import me.tom.ServerPlugin.Utils.Utils;

public class JoinListener implements Listener 
{
	private static Master plugin;
	
	public JoinListener(Master plugin) 
	{
	this.plugin = plugin;
	
	Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler 
	public void onJoin(PlayerJoinEvent e) 
	{
		Player p = e.getPlayer();
		
		if(!p.hasPlayedBefore())
		{
			Bukkit.broadcastMessage(Utils.chat(plugin.getConfig().getString("firstJoin_message").replace("<player>", p.getName())));
			setPlayerData(p);
			p.teleport(new Location(p.getWorld(), -4, 69, -4));
		}
		else 
		{
			Bukkit.broadcastMessage(Utils.chat(plugin.getConfig().getString("join_message").replace("<player>", p.getName())));
			setPlayerDataRejoin(p);
		}
		
		createBoard(p);
		
		if(!p.getInventory().contains(ItemManager.classbook, 1) && getClass(p).equals("None")) 
		{
			p.getInventory().setItem(8, ItemManager.classbook);
		}	
		
		if(!p.getInventory().contains(ItemManager.playermasteritem, 1) && !getClass(p).equals("None")) 
		{
			p.getInventory().setItem(8, ItemManager.playermasteritem);
		}	
	}
	
	//WARRIOR SKILLS 6-10
	@EventHandler 
	public void onSpawn(EntitySpawnEvent event) 
	{
		if(!event.getEntity().equals(null)) 
		{
			if(event.getEntity() instanceof LivingEntity && !event.getEntity().getType().equals(EntityType.ARMOR_STAND) && !event.getEntity().getScoreboardTags().contains("Essential")) 
			{
				Location loc = event.getEntity().getLocation();
				double x = loc.getX();
				double z = loc.getZ();
				
				if(x < 120 && z < 100 && x > -20 && z > -20 || x < 240 && z < 160 && x > 120 && z > 0)
				{
					event.setCancelled(true);
					return;
				}
				
				Integer PlayerCount = Bukkit.getOnlinePlayers().size();
				Integer TotalLevel = 0;
				Integer avLevel = 1;
				for (Player online : Bukkit.getOnlinePlayers()) 
				{
					if(JoinListener.getClass(online) != "None") 
					{
						PersistentDataContainer data = online.getPersistentDataContainer();
					
						TotalLevel += data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER);
					}
				}
				if(PlayerCount > 0) 
				{
					avLevel = TotalLevel/PlayerCount;
				}
				else 
				{
					avLevel  = 1;
				}
				LivingEntity entity = (LivingEntity) event.getEntity();
				String Name = entity.getType().toString();
				String vName = getName(Name);
				Integer bHealth = Math.toIntExact(Math.round(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));
					
				int randomNum = ThreadLocalRandom.current().nextInt(1, Math.round((avLevel/2)*(avLevel/5) + avLevel*2)+21);
				PersistentDataContainer data = entity.getPersistentDataContainer();
				data.set(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER, randomNum);
				data.set(new NamespacedKey(plugin, "Name"), PersistentDataType.STRING, Name);
				entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(bHealth + (bHealth*randomNum)/50);
				entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
				entity.setCustomNameVisible(true);		
				entity.setCustomName(ChatColor.GOLD + "Lvl " + ChatColor.WHITE + randomNum + " " + ChatColor.YELLOW + vName + ChatColor.RED + " ??? " + ChatColor.WHITE + Math.round(entity.getHealth()) + "/" + Math.round(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));		
			}
		}
	}
	
	public String getName(String Name) 
	{
		String lower = Name.toLowerCase();
		String repl = lower.replace("_", " ");
		String first = repl.substring(0,1);
		String remLetStr = repl.substring(1);
		String up = first.toUpperCase();
		return up + remLetStr;
	}
	
	@EventHandler 
	public void onEntityInteract(PlayerInteractEvent event) 
	{

		if (event.getAction().equals(Action.PHYSICAL) && event.getClickedBlock().getType().equals(Material.FARMLAND)) 
		{	
			PersistentDataContainer data = event.getPlayer().getPersistentDataContainer();
		    if(data.get(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING) != "Wilderness" && !event.getPlayer().isOp()) 
		    {
		    	event.setCancelled(true);
		    	event.getPlayer().sendMessage(ChatColor.RED + "Cannot trample crops in this region");
		    	return;
		    }
		}		
	}
	
	@EventHandler 
	public void onVillagerClick(PlayerInteractEntityEvent event) 
	{
		if (event.getRightClicked() instanceof LivingEntity) 
		{	
			LivingEntity entity = (LivingEntity) event.getRightClicked();
			PersistentDataContainer data = event.getPlayer().getPersistentDataContainer();		
			if(entity.getScoreboardTags().contains("Essential")) 
			{
				if(entity.getScoreboardTags().contains("Jason")) 
				{	
					if(data.has(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING) && data.has(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER)) 
					{
						String activeQuest = data.get(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING);
						int questTimer = data.get(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER);
						if(activeQuest == "None") 
						{
							data.set(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING, "ReturnToVillage");
							data.set(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER, 0);
						}
						else if(activeQuest == "ReturnToVillage" && questTimer > 9)
						{
							event.getPlayer().sendMessage("Jason: Good luck!");
						}
						else if(activeQuest == "ReturnToVillage" && questTimer <= 9)
						{
							return;
						}
						else
						{
							event.getPlayer().sendMessage("Are you lost?");
						}
					}
					else 
					{
						event.getPlayer().sendMessage("Jason: You have broken the game!");
					}
				}
				
				if(entity.getScoreboardTags().contains("Jeb")) 
				{	
					event.getPlayer().sendMessage("Hi, welcome to my shop!");
				}
				
				if(entity.getScoreboardTags().contains("Jonathan")) 
				{	
					event.getPlayer().sendMessage("Hello there, how can I help you!");
				}
				
				if(entity.getScoreboardTags().contains("Jake")) 
				{	
					event.getPlayer().sendMessage("I only sell the finest!");
				}
			}
		}		
	}
	
	@EventHandler 
	public void breakSpawn(BlockBreakEvent event) 
	{
		PersistentDataContainer data = event.getPlayer().getPersistentDataContainer();
	    if(data.get(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING) != "Wilderness" && !event.getPlayer().isOp()) 
	    {

	    		event.setCancelled(true);
	    		event.getPlayer().sendMessage(ChatColor.RED + "Cannot break blocks in this region");
	    		return;
	    	
	    }
		if(event.getBlock().getType().equals(Material.SPAWNER)) 
		{
			Player player = event.getPlayer();
			if (!player.getInventory().getItemInMainHand().equals(null)) 
			{			
				ItemStack is = player.getInventory().getItemInMainHand();
				if(is.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)); 
				{
					ItemStack spawner = new ItemStack(Material.SPAWNER);
					
					Block block = event.getBlock();					
					CreatureSpawner cs = (CreatureSpawner) block.getState();
					
					ItemMeta meta = spawner.getItemMeta();
					String name = getName(cs.getSpawnedType().toString());
					meta.setDisplayName(ChatColor.WHITE + name + " Spawner");
					spawner.setItemMeta(meta);
					
					BlockStateMeta blockMeta = (BlockStateMeta) spawner.getItemMeta();
					blockMeta.setBlockState(cs);				
					spawner.setItemMeta(blockMeta);		

					player.getInventory().addItem(spawner);
				}				
			}
		}
	}
	
	@EventHandler 
	public void placeSpawn(BlockPlaceEvent event) 
	{
		PersistentDataContainer data = event.getPlayer().getPersistentDataContainer();
	    if(data.get(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING) != "Wilderness" && !event.getPlayer().isOp()) 
	    {
	    	event.setCancelled(true);
	    	event.getPlayer().sendMessage(ChatColor.RED + "Cannot place blocks in this region");
	    	return;
	    }
		if(event.getBlock().getType().equals(Material.SPAWNER)) 
		{
				
			BlockState block = event.getBlockPlaced().getState();
			CreatureSpawner creatureSpawnerBLOCK = (CreatureSpawner) block;
				
			ItemStack is = event.getItemInHand();
			BlockStateMeta blockstate = (BlockStateMeta) is.getItemMeta();			
			CreatureSpawner creatureSpawnerITEM = (CreatureSpawner) blockstate.getBlockState();
			
			creatureSpawnerBLOCK.setSpawnedType(creatureSpawnerITEM.getSpawnedType());
			block.update();
			
		}
	}
	
	//WARRIOR SKILLS 6-10
		@EventHandler 
		public void entityDamaged(EntityDamageEvent event) 
		{
			if(event.getCause().equals(null)) 
			{
				return;
			}
			if(event.getEntity() instanceof LivingEntity && !event.getEntity().getType().equals(EntityType.ARMOR_STAND) && !event.getEntity().getType().equals(EntityType.PLAYER)) 
			{
				LivingEntity entity = (LivingEntity) event.getEntity();
				PersistentDataContainer data = entity.getPersistentDataContainer();
				
				Integer level = data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER);
				String Name = data.get(new NamespacedKey(plugin, "Name"), PersistentDataType.STRING);
				String vName = getName(Name);
				
				double damage = event.getDamage();
				Integer vdamage = Math.toIntExact(Math.round(damage));
				
				Integer health = Math.toIntExact(Math.round(entity.getHealth())) - vdamage;
				
				if(health < 0) 
				{
					health = 0;
				}
				
				entity.setCustomName(ChatColor.GOLD + "Lvl " + ChatColor.WHITE + level + " " + ChatColor.YELLOW + vName + ChatColor.RED + " ??? " + ChatColor.WHITE + health + "/" + Math.round(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()));	
			}
		}
	
	@EventHandler 
	//WARRIOR SKILL4
	public void Vamprism(EntityDamageByEntityEvent event) 
	{
		if(event.getDamager() instanceof Player)
		{
			Player player = (Player)event.getDamager();
			if(event.getEntity() instanceof LivingEntity) 
			{
				if(player.getInventory().getItem(8) != null) 
				{
					if(player.getInventory().getItem(8).getItemMeta().getDisplayName().equals((ItemManager.playermasteritem.getItemMeta().getDisplayName())) && getClass(player).equals("Warrior")) 
					{
						PersistentDataContainer data = player.getPersistentDataContainer();
										
						if(data.get(new NamespacedKey(plugin, "WarriorSkill4"), PersistentDataType.INTEGER) == 1) 
						{
							double Health = player.getHealth();
							double Damage = event.getDamage()/4;
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
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player)event.getEntity();
			if (player.getInventory().getItem(player.getInventory().getHeldItemSlot()) != null) 
			{
				if(getClass(player).equals("Warrior")) 
				{
					PersistentDataContainer data = player.getPersistentDataContainer();
											
					if(event.getDamager() instanceof LivingEntity && !event.getDamager().equals(null)) 
					{
						LivingEntity entity = (LivingEntity) event.getDamager();
						if(data.get(new NamespacedKey(plugin, "WarriorSkill10"), PersistentDataType.INTEGER) == 1)
						{
							entity.removePotionEffect(PotionEffectType.SLOW);
							entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));				
						}
					}				
				}
			}
		}
		if(event.getDamager() instanceof Firework)
		{
			if(event.getEntity() instanceof Player)
			{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler 
	public void onMove(PlayerMoveEvent event) 
	{
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		PersistentDataContainer data = player.getPersistentDataContainer();
		double x = loc.getX();
		double z = loc.getZ();
		
		if(x < 112 && z < 93 && x > -10 && z > -14)
		{
			data.set(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING, "The Forrest");
		}
		else if(x < 233 && z < 151 && x > 113 && z > 7)
		{
			data.set(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING, "The Village");
		}
		else
		{
			data.set(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING, "Wilderness");
		}
	}
	
	//ONKILLSKILLS
	@EventHandler 
	public void onKill(EntityDeathEvent event) 
	{
		if(event.getEntity() instanceof LivingEntity) 
		{
			LivingEntity entity = event.getEntity();
			if(entity.getKiller() instanceof Player && entity.getKiller() != null)
			{
				Player player = (Player)entity.getKiller();
				
				//WARRIORSKILL1-3
				if(getClass(player).equals("Warrior")) 
				{
					PersistentDataContainer data = player.getPersistentDataContainer();
		
					if(data.get(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER) == 0 && data.get(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER) == 0) 
					{
						player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
					}
				
					if(data.get(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER) == 0) 
					{
						player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
					}
				
					if(data.get(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER) == 1 && data.get(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER) == 1) 
					{
						player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2));
					}
				}
				
				//XPGAIN
				PersistentDataContainer data = entity.getPersistentDataContainer();
				Integer getLevel=1;
				Integer amount=0;
				if(data.has(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER)) 
				{
					getLevel = data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER);
				}
				
				if(entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.SPIDER || entity.getType() == EntityType.CREEPER || entity.getType() == EntityType.CAVE_SPIDER  || entity.getType() == EntityType.ZOMBIE_VILLAGER || entity.getType() == EntityType.ZOMBIFIED_PIGLIN || entity.getType() == EntityType.PIGLIN || entity.getType() == EntityType.STRIDER || entity.getType() == EntityType.HUSK || entity.getType() == EntityType.STRAY || entity.getType() == EntityType.DROWNED) 
				{
					addXP(player, Math.round(2*getLevel/10)+2);
					addMoney(player, (getLevel*0.2)+2.0);
					amount=Math.round(2*getLevel/10)+2;
				}
				else if(entity.getType() == EntityType.ENDERMAN || entity.getType() == EntityType.ENDERMITE || entity.getType() == EntityType.SILVERFISH || entity.getType() == EntityType.ZOGLIN || entity.getType() == EntityType.ILLUSIONER || entity.getType() == EntityType.PILLAGER || entity.getType() == EntityType.PHANTOM || entity.getType() == EntityType.PIGLIN_BRUTE || entity.getType() == EntityType.WITHER_SKELETON || entity.getType() == EntityType.BLAZE || entity.getType() == EntityType.GUARDIAN || entity.getType() == EntityType.MAGMA_CUBE || entity.getType() == EntityType.WITCH) 
				{
					addXP(player, Math.round(5*getLevel/10)+5);
					addMoney(player, (getLevel*0.5)+5.0);
					amount=Math.round(5*getLevel/10)+5;
				}			
				else if(entity.getType() == EntityType.IRON_GOLEM || entity.getType() == EntityType.BAT || entity.getType() == EntityType.AXOLOTL || entity.getType() == EntityType.ELDER_GUARDIAN || entity.getType() == EntityType.RAVAGER)
				{
					addXP(player, Math.round(20*getLevel/10)+20);
					addMoney(player, (getLevel*1)+10.0);
					amount=Math.round(20*getLevel/10)+20;
				}
				else if(entity.getType() == EntityType.ENDER_DRAGON || entity.getType() == EntityType.WITHER)
				{
					addXP(player, Math.round(500*getLevel/10)+500);
					addMoney(player, (getLevel*50)+500.0);
					amount=Math.round(500*getLevel/10)+500;
				}
				else 
				{
					addXP(player, Math.round(1*getLevel/10)+1);
					addMoney(player, (getLevel*0.1)+1.0);
					amount=Math.round(1*getLevel/10)+1;
				}	
								
				World world = player.getWorld();
				Location location = entity.getLocation();

				ArmorStand hologram = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
				hologram.setVisible(false);
				hologram.setGravity(false);
				hologram.setSmall(true);
				hologram.setCustomNameVisible(true);

				hologram.setCustomName(ChatColor.LIGHT_PURPLE + "+" + String.valueOf(amount));
				
				PersistentDataContainer holodata = hologram.getPersistentDataContainer();
				holodata.set(new NamespacedKey(plugin, "isHologram"), PersistentDataType.INTEGER, 1);
			}
		}
	}	

	public static void createBoard(Player player)
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective obj = board.registerNewObjective("playerScoreBoard", "dummy", ChatColor.GOLD + player.getDisplayName() + " info:");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		Score score10  = obj.getScore(ChatColor.DARK_GRAY + "=================");
		score10.setScore(10);
		
		Score score9  = obj.getScore(ChatColor.GRAY + "  Class: " + ChatColor.WHITE + getClass(player));
		score9.setScore(9);
			
		if (JoinListener.getClass(player) != "None") 
		{	
			Integer level = JoinListener.getPlayerLevel(player);
			Integer XP = JoinListener.getPlayerXP(player);
			Integer XPnext = level*level*20;
			if(level < 20) 
			{
				Score score7  = obj.getScore(ChatColor.GRAY + "  Level: " + ChatColor.WHITE + level + ChatColor.LIGHT_PURPLE + " [" + XP + "/" + XPnext + "]" );
				score7.setScore(7);
			}
			else 
			{
				Score score7  = obj.getScore(ChatColor.GRAY + "  Level: " + ChatColor.WHITE + level + ChatColor.LIGHT_PURPLE + " [" + XP + "]" );
				score7.setScore(7);
			}
		}
		else 
		{
			Score score7  = obj.getScore(ChatColor.GRAY + "  Level: " + ChatColor.WHITE + "0");
			score7.setScore(7);
		}
		
		Score score6  = obj.getScore(ChatColor.GRAY + "  Region: " + ChatColor.DARK_GREEN + getRegion(player));
		score6.setScore(6);
		
		Score score5  = obj.getScore(ChatColor.GRAY + "  Money: " + ChatColor.GOLD + getMoney(player));
		score5.setScore(5);
		
		if (JoinListener.getClass(player) != "None") 
		{			
			//WarriorSkill
			if(JoinListener.getClass(player) == "Warrior")
			{
				if(data.get(new NamespacedKey(plugin, "WarriorSkill5"), PersistentDataType.INTEGER) == 1)
				{
					Integer cooldown = 300 - data.get(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER);
					if(cooldown > 0) 
					{
						Score score4  = obj.getScore(ChatColor.GRAY + "  Lifeline: " + ChatColor.RED + cooldown + "s");
						score4.setScore(4);
					}
					else 
					{
						Score score4  = obj.getScore(ChatColor.GRAY + "  Lifeline: " + ChatColor.GREEN + "Ready");
						score4.setScore(4);
					}
				}
				else 
				{
					Score score4  = obj.getScore(ChatColor.GRAY + "  No Class Abilites");
					score4.setScore(4);
				}
			}
		}
		else 
		{
			Score score4  = obj.getScore(ChatColor.GRAY + "  No Class Abilites");
			score4.setScore(4);
		}
		Score score3  = obj.getScore("");
		score3.setScore(3);
		Score score2  = obj.getScore(ChatColor.GRAY + "  Online Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size());
		score2.setScore(2);
		Score score1  = obj.getScore(ChatColor.GRAY + "  Player Kills: " + ChatColor.WHITE + player.getStatistic(Statistic.PLAYER_KILLS));
		score1.setScore(1);
		Score score0  = obj.getScore(ChatColor.GRAY + "  Rank: " + ChatColor.WHITE + getRank(player));
		score0.setScore(0);
		player.setScoreboard(board);				
	}
	
	public static String getRank(Player player) 
    {
		if(player.isOp()) 
		{
			return "Opperator";
		}
		else 
		{
			return "Player";
		}
	}
	
	public static String getClass(Player player) 
	{
		if(player.getScoreboardTags().contains("Warrior")) 
		{
			return "Warrior";
		}
		else if (player.getScoreboardTags().contains("Assassin")) 
		{
			return "Assassin";
		}
		else if (player.getScoreboardTags().contains("Archer")) 
		{
			return "Archer";
		}
		else if (player.getScoreboardTags().contains("Mage")) 
		{
			return "Mage";
		}
		else 
		{
			return "None";
		}
	}
	
	public static Integer getManaAmount(Player player) 
	{
		if(player.getScoreboardTags().contains("Warrior")) 
		{
			return 20;
		}
		else if (player.getScoreboardTags().contains("Assassin")) 
		{
			return 30;
		}
		else if (player.getScoreboardTags().contains("Archer")) 
		{
			return 30;
		}
		else if (player.getScoreboardTags().contains("Mage")) 
		{
			return 80;
		}
		else 
		{
			return 0;
		}
	}
	
	public static void setPlayerData(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "Class"), PersistentDataType.STRING, getClass(player));
		data.set(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER, getManaAmount(player));
		data.set(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER, 1);
		data.set(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill4"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill5"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill10"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill11"), PersistentDataType.INTEGER, 0);	
		data.set(new NamespacedKey(plugin, "WarriorSkill12"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill13"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill14"), PersistentDataType.INTEGER, 0);	
		data.set(new NamespacedKey(plugin, "WarriorSkill15"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill16"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill17"), PersistentDataType.INTEGER, 0);		
		data.set(new NamespacedKey(plugin, "WarriorSkill18"), PersistentDataType.INTEGER, 0);
		data.set(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER, 300);		
		data.set(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE, 0.00);	
		data.set(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING, "Wildnerness");
		data.set(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING, "None");
		data.set(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER, 0);
	}
	
	public static void setPlayerDataRejoin(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(!data.has(new NamespacedKey(plugin, "Class"), PersistentDataType.STRING))
		{
			data.set(new NamespacedKey(plugin, "Class"), PersistentDataType.STRING, getClass(player));
		}
		if(!data.has(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER))
		{
			data.set(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER, 0);		
		}
		if(!data.has(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER))
		{	
			data.set(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER, getManaAmount(player));
		}
		if(!data.has(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER))
		{	
			data.set(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER, 1);
		}
		if(!data.has(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER))
		{
			data.set(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill1"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill2"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill3"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill4"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill4"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill5"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill5"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill6"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill7"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill8"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill9"), PersistentDataType.INTEGER, 0);
		}		
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill10"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill10"), PersistentDataType.INTEGER, 0);
		}	
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill11"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill11"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill12"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill12"), PersistentDataType.INTEGER, 0);
		}		
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill13"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill13"), PersistentDataType.INTEGER, 0);
		}		
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill14"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill14"), PersistentDataType.INTEGER, 0);
		}	
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill15"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill15"), PersistentDataType.INTEGER, 0);
		}	
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill16"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill16"), PersistentDataType.INTEGER, 0);
		}	
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill17"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill17"), PersistentDataType.INTEGER, 0);
		}		
		if(!data.has(new NamespacedKey(plugin, "WarriorSkill18"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkill18"), PersistentDataType.INTEGER, 0);
		}
		if(!data.has(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "WarriorSkillTimer"), PersistentDataType.INTEGER, 300);
		}	
		if(!data.has(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE)) 
		{
			data.set(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE, 0.00);
		}	
		if(!data.has(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING)) 
		{
			data.set(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING, "Wildnerness");
		}
		if(!data.has(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING)) 
		{
			data.set(new NamespacedKey(plugin, "ActiveQuest"), PersistentDataType.STRING, "None");
		}
		if(!data.has(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER)) 
		{
			data.set(new NamespacedKey(plugin, "QuestTimer"), PersistentDataType.INTEGER, 0);
		}
	}
	
	public static void setMaxMana(Player player, Integer level) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
				
		data.set(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER, getManaAmount(player) + level);
	}
	
	public static Boolean hasSkill(Player player, ItemStack is) 
	{
        ItemStack skillitem = is;
		
        PersistentDataContainer data = player.getPersistentDataContainer();
		
		ItemMeta metaItem = skillitem.getItemMeta();
		PersistentDataContainer dataItem = metaItem.getPersistentDataContainer();
				
		String skill = getClass(player) + "Skill" + String.valueOf(dataItem.get(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER));
				
		Integer has = data.get(new NamespacedKey(plugin, skill), PersistentDataType.INTEGER);
		
		if(has == 1) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public static Boolean checkDependencies(Player player, ItemStack skillitem) 
	{    
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		ItemMeta metaItem = skillitem.getItemMeta();
		PersistentDataContainer dataItem = metaItem.getPersistentDataContainer();
		
		if (dataItem.has(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY)) 
		{
			int[] dependencies = dataItem.get(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY); 
			Integer length = dependencies.length;
			Integer has = 0;
		   	for (int i : dependencies) 
		   	{
				String skill = getClass(player) + "Skill" + String.valueOf(i);			
				
		   		if(i == 0) 
		   		{	   		
		   			return true;
		   		}
		   		
		   		else if(!data.get(new NamespacedKey(plugin, skill), PersistentDataType.INTEGER).equals(1)) 
				{
					return false;
				}
		   		
				else 
				{
					has += 1;
				}			
			}

		   	if(has == length)
		   	{
				return true;
		   	}
		   	else 
		   	{
		   		return false;
		   	}	
		}
		else 
		{
			return false;
		}
				
	}
	
	public static Integer checkCost(Player player, ItemStack skillitem) 
	{       	
		ItemMeta metaItem = skillitem.getItemMeta();
		PersistentDataContainer dataItem = metaItem.getPersistentDataContainer();
		
		if (dataItem.has(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER)) 
		{
			return dataItem.get(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER);	
		}
		else 
		{
			return 0;
		}
	}
	
	public static String getPlayerClass(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "Class"), PersistentDataType.STRING)) 
		{
			return data.get(new NamespacedKey(plugin, "Class"), PersistentDataType.STRING);
		}
		else 
		{
			return "None";
		}
	}
	
	public static boolean canAfford(Player player, ItemStack is) 
	{
        ItemStack skillitem = is;
		
        PersistentDataContainer data = player.getPersistentDataContainer();
		
		ItemMeta metaItem = skillitem.getItemMeta();
		PersistentDataContainer dataItem = metaItem.getPersistentDataContainer();
		
		Integer sp = data.get(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER);
		
		Integer cost = dataItem.get(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER);
		
		if(sp >= cost) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public static Integer getCost(ItemStack skillitem) 
	{		
        ItemMeta meta = skillitem.getItemMeta();
		PersistentDataContainer data = meta.getPersistentDataContainer();	

		return data.get(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER);		
	}
	
	public static Double getMoney(Player player) 
	{		
		PersistentDataContainer data = player.getPersistentDataContainer();	
		return data.get(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE);		
	}
	
	public static void UnlockSkill(Player player, ItemStack is) 
	{
        ItemStack skillitem = is;
		
        PersistentDataContainer data = player.getPersistentDataContainer();
		
		ItemMeta metaItem = skillitem.getItemMeta();
		PersistentDataContainer dataItem = metaItem.getPersistentDataContainer();
		
		Integer sp = data.get(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER);
		
		String skill = getClass(player) + "Skill" + String.valueOf(dataItem.get(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER));
		
		Integer cost = dataItem.get(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER);
		
		Boolean depedencies = checkDependencies(player, skillitem);
		
		Integer has = data.get(new NamespacedKey(plugin, skill), PersistentDataType.INTEGER);
				
		if(sp >= cost && depedencies.equals(true) && has == 0) 
		{
			data.set(new NamespacedKey(plugin, skill), PersistentDataType.INTEGER, 1);		
			data.set(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER) - cost);			
		}
		else 
		{
			return;
		}
	}
	
	public static int getPlayerSkillPoints(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER)) 
		{
			return data.get(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER);
		}
		else 
		{
			return 0;
		}
	}
	
	public static int getPlayerLevel(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER)) 
		{
			return data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER);
		}
		else 
		{
			return 0;
		}
	}
	
	public static int getPlayerXP(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER)) 
		{
			return data.get(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER);
		}
		else 
		{
			return 0;
		}
	}
	
	public static String getRegion(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING)) 
		{
			return data.get(new NamespacedKey(plugin, "Region"), PersistentDataType.STRING);
		}
		else 
		{
			return "Wilderness";
		}
	}
	
	public static int getPlayerMana(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER)) 
		{
			return data.get(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER);
		}
		else 
		{
			return 0;
		}
	}
	
	public static int getPlayerMaxMana(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();
		
		if(data.has(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER)) 
		{
			return data.get(new NamespacedKey(plugin, "MaxMana"), PersistentDataType.INTEGER);
		}
		else 
		{
			return 0;
		}
	}
	
	public static void addMana(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();	
		data.set(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "Mana"), PersistentDataType.INTEGER) + 1);	
	}
	
	public static void addXP(Player player, Integer Amount) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();	
		data.set(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER) + Amount);		
		player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
	}
	
	public static void addMoney(Player player, Double Amount) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();	
		data.set(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE, data.get(new NamespacedKey(plugin, "Money"), PersistentDataType.DOUBLE) + Amount);		
	}
	
	public static void levelUp(Player player) 
	{
		PersistentDataContainer data = player.getPersistentDataContainer();		
		data.set(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "XP"), PersistentDataType.INTEGER) - (data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER)*data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER)*20));
		data.set(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER) + 1);
		data.set(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER, data.get(new NamespacedKey(plugin, "SkillPoints"), PersistentDataType.INTEGER) + 1 + Math.abs(data.get(new NamespacedKey(plugin, "Level"), PersistentDataType.INTEGER)/5));	
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
		player.sendTitle(" ", ChatColor.GOLD + "Level Up!", 20, 40, 20);;	
		
        Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.ORANGE).withFade(Color.YELLOW).with(Type.BALL_LARGE).trail(true).build();
       
        fwm.addEffect(effect);

        fwm.setPower(1);
       
        fw.setFireworkMeta(fwm);  
	}
	
	//Not in use
	public static String[] getPlayerData(Player player) 
	{
		return new String[] {getPlayerClass(player), String.valueOf(getPlayerLevel(player)), String.valueOf(getPlayerXP(player)), String.valueOf(getPlayerMana(player))};
	}
}
