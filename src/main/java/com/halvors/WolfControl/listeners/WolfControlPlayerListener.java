/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
 *
 * This file is part of WolfControl.
 *
 * WolfControl is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WolfControl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WolfControl.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.halvors.WolfControl.listeners;

import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.halvors.WolfControl.WolfControl;
import com.halvors.WolfControl.util.ConfigManager;
import com.halvors.WolfControl.util.WorldConfig;
import com.halvors.WolfControl.wolf.SelectedWolfManager;
import com.halvors.WolfControl.wolf.WolfManager;

/**
 * Handle events for all Player related events
 * 
 * @author halvors
 */
public class WolfControlPlayerListener extends PlayerListener {
	private final WolfControl plugin;
	
	private final ConfigManager configManager;
	private final WolfManager wolfManager;
	private final SelectedWolfManager selectedWolfManager;
	
	public WolfControlPlayerListener(final WolfControl plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.wolfManager = plugin.getWolfManager();
		this.selectedWolfManager = plugin.getSelectedWolfManager();
	}
	
	@Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (event.hasItem() && selectedWolfManager.hasSelectedWolf(player.getName())) {
            if (plugin.hasPermissions(player, "WolfControl.target")) {        
            	if (event.getItem().getTypeId() == 1) {
                	Location pos = player.getTargetBlock(null, 120).getLocation();
                    
                	if ((action == Action.LEFT_CLICK_BLOCK) || (action == Action.LEFT_CLICK_AIR)) {
                		PathPoint[] pathPoint = { new PathPoint(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()) };
                		EntityWolf wolf = ((CraftWolf) selectedWolfManager.getSelectedWolf(player.getName())).getHandle();
                		wolf.a(new PathEntity(pathPoint));
                		
                		player.sendMessage(ChatColor.GREEN + "Wolf target set.");
                    } else if ((action == Action.RIGHT_CLICK_BLOCK) || (action == Action.RIGHT_CLICK_AIR)) {
                    	
                    }
                }
            }
        }
	}
	
//	@Override
//	public void onPlayerInteract(PlayerInteractEvent event) {
//		if (!event.isCancelled()) {
//			Action action = event.getAction();
//			Player player = event.getPlayer();
//			
//			
//			/*
//			if ((event.hasItem()) && (selectedWolfManager.hasSelectedWolf(player.getName()))) {
//				player.sendMessage("1");
//				
//	            if (plugin.hasPermissions(player, "WolfControl.wand")) {
//	            	player.sendMessage("2");
//	            	if (event.getItem().getType() == Material.SADDLE) {
//	            		player.sendMessage("3");
//	                	Location pos = player.getTargetBlock(null, 120).getLocation();
//	                
//	                	if ((action == Action.RIGHT_CLICK_BLOCK) || (action == Action.RIGHT_CLICK_AIR)) {
//	                		player.sendMessage("4");
//	                		player.sendMessage(ChatColor.GREEN + "Wolf target set.");
//	                		
//	                		PathPoint[] pathPoint = { new PathPoint(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()) };
//	          	          	EntityWolf wolf = ((CraftWolf) selectedWolfManager.getSelectedWolf(player.getName())).getHandle();
//	          	          	wolf.a(new PathEntity(pathPoint));
//	                    }
//	            	}
//	            }
//	        }
//	        */
//		}
//	}
	
	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (!event.isCancelled()) {
			Player player = event.getPlayer();
			Entity entity = event.getRightClicked();
			World world = entity.getWorld();
			WorldConfig worldConfig = configManager.getWorldConfig(world);
			
			if (entity instanceof Wolf) {
				Wolf wolf = (Wolf) entity;
				
				if (plugin.hasPermissions(player, "WolfControl.select")) {
					int item = worldConfig.item;
					
					if (item != 0) {
						if (player.getItemInHand().getTypeId() == item) {
							if (wolf.isTamed()) {
								/*
								if (!wolfManager.hasWolf(wolf.getEntityId())) {
									wolfManager.addWolf(wolf);
									
									player.sendMessage(ChatColor.GREEN + "Your wolf was named: " + ChatColor.YELLOW + wolfManager.getName(wolf.getEntityId()));
								}
								*/
									
								selectedWolfManager.addSelectedWolf(player.getName(), wolf);
								
								player.sendMessage(ChatColor.GREEN + "Wolf selected.");
							} else {
								// TODO: Add something here.
							}
						}
					} else {
						player.sendMessage(ChatColor.RED + "Error: Item not set in configuration file!");
					}
				}
			}
		}
	}
}