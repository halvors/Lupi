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

package com.halvors.Wolf.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import com.halvors.Wolf.util.ConfigManager;
import com.halvors.Wolf.util.WorldConfig;
import com.halvors.Wolf.wolf.WolfInventory;
import com.halvors.Wolf.wolf.WolfManager;
import com.halvors.Wolf.wolf.WolfTable;

/**
 * Handle events for all Entity related events.
 * 
 * @author halvors
 */
public class WolfEntityListener extends EntityListener {
    private final com.halvors.Wolf.Wolf plugin;
    
    private final ConfigManager configManager;
    private final WolfManager wolfManager;
    
    public WolfEntityListener(final com.halvors.Wolf.Wolf plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.wolfManager = plugin.getWolfManager();
    }
    
    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
//                Wolf wolf = (Wolf) entity;
                
                if (!worldConfig.wolfEnable) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            
            if (event instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                
                if (entity instanceof Wolf) {
                    Wolf wolf = (Wolf) entity;

                    if (wolfManager.hasWolf(wolf)) {
                    	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                        
                    	if (damager instanceof Player) {
                    		Player attacker = (Player)damager;
                        
                    		if (wolf.getOwner().equals(attacker)) {
                    			Player owner = (Player)wolf.getOwner();
                    			Material item = owner.getItemInHand().getType();
                                
                    			if (item.equals(Material.BONE)) {
                    				if (plugin.hasPermissions(owner, "Wolf.info")) {
                    					int health = wolf.getHealth();
                    					int maxHealth = 20;
                                        
                    					owner.sendMessage("Name: " + ChatColor.YELLOW + wolf1.getName());
                    					owner.sendMessage("Health: " + ChatColor.YELLOW + Integer.toString(health) + "/" + Integer.toString(maxHealth));
                                    
                    					event.setCancelled(true);

                    					wolf.setSitting(!wolf.isSitting());
                    					
                    					return;
                    				}
                    			}
                    		}
                    	}
                    	
                    	if (wolf1.hasInventory()) {
                    		WolfInventory wi = wolf1.getInventory();
                    		
                    		for (int i = 0; i < wi.getSize(); i++) {
                            	if (wi.getItem(i) != null && wi.getItem(i).getAmount() > 0 
                            			&& wi.getItem(i).getTypeId() > 0  && wi.getItem(i).getDurability() > -1) {
                            		
                            		Material item = wi.getItem(i).getType();
                            		
                            		if (item.equals(Material.PORK) || item.equals(Material.GRILLED_PORK)) {
                            			// TODO: Add health for pork and remove used pork from chest.
                            		}
                            	}
                            }
                    	}
                    }
                }
            }
        }
    }
    
    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();
        
        if (entity instanceof Wolf) {
            Wolf wolf = (Wolf) entity;
            UUID uniqueId = wolf.getUniqueId();
            
            if (wolf.isTamed() && wolfManager.hasWolf(wolf)) {
            	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
//            	Player owner = (Player) wolf.getOwner();
            	
                if (wolf1.hasInventory()) {
                    WolfInventory wi = plugin.getWolfInventoryManager().getWolfInventory(uniqueId);
                    
                    for (int i = 0; i < wi.getSize(); i++) { // Actually 27
                    	if (wi.getItem(i) != null && wi.getItem(i).getAmount() > 0 
                    			&& wi.getItem(i).getTypeId() > 0  && wi.getItem(i).getDurability() > -1) {
                    		world.dropItem(wolf.getLocation(), wi.getItem(i));
                    	}
                    }
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
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (worldConfig.limitEnable) {
                	List<WolfTable> wts = wolfManager.getWolfTables(player.getName());
                	
                	int size = wts.size();
                	int limit = worldConfig.limitValue;
                	
                	if (size >= limit) {
                		player.sendMessage("You can't tame this wolf, you already have " + ChatColor.YELLOW + Integer.toString(size) + ChatColor.WHITE + " of " + ChatColor.YELLOW + Integer.toString(limit) + ChatColor.WHITE + ".");
                		
                		event.setCancelled(true);
                	}
                }
                
                if (wolfManager.addWolf(wolf)) {
                	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

                	player.sendMessage("This is " + ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + ".");
                	player.sendMessage("You can change name with /setname <name>");
                } else {
                	// TODO: Add error message here.
                }
            }
        }
    }
    
    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            Entity target = event.getTarget();
            
            if (entity instanceof Wolf) {
//                Wolf wolf = (Wolf) target;
                
                if (target instanceof Player) {
//                	Player player = (Player) target;
                }
            }
        }
    }
    
    
    @Override
    public void onItemSpawn(ItemSpawnEvent event) {
    	if (!event.isCancelled()) {
    		if (event.getEntity() instanceof Item) {
    			Item item = (Item) event.getEntity();
    			List<Entity> entities = item.getNearbyEntities(1, 0, 1); // TODO: Figure out position here
    			
    			for (Entity entity : entities) {
    				if (entity instanceof Wolf) {
    					Wolf wolf = (Wolf) entity;
    				
    					if (wolfManager.hasWolf(wolf)) {
    						com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

    						if (wolf1.hasInventory()) {
    							WolfInventory wi = wolf1.getInventory();
    							
    							// Add item to inventory and remove it.
    							wi.addItem(item.getItemStack());
    							item.remove();
    						}
    					}
    				}
    			}
    		}
    	}
    }
}
