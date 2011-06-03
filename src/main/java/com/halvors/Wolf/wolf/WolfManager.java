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

package com.halvors.Wolf.wolf;

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

//import com.avaje.ebean.EbeanServer;
import com.halvors.Wolf.chest.TileEntityVirtualChest;

/**
 * Handle wolves
 * 
 * @author halvors
 */
public class WolfManager {
	private final com.halvors.Wolf.Wolf plugin;
	
//	private final EbeanServer database; // TODO: Use this instead of plugin.getDatabase().
	private final HashMap<Integer, TileEntityVirtualChest> inventory;
	
	public WolfManager(final com.halvors.Wolf.Wolf plugin) {
		this.plugin = plugin;
//		this.database = plugin.getDatabase();
		this.inventory = new HashMap<Integer, TileEntityVirtualChest>();
	}
	
	/**
	 * Get WolfTable
	 * 
	 * @param name
	 * @return WolfTable
	 */
	public WolfTable getWolfTable(final int entityId)
	{
		return plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
	}
	
	/**
	 * Get WolfTable by name
	 * 
	 * @param name
	 * @return
	 */
	public WolfTable getWolfTable(final String name)
	{
		return getWolfTable(getEntityId(name));
	}
	
	/**
	 * Get all WolfTables
	 * 
	 * @return List<WolfTable>
	 */
	public List<WolfTable> getWolfTables()
	{
		return plugin.getDatabase().find(WolfTable.class).findList();
	}
	
	/**
	 * Get all owners WolfTables
	 * 
	 * @param owner
	 * @return List<WolfTable>
	 */
	public List<WolfTable> getWolfTables(final String owner)
	{
		return plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
	}
	
	/**
	 * Add a wolf
	 * 
	 * @param name
	 * @param wolf
	 */
	public void addWolf(final Wolf wolf, final String name)
	{
		if (wolf.isTamed()) {
			Player player = (Player) wolf.getOwner();
			
			// Add wolf to table
			WolfTable wolfTable = new WolfTable();
			wolfTable.setName(name);
			wolfTable.setOwner(player.getName());
			wolfTable.setEntityId(wolf.getEntityId());
			wolfTable.setLocationX(wolf.getLocation().getX());
			wolfTable.setLocationY(wolf.getLocation().getY());
			wolfTable.setLocationZ(wolf.getLocation().getZ());
			wolfTable.setWorld(wolf.getWorld().getName());
			
			// Add wolf inventory
			inventory.put(wolf.getEntityId(), new TileEntityVirtualChest());
			
			plugin.getDatabase().save(wolfTable);
		}
	}
	
	/**
	 * Add a wolf with random name
	 * 
	 * @param wolf
	 */
	public void addWolf(final Wolf wolf)
	{
		addWolf(wolf, getRandomName());
	}
	
	/**
	 * Remove a wolf
	 * 
	 * @param name
	 */
	public void removeWolf(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
		if (wolfTable != null)
		{
			inventory.remove(entityId);
			plugin.getDatabase().delete(wolfTable);
		}
	}
	
	/**
	 * Remove a wolf by name
	 * 
	 * @param name
	 */
	public void removeWolf(final String name)
	{
		removeWolf(getEntityId(name));
	}
	
