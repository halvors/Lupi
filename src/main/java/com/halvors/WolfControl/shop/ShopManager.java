package com.halvors.WolfControl.shop;

import org.bukkit.block.Sign;

import com.halvors.WolfControl.WolfControl;

/**
 * Handles shops
 * 
 * @author halvors
 */
public class ShopManager {
	public final WolfControl plugin;
	
	public ShopManager(final WolfControl plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Check if a sign is a valid shop
	 * 
	 * @param sign
	 * @return
	 */
	public boolean isValid(Sign sign) {
		String prefix = "[Wolf]";
		String[] text = sign.getLines();
		
		if ((text[0].equalsIgnoreCase(prefix)) && (sign.getWorld().getPlayers().contains(text[1]))) {
			return true;
		}
		
		return false;
	}
}
