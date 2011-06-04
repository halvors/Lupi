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

import net.minecraft.server.EntityPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.halvors.Wolf.util.ConfigManager;
import com.halvors.Wolf.util.WorldConfig;
import com.halvors.Wolf.wolf.WolfInventoryManager;
import com.halvors.Wolf.wolf.WolfManager;

/**
 * Handle events for all Entity related events
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
//              Wolf wolf = (Wolf) entity;
                
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
                    
                    if (damager instanceof Player) {
                        Player attacker = (Player) damager;
                        
                        if (wolf.isTamed() && wolfManager.hasWolf(wolf.getEntityId())) {
                            Player player = (Player) wolf.getOwner();
                            
                            if (attacker.equals(player)) {
                                Material item = player.getItemInHand().getType();
                                
                                if (item.equals(Material.BONE)) {
                                    if (plugin.hasPermissions(player, "Wolf.info")) {
                                        String name = wolfManager.getName(wolf.getEntityId());
                                        int health = wolf.getHealth();
                                        int maxHealth = 20;
                                        
                                        player.sendMessage("Name: " + ChatColor.YELLOW + name);
                                        player.sendMessage("Health: " + ChatColor.YELLOW + Integer.toString(health) + "/" + maxHealth);
                                        
                                        event.setCancelled(true);
                                        
                                        return;
                                    }
                                } else if (item.equals(Material.CHEST)) {
                                    if (plugin.hasPermissions(player, "Wolf.chest")) {
                                    	EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                                    	entityPlayer.a(wolfManager.getInventory(wolf.getEntityId()));
                                        
                                        event.setCancelled(true);
                                        
                                        return;
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
        
        if (entity instanceof Wolf) {
            Wolf wolf = (Wolf) entity;
            
            if (wolf.isTamed() && wolfManager.hasWolf(wolf.getEntityId())) {
                wolfManager.removeWolf(wolf.getEntityId());
                
                // TODO: Drop items from inventory.
            }
        }
    }
    
    /*
    @Override
    public void onEntityTame(EntityTameEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            Player player = (Player) event.getOwner();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                int size = wolfManager.getWolves(player.getName()).size();
                int limit = worldConfig.wolfLimit;
                
                if (size != 0 && size <= limit) {
                    return;
                }
                
                if (!wolfManager.hasWolf(wolf.getEntityId())) {
                    wolfManager.addWolf(wolf);
                    
                    player.sendMessage(ChatColor.GREEN + "You're wolf is now tame. Type /name <name> for give your new wolf a name.");
                }
            }
        }
    }
    */
    
    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        if (!event.isCancelled()) {
            Entity entity = event.getEntity();
            Entity target = event.getTarget();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
        
            if (entity instanceof Wolf) {
//              Wolf wolf = (Wolf) entity;
                
                if (target instanceof Player) {
//                  Player player = (Player) target;
                
                    if (worldConfig.wolfPeaceful) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}