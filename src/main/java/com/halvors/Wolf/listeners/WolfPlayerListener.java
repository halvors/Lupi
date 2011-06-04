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

import com.halvors.Wolf.util.ConfigManager;
import com.halvors.Wolf.util.WorldConfig;
import com.halvors.Wolf.wolf.SelectedWolfManager;
import com.halvors.Wolf.wolf.WolfManager;

/**
 * Handle events for all Player related events
 * 
 * @author halvors
 */
public class WolfPlayerListener extends PlayerListener {
    private final com.halvors.Wolf.Wolf plugin;
    
    private final ConfigManager configManager;
    private final WolfManager wolfManager;
    private final SelectedWolfManager selectedWolfManager;
    
    public WolfPlayerListener(final com.halvors.Wolf.Wolf plugin) {
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
            Location pos = player.getTargetBlock(null, 120).getLocation();
                
            if (event.getItem().getType() == Material.SADDLE) {
                if (plugin.hasPermissions(player, "Wolf.target")) {
                    if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                        PathPoint[] pathPoint = { new PathPoint(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ()) };
                        EntityWolf wolf = ((CraftWolf) selectedWolfManager.getSelectedWolf(player.getName())).getHandle();
                        wolf.a(new PathEntity(pathPoint));
                        
                        player.sendMessage(ChatColor.GREEN + "Wolf target set.");
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
                
                // Adds the wolf to database.
                if (!wolfManager.hasWolf(wolf.getEntityId()) && wolf.isTamed() && wolf.getOwner().equals(player)) {
                    wolfManager.addWolf(wolf);
                }
                player.sendMessage("&eWolf EntityId: " + wolf.getEntityId() + ", Database ID: " + wolfManager.getWolfTable(wolf.getEntityId()).getId());
                if (player.getItemInHand().getTypeId() == worldConfig.item) {
                    if (plugin.hasPermissions(player, "Wolf.select")) {
                        if (wolf.isTamed() && wolf.getOwner().equals(player)) {
                            if (!wolfManager.hasWolf(wolf.getEntityId())) {
                                wolfManager.addWolf(wolf);
                                
                                player.sendMessage(ChatColor.GREEN + "Your wolf was named: " + ChatColor.YELLOW + wolfManager.getName(wolf.getEntityId()));
                            }
                            
                            selectedWolfManager.addSelectedWolf(player.getName(), wolf);
                            
                            player.sendMessage(ChatColor.GREEN + "Wolf selected.");
                        }
                    }
                }
            }
        }
    }
}