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

import java.util.UUID;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathPoint;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

import com.halvors.wolf.WolfPlugin;
import com.halvors.wolf.util.ConfigManager;
import com.halvors.wolf.util.WorldConfig;
import com.halvors.wolf.wolf.SelectedWolfManager;
import com.halvors.wolf.wolf.WolfManager;

/**
 * Handle events for all Player related events.
 * 
 * @author halvors
 */
public class WolfPlayerListener extends PlayerListener {
    private final WolfPlugin plugin;
    
    private final ConfigManager configManager;
    private final WolfManager wolfManager;
    private final SelectedWolfManager selectedWolfManager;
    
    public WolfPlayerListener(final WolfPlugin plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.wolfManager = plugin.getWolfManager();
        this.selectedWolfManager = plugin.getSelectedWolfManager();
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        String name = player.getName();
        
        if (event.hasItem() && selectedWolfManager.hasSelectedWolf(name)) {
            Wolf wolf = (Wolf) selectedWolfManager.getSelectedWolf(name);

            if (wolfManager.hasWolf(wolf)) {
                Material item = event.getItem().getType();
                Location pos = player.getTargetBlock(null, 120).getLocation();
                
                if (item.equals(Material.SADDLE)) {
                    if (plugin.hasPermissions(player, "Wolf.wolf.target")) {
                        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_AIR)) {
                            PathPoint[] pathPoint = { new PathPoint(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()) };
                            EntityWolf entityWolf = ((CraftWolf)wolf).getHandle();
                            entityWolf.a(new PathEntity(pathPoint));
                        
                            player.sendMessage(ChatColor.GREEN + "Wolf target set.");
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!event.isCancelled()) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            World world = entity.getWorld();
            WorldConfig worldConfig = configManager.getWorldConfig(world);
            
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                UUID uniqueId = wolf.getUniqueId();
                
                if (wolf.isTamed() && wolf.getOwner().equals(player)) {
                    
                    // Add tamed wolves that existed prior to plugin if wolf is tamed and player is the owner
                    if (!wolfManager.hasWolf(uniqueId)) {
                        wolfManager.addWolf(wolf);
                        com.halvors.wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                        player.sendMessage("This is " + ChatColor.YELLOW + wolf1.getName());
                        player.sendMessage("You can change name with /wolf setname <name>");
                    }
                    
                    com.halvors.wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                    Material item = player.getItemInHand().getType();
                    
                    if (item.equals(Material.BONE)) {
                        if (plugin.hasPermissions(player, "Wolf.wolf.select")) {
                            selectedWolfManager.addSelectedWolf(player.getName(), wolf);
                                
                            player.sendMessage(ChatColor.GREEN + "Wolf selected.");
                            
//                            wolf.setSitting(!wolf.isSitting());
                        }
                    } else if (item.equals(Material.CHEST)) {
                        if (plugin.hasPermissions(player, "Wolf.wolf.inventory")) {
                            if (worldConfig.wolfInventory) {
                                if (wolf1.hasInventory()) {
                                    EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                                    entityPlayer.a(wolf1.getInventory());
                                } else {
                                    // Add inventory.
                                    wolf1.addInventory();
                                        
                                    // Remove 1 chest for players inventory.
                                    if (item.equals(Material.CHEST)) {
                                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                                    }
                                        
                                    player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " has now inventory. Right click with a chest to open it.");
                                }
                            
//                                wolf.setSitting(!wolf.isSitting());
                            }
                        }
                    }
                }
            }
        }
    }
}
