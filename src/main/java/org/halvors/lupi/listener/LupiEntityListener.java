/* 
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 * Copyright (C) 2011 adamonline45 <adamonline45@gmail.com>
 * 
 * This file is part of Lupi.
 * 
 * Lupi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Lupi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Lupi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.halvors.lupi.listener;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.util.ConfigurationManager;
import org.halvors.lupi.util.WolfUtil;
import org.halvors.lupi.util.WorldConfiguration;
import org.halvors.lupi.wolf.WolfManager;

/**
 * Handle events for all Entity related events.
 * 
 * @author halvors
 */
public class LupiEntityListener extends EntityListener {
//    private final Lupi plugin;
    
    private final ConfigurationManager configManager;
    private final WolfManager wolfManager;
    
    public LupiEntityListener(Lupi plugin) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.wolfManager = plugin.getWolfManager();
    }
    
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {        	
            Entity entity = event.getEntity();
            SpawnReason reason = event.getSpawnReason();
            World world = entity.getWorld();
            WorldConfiguration worldConfig = configManager.get(world);
            
            if (entity instanceof Wolf) {
//            Wolf wolf = (Wolf) entity;
                
            	if (reason == SpawnReason.NATURAL) {
            		if (!worldConfig.wolfEnable) {
            			event.setCancelled(true);
            			return;
            		}
            	}
            }
        }
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
    	if (!event.isCancelled()) {
    		Entity entity = event.getEntity();
    		
    		if (entity instanceof Wolf) {
    			Wolf wolf = (Wolf) entity;
    			
    			if (wolf.isTamed()) {
    				org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
    				
    				if (wolf1.hasLoadedInventory()) {
    					WolfUtil.doArmorCheck(wolf1, event);
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
            
            if (wolf.isTamed()) {
                org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

                if (wolf1.hasLoadedInventory()) {
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
            Player player = (Player) event.getOwner();
            World world = entity.getWorld();
            WorldConfiguration worldConfig = configManager.get(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                // Check the wolf limit.
                int limit = worldConfig.wolfLimit;
                
                if (limit > 0) {
                    if (limit <= wolfManager.getWolves(player).size()) {
                        player.sendMessage("You can't tame more wolves, the limit is " + ChatColor.YELLOW + Integer.toString(limit) + ChatColor.WHITE + " wolves.");
                        
                        event.setCancelled(true);
                        return;
                    }
                }
                
                // Add the wolf.
                if (wolfManager.addWolf(wolf)) {
                    org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                    
                    player.sendMessage("This wolf's name is " + ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + ".");
                    player.sendMessage("You can change name with /wolf setname <name>.");
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
            WorldConfiguration worldConfig = configManager.get(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (target instanceof Player) {
                    Player player = (Player) target;

                    if (wolf.isTamed()) {
                    	if (wolfManager.hasWolf(wolf) && !wolf.getOwner().equals(player)) {
                    		if (worldConfig.wolfPvp && world.getPVP()) {
                    			event.setCancelled(true);
                    			return;
                    		}
                    	}
                	} else {
                		if (worldConfig.wolfFriendly) {
                			event.setCancelled(true);
                			return;
                		}
                	}
                }
            }
        }
    }
}
