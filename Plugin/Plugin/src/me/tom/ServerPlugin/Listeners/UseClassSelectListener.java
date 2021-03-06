package me.tom.ServerPlugin.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.tom.ServerPlugin.Master;
import me.tom.ServerPlugin.Items.ItemManager;
import net.minecraft.world.item.Items;

public class UseClassSelectListener implements Listener 
{
	public static Inventory selectClassinv;
	public static Inventory warriorInv;
	public static Inventory assassinInv;
	public static Inventory archerInv;
	public static Inventory mageInv;
	private static Master plugin;
		
	public UseClassSelectListener (Master plugin) 
	{
	this.plugin = plugin;
	
	Bukkit.getPluginManager().registerEvents(this, plugin);
	createInv();
	}
	
	@EventHandler 
	public void onClick(InventoryClickEvent event) 
	{	
		Player player = (Player) event.getWhoClicked();	
		
		if(player.getOpenInventory().getType().equals(InventoryType.CRAFTING))
		{
			
		}	
		if(event.getSlot() == 8);
	    {
            event.setResult(Result.DENY);
            event.setCancelled(true);
	    }
		if(event.getCurrentItem() == null) 
		{
			return;
		}
		if(event.getCurrentItem().getItemMeta() == null) 
		{
			return;
		}
		if(event.getCurrentItem().getItemMeta().getDisplayName() == null) 
		{
			return;
		}
		
		ItemStack item = event.getCurrentItem();				
		
		if(event.getInventory().equals(selectClassinv)) 
		{
			event.setCancelled(true);
			//Select Warrior
			if (event.getSlot() == 10) 
			{
				player.removeScoreboardTag("Assassin");
				player.removeScoreboardTag("Archer");
				player.removeScoreboardTag("Mage");
				player.addScoreboardTag("Warrior");
				player.closeInventory();
			}
		
			//Select Assassin
			if (event.getSlot() == 12) 
			{
				player.removeScoreboardTag("Warrior");
				player.removeScoreboardTag("Archer");
				player.removeScoreboardTag("Mage");
				player.addScoreboardTag("Assassin");
				player.closeInventory();
			}
		
			//Select Archer
			if (event.getSlot() == 14) 
			{
				player.removeScoreboardTag("Warrior");
				player.removeScoreboardTag("Assassin");
				player.removeScoreboardTag("Mage");
				player.addScoreboardTag("Archer");
				player.closeInventory();
			}
		
			//Select Mage
			if (event.getSlot() == 16) 
			{
				player.removeScoreboardTag("Warrior");
				player.removeScoreboardTag("Assassin");
				player.removeScoreboardTag("Archer");
				player.addScoreboardTag("Mage");
				player.closeInventory();
			}
		
			//Select Close
			if (event.getSlot() == 22) 
			{
				player.closeInventory();
			}
		}
		if(event.getInventory().equals(warriorInv)) 
		{
			event.setCancelled(true);
			
			if(item.getType().equals(Material.BARRIER)) 
			{
				player.closeInventory();
				return;
			}
			
			if(item.getType().equals(Material.WHITE_STAINED_GLASS_PANE)) 
			{
				return;
			}

			else
			{		
				JoinListener.UnlockSkill(player, item);					
				UseClassSelectListener.createWarriorIvn(player);
				player.openInventory(UseClassSelectListener.warriorInv);			
			}			
		}
	}
	
	public static void createInv() 
	{
		selectClassinv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Select Class");
		
		ItemStack item = new ItemStack(Material.IRON_HELMET);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		
		//Warrior
		meta.setDisplayName(ChatColor.BLUE + "Warrior");
		lore.add(ChatColor.GRAY + "Click to select the " + ChatColor.BLUE + "Warrior " + ChatColor.GRAY + "class");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		selectClassinv.setItem(10, item);
		lore.clear();
		
		//Assassin
		item.setType(Material.IRON_SWORD);
		meta.setDisplayName(ChatColor.RED + "Assassin");
		lore.add(ChatColor.GRAY + "Click to select the " + ChatColor.RED + "Assassin " + ChatColor.GRAY + "class");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		selectClassinv.setItem(12, item);
		lore.clear();
		
		//Archer
		item.setType(Material.BOW);
		meta.setDisplayName(ChatColor.GREEN + "Archer");
		lore.add(ChatColor.GRAY + "Click to select the " + ChatColor.GREEN + "Archer " + ChatColor.GRAY + "class");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		selectClassinv.setItem(14, item);
		lore.clear();
		
		//Mage
		item.setType(Material.BLAZE_POWDER);
		meta.setDisplayName(ChatColor.YELLOW + "Mage");
		lore.add(ChatColor.GRAY + "Click to select the " + ChatColor.YELLOW + "Mage " + ChatColor.GRAY + "class");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		selectClassinv.setItem(16, item);
		lore.clear();
		
		//Close
		item.setType(Material.BARRIER);
		meta.setDisplayName(ChatColor.DARK_GRAY + "Close");
		lore.add(ChatColor.GRAY + "Click to close the menu");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		selectClassinv.setItem(22, item);
		lore.clear();
	}
	
