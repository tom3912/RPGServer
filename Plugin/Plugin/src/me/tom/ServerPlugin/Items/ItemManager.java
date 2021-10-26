package me.tom.ServerPlugin.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager 
{
	public static ItemStack wand;
	public static ItemStack bloodthirster;
	public static ItemStack classbook;
	public static ItemStack playermasteritem;
	
	public static void init() 
	{
		createWand();
		createBloodthirster();		
		createClassBook();	
		createPlayerMasterItem();
	}
	
	private static void createWand() 
	{
		ItemStack item = new ItemStack(Material.STICK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Admin Wand");
		List<String> lore = new ArrayList<>();
		lore.add("§7Server plugin admin item");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.LUCK, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		wand = item;
	}
	
	private static void createClassBook() 
	{
		ItemStack item = new ItemStack(Material.BOOK, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Select Class");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Right click to select a class");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.LUCK, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		classbook = item;
	}
	
	private static void createPlayerMasterItem() 
	{
		ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Player Info");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Click to view player data");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.LUCK, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		playermasteritem = item;
	}
	
	private static void createBloodthirster() 
	{
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6Bloodthirster");
		List<String> lore = new ArrayList<>();
		lore.add("");
		lore.add("§6Item ability: Overheal");
		lore.add("§7Gain §aAborption §7equal to");
		lore.add("§7how many mob you have ");
		lore.add("§7killed recently");
		lore.add("§4Souls: [0/50]");
		lore.add("§8Right click to use");
		lore.add("");
		lore.add("§6Item ability: Bloodlust");
		lore.add("§7This item has §a+50% Lifesteal");
		lore.add("§8Pasive Ability");
		lore.add("");
		lore.add("§6Legendary Item");
		meta.setLore(lore);
		item.setItemMeta(meta);
		bloodthirster = item;
	}
}
