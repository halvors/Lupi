/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
 *
 * This file is part of Wolf.
 *
 * Wolf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wolf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Wolf.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.halvors.Wolf.listeners;

import org.bukkit.Material;
//import org.bukkit.block.Sign;
//import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

//import com.halvors.Wolf.util.ConfigManager;

/**
 * Handle events for all Block related events
 * 
 * @author halvors
 */
public class WolfBlockListener extends BlockListener {
	//private final com.halvors.Wolf.Wolf plugin;
	
	//private final ConfigManager configManager;
	
	public WolfBlockListener(final com.halvors.Wolf.Wolf plugin) {
	//	this.plugin = plugin;
	//	this.configManager = plugin.getConfigManager();
	}
	
	@Override
	public void onSignChange(SignChangeEvent event) {
		if (!event.isCancelled()) {
			//Player player = event.getPlayer();
			Material type = event.getBlock().getType();
			
			if (type == Material.SIGN || type == Material.WALL_SIGN || type == Material.SIGN_POST) {
				//Sign sign = (Sign)event.getBlock();
				//String[] text = event.getLines();
			}
		}
	}
}