	/**
	 * Get wolf by entityid
	 * 
	 * @param name
	 * @return Wolf
	 */
	public Wolf getWolf(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
		for (Entity entity : plugin.getServer().getWorld(wolfTable.getWorld()).getEntities())
		{
			if (entity instanceof Wolf)
			{
				if (wolfTable.getEntityId() == entity.getEntityId()) 
				{
					Wolf wolf = (Wolf) entity;
				
					return wolf;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get wolf by location
	 * 
	 * @param location
	 * @return WolfTable
	 */
	public WolfTable getWolf(final Location location)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("locationX", location.getX())
			.eq("locationY", location.getY()).eq("locationZ", location.getZ()).findUnique();
		
		if (wolfTable != null)
		{
			return wolfTable;
		}
		
		return null;
	}
	
	/**
	 * Get wolf by name
	 * 
	 * @param name
	 * @return Wolf
	 */
	public Wolf getWolf(final String name)
	{
		return getWolf(getEntityId(name));
	}
		
	/**
	 * Get all players wolves
	 * 
	 * @param owner
	 * @return List<Wolf>
	 */
	public List<Wolf> getWolves(final String owner)
	{	
		List<WolfTable> wolfTables = plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
		List<Wolf> wolves = new ArrayList<Wolf>();
		
		for (WolfTable wolfTable : wolfTables)
		{
			for (Entity entity : plugin.getServer().getWorld(wolfTable.getWorld()).getEntities())
			{
				if (entity instanceof Wolf && wolfTable.getEntityId() == entity.getEntityId())
				{
					Wolf wolf = (Wolf) entity;
					wolves.add(wolf);
				}
			}
		}
		
		return wolves;
	}
	
	/**
	 * Get wolf name by entityId
	 * 
	 * @param entityId
	 * @return String
	 */
	public String getName(final int entityId) 
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
		return wolfTable.getName();
	}
	
	/**
	 * Get the wolf's owner
	 * 
	 * @param entityId
	 * @return Player
	 */
	public Player getOwner(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
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
	public Player getOwner(final String name)
	{
		return getOwner(getEntityId(name));
	}
	
	/**
	 * Get wolf's location
	 * 
	 * @param entityId
	 * @return Location
	 */
	public Location getLocation(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
		World world = plugin.getServer().getWorld(wolfTable.getWorld());
		double x = wolfTable.getLocationX();
		double y = wolfTable.getLocationY();
		double z = wolfTable.getLocationZ();
		
		Location location = new Location(world, x, y, z);
		
		return location;
	}
	
	/**
	 * Get the wolf's location by name
	 * 
	 * @param name
	 * @return Location
	 */
	public Location getLocation(final String name)
	{
		return getLocation(getEntityId(name));
	}
	
	/**
	 * Get the wolf's world
	 * 
	 * @param entityId
	 * @return World
	 */
	public World getWorld(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
		return plugin.getServer().getWorld(wolfTable.getWorld());
	}
	
	/**
	 * Get the wolf's world by name
	 * 
	 * @param name
	 * @return
	 */
	public World getWorld(final String name)
	{
		return getWorld(getEntityId(name));
	}
	
	/**
	 * Get wolf's inventory
	 * 
	 * @param entityId
	 * @return
	 */
	public TileEntityVirtualChest getInventory(final int entityId)
	{
		if (!inventory.containsKey(entityId)) { // Temporary fix.
			inventory.put(entityId, new TileEntityVirtualChest());
		}
		
		return inventory.get(entityId);
	}
	
	/** 
	 * Check if wolf exist
	 * 
	 * @param entityId
	 * @return
	 */
	public boolean hasWolf(final int entityId)
	{
		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
		
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
	public boolean hasWolf(final String name)
	{
		return hasWolf(getEntityId(name));
	}
	
	public void updateWolf(final WolfTable wolfTable)
	{
		plugin.getDatabase().update(wolfTable);
	}
	
	/**
	 * Get wolf's entityId
	 * 
	 * @param name
	 * @return
	 */
	public int getEntityId(final String name)
	{
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
	public Wolf spawnWolf(final Player player, final World world, final Location location)
	{
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
	public Wolf spawnWolf(final Player player)
	{
		return spawnWolf(player, player.getWorld(), player.getLocation());
	}
	
	/**
	 * Release a wolf
	 * 
	 * @param wolf
	 */
	public void releaseWolf(final Wolf wolf)
	{
//		WolfTable wolfTable = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", wolf.getEntityId()).findUnique();
		
		if (hasWolf(wolf.getEntityId())) {
			removeWolf(wolf.getEntityId());
		}
		
		wolf.setTamed(false);
	}
	
	/**
	 * Generate a random name
	 * 
	 * @return
	 */
	public String getRandomName() // TODO: Improve this.
	{
		Random random = new Random();
        List<String> names = new ArrayList<String>();
        String name = "Wolf";
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(WolfManager.class.getResourceAsStream("wolfnames.txt")));
           
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
