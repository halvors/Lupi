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

package com.halvors.WolfControl.wolf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.avaje.ebean.EbeanServer;
import com.halvors.WolfControl.WolfControl;
import com.halvors.WolfControl.chest.TileEntityVirtualChest;

/**
 * Handle wolves
 * 
 * @author halvors
 */
public class WolfManager {
	private final WolfControl plugin;
	
	private final EbeanServer database;
	private final HashMap<Integer, TileEntityVirtualChest> inventory;
	
	public WolfManager(final WolfControl plugin) {
		this.plugin = plugin;
		this.database = plugin.getDatabase();
		this.inventory = new HashMap<Integer, TileEntityVirtualChest>();
	}
	
	/**
	 * Get a WolfTable
	 * 
	 * @param name
	 * @return WolfTable
	 */
	public WolfTable getWolfTable(final int entityid) {
		return plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
	}
	
	/**
	 * Get all WolfTables
	 * 
	 * @return List<WolfTable>
	 */
	public List<WolfTable> getWolfTables() {
		return plugin.getDatabase().find(WolfTable.class).findList();
	}
	
	public List<WolfTable> getWolfTables(final String owner) {
		return plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
	}
	
	/**
	 * Add a wolf with random name
	 * 
	 * @param wolf
	 */
	public void addWolf(final Wolf wolf) {
		addWolf(wolf, getRandomName());
	}
	
	/**
	 * Add a wolf
	 * 
	 * @param name
	 * @param wolf
	 */
	public void addWolf(final Wolf wolf, final String name) {
		if (wolf.isTamed()) {
			Player player = (Player) wolf.getOwner();
			
			// Set wolf variables
			WolfTable wolfTable = new WolfTable();
			wolfTable.setName(name);
			wolfTable.setOwner(player.getName());
			wolfTable.setEntityId(wolf.getEntityId());
			wolfTable.setWorld(wolf.getWorld().getName());
			
			// Add wolf inventory
			inventory.put(wolf.getEntityId(), new TileEntityVirtualChest());
			
//			plugin.getDatabase().find(WolfTable.class).findList().add(wolfTable);
			plugin.getDatabase().save(wolfTable);
		}
	}
	
	/**
	 * Remove a wolf
	 * 
	 * @param name
	 */
	public void removeWolf(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		if (wolfTable != null) {
			inventory.remove(entityid);
			plugin.getDatabase().delete(wolfTable);
		}
	}
	
	/**
	 * Get wolf
	 * 
	 * @param name
	 * @return Wolf
	 */
	public Wolf getWolf(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		for (Entity entity : plugin.getServer().getWorld(wolfTable.getWorld()).getEntities()) {
			if (entity instanceof Wolf) {
				if (wolfTable.getEntityId() == entity.getEntityId()) {
					Wolf wolf = (Wolf) entity;
				
					return wolf;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get wolf by name
	 * 
	 * @param name
	 * @return Wolf
	 */
	public Wolf getWolf(final String name) {
		return getWolf(getEntityId(name));
	}
	
	/**
	 * Get all players wolves
	 * 
	 * @param owner
	 * @return List<Wolf>
	 */
	public List<Wolf> getWolves(final String owner) {	
		List<WolfTable> wolfTables = plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
		List<Wolf> wolves = new ArrayList<Wolf>();
		
		for (WolfTable wolfTable : wolfTables) {
			for (Entity entity : plugin.getServer().getWorld(wolfTable.getWorld()).getEntities()) {
				if ((entity instanceof Wolf) && (wolfTable.getEntityId() == entity.getEntityId())) {
					Wolf wolf = (Wolf) entity;
					wolves.add(wolf);
				}
			}
		}
		
		return wolves;
	}
	
	/**
	 * Get wolf name by entityid
	 * 
	 * @param entityid
	 * @return String
	 */
	public String getName(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		return wolfTable.getName();
	}
	
	/**
	 * Get the wolf's owner
	 * 
	 * @param entityid
	 * @return Player
	 */
	public Player getOwner(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (player.getName() == wolfTable.getOwner()) {
				return player;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the wolf's owner by name
	 * 
	 * @param name
	 * @return
	 */
	public Player getOwner(final String name) {
		return getOwner(getEntityId(name));
	}
	
	/**
	 * Get the wolf's world
	 * 
	 * @param entityid
	 * @return World
	 */
	public World getWorld(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		return plugin.getServer().getWorld(wolfTable.getWorld());
	}
	
	/**
	 * Get the wolf's world by name
	 * 
	 * @param name
	 * @return
	 */
	public World getWorld(final String name) {
		return getWorld(getEntityId(name));
	}
	
	/**
	 * Get wolf's inventory
	 * 
	 * @param entityid
	 * @return
	 */
	public TileEntityVirtualChest getInventory(final int entityid) {
		if (!inventory.containsKey(entityid)) { // Temporary fix.
			inventory.put(entityid, new TileEntityVirtualChest());
		}
		
		return inventory.get(entityid);
	}
	
	/** 
	 * Check if wolf exist
	 * 
	 * @param entityid
	 * @return
	 */
	public boolean hasWolf(final int entityid) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", entityid).findUnique();
		
		if (wolfTable != null) {
			return true;
		}
		
		return false;
	}

	/**
	 * Check if wolf exist by name
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasWolf(final String name) {
		return hasWolf(getEntityId(name));
	}
	
	/**
	 * Get wolf's entityid
	 * 
	 * @param name
	 * @return
	 */
	public int getEntityId(final String name) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().ieq("name", name).findUnique();
		
		return wolfTable.getEntityId();
	}
	
	/**
	 * Spawn a wolf
	 * 
	 * @param player
	 * @param world
	 * @param location
	 * @return
	 */
	public Wolf spawnWolf(final Player player, final World world, final Location location) {
		Wolf wolf = (Wolf) world.spawnCreature(location, CreatureType.WOLF);
		wolf.setTamed(true);
		wolf.setOwner(player);
		
		return wolf;
	}
	
	/**
	 * Spawn a wolf at player's position
	 * 
	 * @param player
	 * @param tame
	 * @return Wolf
	 */
	public Wolf spawnWolf(final Player player) {
		return spawnWolf(player, player.getWorld(), player.getLocation());
	}
	
	/**
	 * Release a wolf
	 * 
	 * @param wolf
	 */
	public void releaseWolf(final Wolf wolf) {
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityid", wolf.getEntityId()).findUnique();
		
		if (wolfTable != null) {
			plugin.getDatabase().delete(wolfTable);
		}
		
		wolf.setTamed(false);
	}
	
	/**
	 * Generate a random name
	 * 
	 * @return
	 */
	public String getRandomName() {
		Random random = new Random();
        List<String> names = new ArrayList<String>();
        String name = "Wolf";
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(WolfManager.class.getResourceAsStream("wolfnames.txt")));
            String s = "";
            
            do {
                String s1;
                if ((s1 = bufferedReader.readLine()) == null) {
                    break;
                }
                
                s1 = s1.trim();
                
                if (s1.length() > 0) {
                    names.add(s1);
                }
            } while (true);
            
            name = (String)names.get(random.nextInt(names.size()));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return name;
	}
}
