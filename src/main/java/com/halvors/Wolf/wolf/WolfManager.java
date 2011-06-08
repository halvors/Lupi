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
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

//import com.halvors.Wolf.chest.TileEntityVirtualChest;

/**
 * Handle wolves
 * 
 * @author halvors
 */
public class WolfManager {
    private final com.halvors.Wolf.Wolf plugin;
    
    private WolfInventoryManager wolfInventoryManager;
    
    public WolfManager(com.halvors.Wolf.Wolf plugin) {
        this.plugin = plugin;
        this.wolfInventoryManager = plugin.getWolfInventoryManager();
    }
    
    /**
     * Get WolfTable by entityId
     * 
     * @param name
     * @return WolfTable
     */
    public WolfTable getWolfTable(int entityId) {
        return plugin.getDatabase().find(WolfTable.class).where()
            .eq("entityId", entityId).findUnique();
    }
    
    /**
     * Get WolfTable by name and owner
     * 
     * @param name
     * @return WolfTable
     */
    public WolfTable getWolfTable(String name, String owner) {
        return plugin.getDatabase().find(WolfTable.class).where()
            .eq("name", name).eq("owner", owner).findUnique();
    }
    
    /**
     * Get WolfTable by location
     * 
     * @param location
     * @return
     */
    public WolfTable getWolfTable(Location location) {
        return plugin.getDatabase().find(WolfTable.class).where()
            .eq("locationX", location.getBlockX()).eq("locationY", location.getBlockY()).eq("locationZ", location.getBlockZ()).findUnique();
    }

