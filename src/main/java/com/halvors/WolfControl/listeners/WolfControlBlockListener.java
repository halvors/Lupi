package com.halvors.WolfControl.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

import com.halvors.WolfControl.WolfControl;
import com.halvors.WolfControl.shop.ShopManager;
import com.halvors.WolfControl.util.ConfigManager;

/**
 * Handle events for all Block related events
 * 
 * @author halvors
 */
public class WolfControlBlockListener extends BlockListener {
	private final WolfControl plugin;
	
	private final ConfigManager configManager;
	private final ShopManager shopManager;
	
	public WolfControlBlockListener(final WolfControl plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.shopManager = plugin.getShopManager();
	}
	
	@Override
	public void onSignChange(SignChangeEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();
			Material type = event.getBlock().getType();
			
			if ((type == Material.SIGN) || (type == Material.WALL_SIGN) || (type ==Material.SIGN_POST)) {
				Sign sign = (Sign)event.getBlock();
				String[] text = event.getLines();
				
				if (shopManager.isValid(sign)) {
					player.sendMessage(ChatColor.GREEN + "You've successfully setup a wolf shop.");
				}
			}
		}
	}
}