	public static ChatColor getTitleColour(boolean isUnlocked) 
	{
		if(isUnlocked) 
		{
			return ChatColor.GREEN;
		}
		else 
		{
			return ChatColor.DARK_GRAY;
		}
	}
	
	public static ChatColor getCostColour(boolean canAfford) 
	{
		if(canAfford) 
		{
			return ChatColor.AQUA;
		}
		else 
		{
			return ChatColor.RED;
		}
	}
	
	public static void setItem(boolean isUnlocked, boolean hasDependencies, ItemStack item) 
	{
		if(hasDependencies == true && isUnlocked == false) 
		{
			item.setType(Material.YELLOW_STAINED_GLASS_PANE);
		}
		if(hasDependencies == false && isUnlocked == false) 
		{
			item.setType(Material.RED_STAINED_GLASS_PANE);
		}
	}
	
	//Warrior Skill Tree
	public static void createWarriorIvn(Player player) 
	{
		warriorInv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + " Class Tree " + ChatColor.BLUE + "Skill Points: " + JoinListener.getPlayerSkillPoints(player));
		
		ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		PersistentDataContainer data = meta.getPersistentDataContainer();
		
		int ID;
		int cost;
		int[] dependencies;
		
		//White Background
		meta.setDisplayName(" ");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		warriorInv.setItem(0, item);
		warriorInv.setItem(1, item);
		warriorInv.setItem(3, item);
		warriorInv.setItem(7, item);
		warriorInv.setItem(8, item);
		
		warriorInv.setItem(9, item);
		warriorInv.setItem(10, item);
		warriorInv.setItem(12, item);
		warriorInv.setItem(13, item);
		warriorInv.setItem(14, item);
		warriorInv.setItem(16, item);
		warriorInv.setItem(17, item);
		
		warriorInv.setItem(23, item);
		
		warriorInv.setItem(27, item);
		warriorInv.setItem(28, item);
		warriorInv.setItem(30, item);
		warriorInv.setItem(31, item);
		warriorInv.setItem(32, item);
		warriorInv.setItem(34, item);
		warriorInv.setItem(35, item);
		
		warriorInv.setItem(36, item);
		warriorInv.setItem(37, item);
		warriorInv.setItem(39, item);
		warriorInv.setItem(40, item);
		warriorInv.setItem(41, item);
		warriorInv.setItem(43, item);
		warriorInv.setItem(44, item);
		
		item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		meta.setDisplayName(" ");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		warriorInv.setItem(46, item);
		warriorInv.setItem(47, item);
		warriorInv.setItem(48, item);
		warriorInv.setItem(49, item);
		warriorInv.setItem(50, item);
		warriorInv.setItem(51, item);
		warriorInv.setItem(52, item);
		
		//Skill1		
		ID=1;
		cost=1;
		dependencies = new int[] {0,0};
		
