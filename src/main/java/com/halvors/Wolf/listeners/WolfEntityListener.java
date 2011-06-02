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
import com.halvors.Wolf.wolf.SelectedWolfManager;
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
	private final SelectedWolfManager selectedWolfManager;
	
	public WolfEntityListener(final com.halvors.Wolf.Wolf plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.wolfManager = plugin.getWolfManager();
		this.selectedWolfManager = plugin.getSelectedWolfManager();
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!event.isCancelled()) {
			Entity entity = event.getEntity();
			World world = entity.getWorld();
			WorldConfig worldConfig = configManager.getWorldConfig(world);
			
			if (entity instanceof org.bukkit.entity.Wolf) {
				Wolf wolf = (Wolf) entity;
				
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
				
				if (entity instanceof org.bukkit.entity.Wolf) {
					org.bukkit.entity.Wolf wolf = (org.bukkit.entity.Wolf) entity;
					
					if (damager instanceof Player) {
						Player attacker = (Player) damager;
						
						if (wolf.isTamed()) {
							Player player = (Player) wolf.getOwner();
							
							if (player == attacker) {
								Material item = player.getItemInHand().getType();
								
								if (item == Material.DIRT) {
									if (plugin.hasPermissions(player, "WolfControl.info")) {
										int health = wolf.getHealth();
										int maxHealth = 20;
										
										player.sendMessage(ChatColor.YELLOW + wolfManager.getName(wolf.getEntityId()) + ChatColor.GREEN + " health is " + Integer.toString(health) + "/" + maxHealth + ".");
										
//										wolf.setSitting(true);
										event.setCancelled(true);
										
										return;
									}
								} else if (item == Material.CHEST) {
						            if (plugin.hasPermissions(player, "WolfControl.chest")) {
						            	EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
						            	entityPlayer.a(wolfManager.getInventory(wolf.getEntityId()));
						            	
//						            	wolf.setSitting(true);
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
		
		if (entity instanceof org.bukkit.entity.Wolf) {
			org.bukkit.entity.Wolf wolf = (org.bukkit.entity.Wolf) entity;
			
			if (wolf.isTamed()) {
				wolfManager.removeWolf(wolf.getEntityId());
			}
			
			// TODO: Remove dead wolves.
		}
	}
	
	/*
	@Override
	public void onEntityTame(EntityTameEvent event) {
		if (!event.isCancelled()) {
			Entity entity = event.getEntity();
			Player player = (Player) event.getOwner();
			
			if (entity instanceof org.bukkit.entity.Wolf) {
				org.bukkit.entity.Wolf wolf = (org.bukkit.entity.Wolf) entity;
				wolfManager.addWolf(wolf);
				
				player.sendMessage(ChatColor.GREEN + "You're wolf is now tame. Type /name <name> for give your new wolf a name.");
				
				// TODO: Add wolf limit.
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
		
			if (entity instanceof org.bukkit.entity.Wolf) {
				org.bukkit.entity.Wolf wolf = (org.bukkit.entity.Wolf)entity;
				
				if (target instanceof Player) {
					Player player = (Player)target;
				
					if (worldConfig.wolfPeaceful) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}