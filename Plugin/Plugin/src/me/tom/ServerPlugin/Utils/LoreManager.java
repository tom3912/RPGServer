package me.tom.ServerPlugin.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sun.jna.platform.unix.X11;

import net.minecraft.server.level.PlayerChunk.e;
import net.minecraft.tags.Tag.f;

public class LoreManager 
{
	public static Integer[] getSoulValue(Player player, List<String> lore) 
	{
		List<ChatColor> colour = new ArrayList<>();	
		for (String s : lore) 
		{
			String sc =  s.replace(ChatColor.COLOR_CHAR, '&');
			
			for(int i = 0 ; i < sc.length() ; i++) 
			{
	            char c = sc.charAt(i);
	            if(c=='&') 
	            {
	                char id = sc.charAt(i+1); 
	                colour.add(ChatColor.getByChar(id));
	            }
	        }
			
		}
		int index = colour.indexOf(ChatColor.DARK_RED)-1;	

		String valuestring = lore.get(index);		
		String sv = valuestring.replaceAll("[^0-9]", "");
		String sf = sv.replaceFirst("4", "");
		String value = sf.replaceAll("50", "");
		
		if(value!="") 
		{
			int Value = Integer.valueOf(value);
			return new Integer[]{Value,index};
		}
		else
		{
			return new Integer[]{50,index};
		}
	}
}
