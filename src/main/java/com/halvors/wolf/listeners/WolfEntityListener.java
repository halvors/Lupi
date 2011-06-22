/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
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

package com.halvors.wolf.listeners;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.halvors.wolf.WolfPlugin;
import com.halvors.wolf.util.ConfigManager;
import com.halvors.wolf.util.WorldConfig;
import com.halvors.wolf.wolf.WolfManager;
import com.halvors.wolf.wolf.WolfTable;

/**
 * Handle events for all Entity related events.
 * 
 * @author halvors
 */
public class WolfEntityListener extends EntityListener {
//    private final WolfPlugin plugin;
    
    private final ConfigManager configManager;
    private final WolfManager wolfManager;
    
    public WolfEntityListener(final WolfPlugin plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.wolfManager = plugin.getWolfManager();
    }
    
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {        	
            Entity entity = event.getEntity();
        	SpawnReason reason = event.getSpawnReason();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (reason == SpawnReason.NATURAL) {
            	if (entity instanceof Wolf) {
//                Wolf wolf = (Wolf) entity;
                
            		if (!worldConfig.wolfEnable) {
            			event.setCancelled(true);
            		}
            	}
            }
        }
    }
    
    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Wolf) {
            Wolf wolf = (Wolf) entity;
            
            if (wolf.isTamed() && wolfManager.hasWolf(wolf)) {
                com.halvors.wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

                if (wolf1.hasInventory()) {
                	wolf1.dropInventory();
                }
                
                wolfManager.removeWolf(wolf);
            }
        }
    }
    
    @Override
    public void onEntityTame(EntityTameEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            Player owner = (Player) event.getOwner();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (worldConfig.limitEnable) {
                    List<WolfTable> wts = wolfManager.getWolfTables(owner);
                    int size = wts.size();
                    int limit = worldConfig.limitValue;
                    
                    if (size >= limit) {
                        owner.sendMessage("You can't tame more wolves, the limit is " + ChatColor.YELLOW + Integer.toString(limit));
                        
                        event.setCancelled(true);
                        
                        return;
                    }
                }
                
                if (wolfManager.addWolf(wolf)) {
                    com.halvors.wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                    owner.sendMessage("This is " + ChatColor.YELLOW + wolf1.getName());
                    owner.sendMessage("You can change name with /wolf setname <name>");
                } else {
                	// TODO: Display some kind of error message here.
                }
            }
        }
    }
    
    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            Entity target = event.getTarget();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (target instanceof Player) {
                    Player player = (Player) target;

                    if (wolf.isTamed() && wolfManager.hasWolf(wolf)) {
                    	if (!wolf.getOwner().equals(player)) {
                    		if (worldConfig.wolfPvp && world.getPVP()) {
                    			event.setCancelled(true);
                    		}
                    	}
                	}
                }
            }
        }
    }
}