    /**
     * Update WolfTable
     * 
     * @param wt
     */
    public void updateWolfTable(WolfTable wt) {
        plugin.getDatabase().update(wt);
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
    public List<WolfTable> getWolfTables(String owner) {
        return plugin.getDatabase().find(WolfTable.class).where()
            .ieq("owner", owner).findList();
    }
    
    /**
     * Add a wolf
     * 
     * @param name
     * @param wolf
     */
    public void addWolf(Wolf wolf, String name) {
        if (wolf.isTamed()) {
            Player player = (Player)wolf.getOwner();
            
            // Check if a wolf with same name already exists.
            for (WolfTable wt : getWolfTables(player.getName())) {
                if (wt.getName().equalsIgnoreCase(name)) {
                    // TODO: Maybe give a random name instead of return
                    
                    return;
                }
            }
            
            // Create a new WolfTable
            WolfTable wt = new WolfTable();
            wt.setEntityId(wolf.getEntityId());
            wt.setName(name);
            wt.setOwner(player.getName());
            wt.setLocation(wolf.getLocation());
            wt.setWorld(wolf.getWorld().getName());
            wt.setInventory(false);
            
            // Save the wolf to the database
            plugin.getDatabase().save(wt);
            
            // Pull a fresh copy of the wolf to retrieve the database ID
            wt = getWolfTable(wolf.getEntityId());
            
            // Add wolf inventory
            plugin.getWolfInventoryManager().addWolfInventory(wt.getId());
        }
    }
    
    /**
     * Add a wolf with random name
     * 
     * @param wolf
     */
    public void addWolf(Wolf wolf) {
        addWolf(wolf, getRandomName());
    }
    
    /**
     * Remove a wolf by entityId
     * 
     * @param name
     */
    public void removeWolf(int entityId) {
        WolfTable wt = getWolfTable(entityId);
        
        if (wt != null) {
            plugin.getWolfInventoryManager().removeWolfInventory(wt.getId());
            plugin.getDatabase().delete(wt);
        }
    }
    
    /**
     * Remove a wolf by name
     * 
     * @param name
     */
    public void removeWolf(String name, String owner) {
        removeWolf(getEntityId(name, owner));
    }
    
    /**
     * Check if wolf exist by WolfTable
     * 
     * @param wt
     * @return Boolean
     */
    public boolean hasWolf(WolfTable wt) {
        if (wt != null) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if wolf exist by name and owner
     * 
     * @param name
     * @return Boolean
     */
    public boolean hasWolf(String name, String owner) {
        return hasWolf(getWolfTable(name, owner));
    }
    
    /** 
     * Check if wolf exist by entityId
     * 
     * @param entityId
     * @return Boolean
     */
    public boolean hasWolf(int entityId) {
        return hasWolf(getWolfTable(entityId));
    }
    
    /**
     * Get wolf by WolfTable
     * 
     * @param wt
     * @return Wolf
     */
    public Wolf getWolf(WolfTable wt) {
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
     * Get wolf by name and owner
     * 
     * @param name
     * @return Wolf
     */
    public Wolf getWolf(String name, String owner) {
        return getWolf(getWolfTable(name, owner));
    }
    
    /**
     * Get wolf by entityid
     * 
     * @param name
     * @return Wolf
     */
    public Wolf getWolf(int entityId) {
        return getWolf(getWolfTable(entityId));
    }
    
    /**
     * Get all wolves
     * 
     * @return
     */
    public List<Wolf> getWolves() {    
        List<WolfTable> wts = getWolfTables();
        List<Wolf> wolves = new ArrayList<Wolf>();
        
        for (WolfTable wt : wts) {
            for (Entity entity : plugin.getServer().getWorld(wt.getWorld()).getEntities()) {
                if (entity instanceof Wolf && entity.getEntityId() == wt.getEntityId()) {
                    wolves.add((Wolf) entity);
                }
            }
        }
        
        return wolves;
    }

    /**
     * Get all wolves by owner
     * 
     * @param owner
     * @return List<Wolf>
     */
    public List<Wolf> getWolves(String owner) {    
        List<WolfTable> wts = getWolfTables(owner);
        List<Wolf> wolves = new ArrayList<Wolf>();
        
        for (WolfTable wt : wts) {
            for (Entity entity : plugin.getServer().getWorld(wt.getWorld()).getEntities()) {
                if (entity instanceof Wolf && entity.getEntityId() == wt.getEntityId()) {
                    wolves.add((Wolf) entity);
                }
            }
        }
        
        return wolves;
    }
    
    /**
     * Get entityId by name and owner
     * 
     * @param name
     * @return Integer
     */
    public int getEntityId(String name, String owner) {
        WolfTable wt = getWolfTable(name, owner);
        
        if (wt != null) {
            return wt.getEntityId();
        }
        
        return 0;
    }
    
    /**
     * Get name by entityId
     * 
     * @param entityId
     * @return String
     */
    public String getName(int entityId) {
        WolfTable wt = getWolfTable(entityId);
        
        if (wt != null) {
            return wt.getName();
        }
        
        return null;
    }
    
    /**
     * Get wolf's owner by entityId
     * 
     * @param entityId
     * @return Player
     */
    public Player getOwner(int entityId) {
        WolfTable wt = getWolfTable(entityId);
        
        if (wt != null) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(wt.getOwner())) {
                    return player;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get location by entityId
     * 
     * @param entityId
     * @return Location
     */
    public Location getLocation(int entityId) {
        WolfTable wt = getWolfTable(entityId);
        
        if (wt != null) {
            return wt.getLocation();
        }
        
        return null;
    }
    
    /**
     * Get location by name and owner
     * 
     * @param name
     * @param owner
     * @return Location
     */
    public Location getLocation(String name, String owner) {
        return getLocation(getEntityId(name, owner));
    }
    
    /**
     * Get wolf's world by entityId
     * 
     * @param entityId
     * @return World
     */
    public World getWorld(int entityId) {
        WolfTable wt = getWolfTable(entityId);
        
        if (wt != null) {
            return plugin.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
    
    /**
     * Get wolf's world by name and owner
     * 
     * @param name
     * @return World
     */
    public World getWorld(String name, String owner) {
        return getWorld(getEntityId(name, owner));
    }
    
    /**
     * Check if wolf has inventory by entityId
     * 
     * @param entityId
     * @return Boolean
     */
    public boolean hasInventory(int entityId) {
    	WolfTable wt = getWolfTable(entityId);
    	
    	if (wt != null) {
    		return wt.getInventory();
    	}
    	
    	return false;
    }
    
    /**
     * Check if wolf has inventory by name and owner
     * 
     * @param name
     * @param owner
     * @return Boolean
     */
    public boolean hasInventory(String name, String owner) {
    	return hasInventory(getEntityId(name, owner));
    }
    
    /**
     * Spawn a wolf
     * 
     * @param player
     * @param world
     * @param location
     * @return Wolf
     */
    public Wolf spawnWolf(Player player, World world, Location location) {
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
    public Wolf spawnWolf(Player player) {
        return spawnWolf(player, player.getWorld(), player.getLocation());
    }
    
    /**
     * Release a wolf
     * 
     * @param wolf
     */
    public void releaseWolf(Wolf wolf) {
        if (hasWolf(wolf.getEntityId())) {
            removeWolf(wolf.getEntityId());
        }
        
        wolf.setTamed(false);
    }
    
    /**
     * Generate a random name
     * 
     * @return String
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