		//Create Item
		item = new ItemStack(Material.WOODEN_SWORD);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Slayer");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Berserk");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "Strength I" + ChatColor.GRAY + " for a short");
		lore.add(ChatColor.GRAY + "amont of time after killing");
		lore.add(ChatColor.GRAY + "an enermy");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(38, item);
		lore.clear();
		
		//Skill2
		ID=2;
		cost=3;
		dependencies = new int[] {1,11};
		
		//Create Item
		item = new ItemStack(Material.STONE_SWORD);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Beserker");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Berserk II");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "Strength II" + ChatColor.GRAY + " for a short");
		lore.add(ChatColor.GRAY + "amont of time after killing");
		lore.add(ChatColor.GRAY + "an enermy");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(20, item);
		lore.clear();
		
		//Skill3
		ID=3;
		cost=5;
		dependencies = new int[] {1,2,11,12};
		
		//Create Item
		item = new ItemStack(Material.IRON_SWORD);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Warlord");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Berserk III");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "Strength III" + ChatColor.GRAY + " for a short");
		lore.add(ChatColor.GRAY + "amont of time after killing");
		lore.add(ChatColor.GRAY + "an enermy");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(2, item);
		lore.clear();
		
		//Skill4
		ID=4;
		cost=6;
		dependencies = new int[] {1,2,11,13};
		
		//Create Item
		item = new ItemStack(Material.RED_DYE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Vamprism");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Vampirism");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+25% Lifesteal");	
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(18, item);
		lore.clear();
		
		//Skill5
		ID=5;
		cost=5;
		dependencies = new int[] {1,2,11,14};
		
		//Create Item
		item = new ItemStack(Material.GOLDEN_APPLE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Lifeline");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Lifeline");
		lore.add(ChatColor.GRAY + "Upon dropping to bellow" + ChatColor.RED + " 25% Health" + ChatColor.GRAY + " gain");
		lore.add(ChatColor.GREEN + "Regneration V " + ChatColor.GRAY + "and " + ChatColor.GREEN + "Absorption V");
		lore.add(ChatColor.GRAY + "for " + ChatColor.AQUA + "15s");		
		lore.add(ChatColor.BLUE + "Cooldown: 300s");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(22, item);
		lore.clear();
		
		//Skill6
		ID=6;
		cost=1;
		dependencies = new int[] {0};
				
		//Create Item
		item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
				
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
				
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Defender");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Reinforce I");
		lore.add(ChatColor.GRAY + "If bellow" + ChatColor.RED + " 25% Health" + ChatColor.GRAY + " the");
		lore.add(ChatColor.GRAY + "player gains "+ ChatColor.GREEN + "Resistance I");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(42, item);
		lore.clear();
		
		//Skill7
		ID=7;
		cost=3;
		dependencies = new int[] {6,15};
		
		//Create Item
		item = new ItemStack(Material.GOLDEN_CHESTPLATE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Knight");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Reinforce II");
		lore.add(ChatColor.GRAY + "If bellow" + ChatColor.RED + " 30% Health" + ChatColor.GRAY + " the");
		lore.add(ChatColor.GRAY + "player gains "+ ChatColor.GREEN + "Resistance II");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(24, item);
		lore.clear();
		
		//Skill8
		ID=8;
		cost=5;
		dependencies = new int[] {6,7,15,16};
		
		//Create Item
		item = new ItemStack(Material.IRON_CHESTPLATE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Guardian");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Reinforce III");
		lore.add(ChatColor.GRAY + "If bellow" + ChatColor.RED + " 40% Health" + ChatColor.GRAY + " the");
		lore.add(ChatColor.GRAY + "player gains "+ ChatColor.GREEN + "Resistance III");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(6, item);
		lore.clear();
		
		//Skill9
		ID=9;
		cost=7;
		dependencies = new int[] {6,7,8,15,16,17};
		
		//Create Item
		item = new ItemStack(Material.NETHERITE_CHESTPLATE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Paladin");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Reinforce IV");
		lore.add(ChatColor.GRAY + "If bellow" + ChatColor.RED + " 50% Health" + ChatColor.GRAY + " the");
		lore.add(ChatColor.GRAY + "player gains "+ ChatColor.GREEN + "Resistance IV");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(4, item);
		lore.clear();
		
		//Skill10
		ID=10;
		cost=5;
		dependencies = new int[] {6,7,15,18};
		
		//Create Item
		item = new ItemStack(Material.DIAMOND_HELMET);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Cold Steel");
		lore.add("");
		lore.add(ChatColor.GOLD + "Class ability: Cold Steel");
		lore.add(ChatColor.GRAY + "Enermies that attack you are ");
		lore.add(ChatColor.GRAY + "given "+ ChatColor.BLUE + "Slowness II");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(26, item);
		lore.clear();
		
		//Skill11
		ID=11;
		cost=1;
		dependencies = new int[] {1};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+1 Attack Damage");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(29, item);
		lore.clear();
		
		//Skill12
		ID=12;
		cost=2;
		dependencies = new int[] {1,2,11};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+2 Attack Damage");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(11, item);
		lore.clear();
		
		//Skill13
		ID=13;
		cost=1;
		dependencies = new int[] {1,2,11};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+1 Attack Speed");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(19, item);
		lore.clear();
		
		//Skill14
		ID=14;
		cost=1;
		dependencies = new int[] {1,2,11};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+1 Maximum Health");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(21, item);
		lore.clear();
		
		//Skill15
		ID=15;
		cost=1;
		dependencies = new int[] {6};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+1 Maximum Health");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(33, item);
		lore.clear();
		
		//Skill16
		ID=16;
		cost=2;
		dependencies = new int[] {6,7,15};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+2 Maximum Health");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(15, item);
		lore.clear();
		
		//Skill17
		ID=17;
		cost=3;
		dependencies = new int[] {6,7,8,15,16};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+3 Maximum Health");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(5, item);
		lore.clear();
		
		//Skill18
		ID=18;
		cost=1;
		dependencies = new int[] {6,7,15};
		
		//Create Item
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		data = meta.getPersistentDataContainer();
		
		data.set(new NamespacedKey(plugin, "SkillName"), PersistentDataType.INTEGER, ID);
		data.set(new NamespacedKey(plugin, "Cost"), PersistentDataType.INTEGER, cost);
		data.set(new NamespacedKey(plugin, "Dependencies"), PersistentDataType.INTEGER_ARRAY, dependencies);
		item.setItemMeta(meta);
		
			//Visual Display
		setItem(JoinListener.hasSkill(player, item),JoinListener.checkDependencies(player, item),item);
		meta.setDisplayName(getTitleColour(JoinListener.hasSkill(player, item)) + "Attribute Bonus");
		lore.add("");
		lore.add(ChatColor.GRAY + "Gain " + ChatColor.GREEN + "+1 Armour");
		lore.add(ChatColor.DARK_GRAY + "Passive Ability");
		lore.add("");
		if(!JoinListener.hasSkill(player, item)) 
		{
			lore.add(getCostColour(JoinListener.canAfford(player, item)) + "Cost: " + String.valueOf(cost) + " Skill Points");
			meta.removeEnchant(Enchantment.LUCK);
		}
		else 
		{
			meta.addEnchant(Enchantment.LUCK, 1, false);
		}
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		warriorInv.setItem(25, item);
		lore.clear();
		
		//Playerhead
		item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skull = (SkullMeta)item.getItemMeta();
		skull.setOwningPlayer(player);
		skull.setDisplayName(ChatColor.GOLD + player.getName());
		lore.add("");
		lore.add(ChatColor.GRAY + "Player stats:");
		lore.add(ChatColor.GRAY + "Class: " + ChatColor.WHITE + JoinListener.getClass(player) + ChatColor.DARK_GRAY + " :" + ChatColor.GOLD + " Lvl: " + ChatColor.WHITE + String.valueOf(JoinListener.getPlayerLevel(player)));
		lore.add(ChatColor.RED + " Health: " + ChatColor.WHITE + String.valueOf(Math.round(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue())));
		lore.add(ChatColor.AQUA + " Mana: " + ChatColor.WHITE + String.valueOf(JoinListener.getPlayerMaxMana(player)));
		lore.add(ChatColor.DARK_PURPLE + " Strength: " + ChatColor.WHITE + String.valueOf(Math.round(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue())));
		lore.add(ChatColor.GRAY + " Armour: " + ChatColor.WHITE + String.valueOf(Math.round(player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue())));
		lore.add(ChatColor.YELLOW + " Attack Speed: " + ChatColor.WHITE + String.valueOf(Math.round(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getBaseValue())));
		lore.add(ChatColor.BLUE + " Movement Speed: " + ChatColor.WHITE + String.valueOf(Math.round(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue())));
		skull.setLore(lore);
		item.setItemMeta(skull);
		warriorInv.setItem(45, item);
		lore.clear();
		
		//Close
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GRAY + "Close");
		lore.add(ChatColor.GRAY + "Click to close the menu");
		meta.setLore(lore);
		item.setItemMeta(meta);
		warriorInv.setItem(53, item);
		lore.clear();
	}
}