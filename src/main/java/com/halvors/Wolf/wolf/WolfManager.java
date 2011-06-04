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

/**
 * Handle wolves
 * 
 * @author halvors
 */
public class WolfManager {
    private final com.halvors.Wolf.Wolf plugin;
    
//    private final EbeanServer database; // TODO: Use this instead of plugin.getDatabase().
    private final HashMap<Integer, WolfInventory> wolfInventory;
    
    public WolfManager(final com.halvors.Wolf.Wolf plugin) {
        this.plugin = plugin;
//        this.database = plugin.getDatabase();
        this.wolfInventory = new HashMap<Integer, WolfInventory>();
    }
    
    /**
     * Get WolfTable
     * 
     * @param name
     * @return WolfTable
     */
    public WolfTable getWolfTable(final int entityId) {
        return plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
    }
    
    /**
     * Get WolfTable by name
     * 
     * @param name
     * @return
     */
    public WolfTable getWolfTable(final String name) {
        return getWolfTable(getEntityId(name));
    }
    
    /**
     * Get all WolfTables
     * 
     * @return List<WolfTable>
     */
    public List<WolfTable> getWolfTables() {
        return plugin.getDatabase().find(WolfTable.class).findList();
    }
    
    /**
     * Get all owners WolfTables
     * 
     * @param owner
     * @return List<WolfTable>
     */
    public List<WolfTable> getWolfTables(final String owner) {
        return plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
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
            
            // Check if a wolf with same name already exists.
            for (WolfTable wt : getWolfTables(player.getName())) {
                if (wt.getName() == name) {
                    // TODO: Maybe give a random name instead of return
                    
                    return;
                }
            }
            
            // Add wolf to table
            WolfTable wt = new WolfTable();
            wt.setName(name);
            wt.setOwner(player.getName());
            wt.setEntityId(wolf.getEntityId());
            wt.setLocationX(wolf.getLocation().getX());
            wt.setLocationY(wolf.getLocation().getY());
            wt.setLocationZ(wolf.getLocation().getZ());
            wt.setWorld(wolf.getWorld().getName());
            
            // Add wolf inventory
            wolfInventory.put(wolf.getEntityId(), new WolfInventory());
            
            plugin.getDatabase().save(wt);
        }
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
     * Remove a wolf
     * 
     * @param name
     */
    public void removeWolf(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
            wolfInventory.remove(entityId);
            plugin.getDatabase().delete(wt);
        }
    }
    
    /**
     * Remove a wolf by name
     * 
     * @param name
     */
    public void removeWolf(final String name) {
        removeWolf(getEntityId(name));
    }
    
    public void updateWolf(final WolfTable wt) {
        plugin.getDatabase().update(wt);
    }
    
    /**
     * Get wolf by entityid
     * 
     * @param name
     * @return Wolf
     */
    public Wolf getWolf(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        for (Entity entity : plugin.getServer().getWorld(wt.getWorld()).getEntities()) {
            if (entity instanceof Wolf) {
                if (wt.getEntityId() == entity.getEntityId())  {
                    return (Wolf) entity;
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
    public WolfTable getWolf(final Location location) { // TODO: Shouldn't this return Wolf? And not WolfTable?
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("locationX", location.getX())
            .eq("locationY", location.getY()).eq("locationZ", location.getZ()).findUnique();
        
        if (wt != null) {
            return wt;
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
        List<WolfTable> wts = plugin.getDatabase().find(WolfTable.class).where().ieq("owner", owner).findList();
        List<Wolf> wolves = new ArrayList<Wolf>();
        
        for (WolfTable wt : wts) {
            for (Entity entity : plugin.getServer().getWorld(wt.getWorld()).getEntities()) {
                if (entity instanceof Wolf && wt.getEntityId() == entity.getEntityId()) {
                    wolves.add((Wolf) entity);
                }
            }
        }
        
        return wolves;
    }
    
    /**
     * Get the wolf's owner
     * 
     * @param entityId
     * @return Player
     */
    public Player getWolfOwner(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
        	for (Player player : plugin.getServer().getOnlinePlayers()) {
        		if (player.getName() == wt.getOwner()) {
        			return player;
        		}
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
    public Player getWolfOwner(final String name) {
        return getWolfOwner(getEntityId(name));
    }
    
    /**
     * Get wolf's location
     * 
     * @param entityId
     * @return Location
     */
    public Location getWolfLocation(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
        	World world = plugin.getServer().getWorld(wt.getWorld());
        	double x = wt.getLocationX();
        	double y = wt.getLocationY();
        	double z = wt.getLocationZ();
        
        	Location location = new Location(world, x, y, z);
        
        	return location;
        }
        
        return null;
    }
    
    /**
     * Get the wolf's location by name
     * 
     * @param name
     * @return Location
     */
    public Location getWolfLocation(final String name) {
        return getWolfLocation(getEntityId(name));
    }
    
    /**
     * Get the wolf's world
     * 
     * @param entityId
     * @return World
     */
    public World getWolfWorld(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
        	return plugin.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
    
    /**
     * Get the wolf's world by name
     * 
     * @param name
     * @return
     */
    public World getWolfWorld(final String name) {
        return getWolfWorld(getEntityId(name));
    }
    
    /**
     * Get wolf's inventory by entityId
     * 
     * @param entityId
     * @return
     */
    public WolfInventory getWolfInventory(final int entityId) {
        return wolfInventory.get(entityId);
    }
    
    public WolfInventory getWolfInventory(final String name) {
        return getWolfInventory(getEntityId(name));
    }
    
    /** 
     * Check if wolf exist
     * 
     * @param entityId
     * @return
     */
    public boolean hasWolf(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
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
     * Get wolf's name by entityId
     * 
     * @param entityId
     * @return String
     */
    public String getName(final int entityId) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().eq("entityId", entityId).findUnique();
        
        if (wt != null) {
        	return wt.getName();
        }
        
        return null;
    }
    
    /**
     * Get wolf's entityId by name
     * 
     * @param name
     * @return
     */
    public int getEntityId(final String name) {
        WolfTable wt = plugin.getDatabase().find(WolfTable.class).where().ieq("name", name).findUnique();
        
        if (wt != null) {
        	return wt.getEntityId();
        }
        
        return 0;
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
    public String getRandomName() { // TODO: Improve this. 
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
