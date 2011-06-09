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

import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Represents a wolf
 * 
 * @author halvors
 */
public class Wolf {
	private final com.halvors.Wolf.Wolf plugin;
	
	private UUID uniqueId;
	
	public Wolf(final com.halvors.Wolf.Wolf plugin, UUID uniqueId) {
		this.plugin = plugin;
		this.uniqueId = uniqueId;
	}
	
    /**
     * Get WolfTable
     * 
     * @return WolfTable
     */
    public WolfTable getWolfTable() {
    	return plugin.getDatabase().find(WolfTable.class).where()
    		.eq("uniqueId", uniqueId.toString()).findUnique();
    }

    /**
     * Save WolfTable
     */
    public void saveWolfTable() {
    	plugin.getDatabase().save(getWolfTable());
    }
    
    /**
     * Delete WolfTable
     */
    public void deleteWolfTable() {
    	plugin.getDatabase().delete(getWolfTable());
    }
    
    /**
     * Update WolfTable
     */
    public void updateWolfTable() {
        plugin.getDatabase().update(getWolfTable());
    }
	
	/**
	 * Get id
	 * 
	 * @return int
	 */
	public int getId() {
		WolfTable wt = getWolfTable();
		
		if (wt != null) {
			return wt.getId();
		}
		
		return 0;
	}
	
	/**
	 * Set id
	 * 
	 * @param id
	 */
	public void setId(int id) {
		WolfTable wt = getWolfTable();
		
		if (wt != null) {
			wt.setId(id);
		} 
	}
	
	/**
	 * Get uniqueId
	 * 
	 * @return UUID
	 */
	public UUID getUniqueId() {
		return uniqueId;
	}
	
	/**
	 * Set uniqueId
	 * 
	 * @param uniqueId
	 */
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}
    
    /**
     * Get name
     * 
     * @return String
     */
    public String getName() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return wt.getName();
        }
        
        return null;
    }
    
    /**
     * Set name
     * 
     * @param name
     */
    public void setName(String name) {
    	WolfTable wt = getWolfTable();
    	
    	if (wt != null) {
    		wt.setName(name);
    	}
    }
    
    /**
     * Get owner
     * 
     * @return Player
     */
    public Player getOwner() {
        WolfTable wt = getWolfTable();
        
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
     * Set owner
     * 
     * @param owner
     */
    public void setOwner(Player owner) {
    	WolfTable wt = getWolfTable();
    	
    	if (wt != null) {
    		wt.setOwner(owner.getName());
    		
    		getWolf().setOwner(owner);
    	}
    }
    
    /**
     * Get world
     * 
     * @return World
     */
    public World getWorld() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return plugin.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
    
    /**
     * Set world
     * 
     * @param world
     */
    public void setWorld(World world) {
    	WolfTable wt = getWolfTable();
    	
    	if (wt != null) {
    		wt.setWorld(world.getName());
    	}
    }
    
    /**
     * Check if inventory exists
     * 
     * @return boolean
     */
    public boolean hasInventory() {
    	WolfTable wt = getWolfTable();
    	
    	if (wt != null) {
    		return wt.isInventory();
    	}
    	
    	return false;
    }
    
    /**
     * Set inventory
     * 
     * @param inventory
     */
    public void setInventory(boolean inventory) {
    	WolfTable wt = getWolfTable();
    	
    	if (wt != null) {
    		wt.setInventory(inventory);
    	}
    }
    
    /**
     * Add wolf inventory
     */
    public void addInventory() {
        setInventory(true);
        updateWolfTable();
        plugin.getWolfInventoryManager().addWolfInventory(uniqueId);
	}

    /**
     * Remove wolf inventory
     */
    public void removeInventory() {
    	setInventory(false);
        
    	plugin.getWolfInventoryManager().removeWolfInventory(uniqueId);
    }

    /**
     * Get wolf
     * 
     * @return
     */
    public org.bukkit.entity.Wolf getWolf() {
    	List<Entity> entities = getWorld().getEntities();
    	
    	for (Entity entity : entities) {
            if (entity instanceof Wolf) {
                if (uniqueId.equals(entity.getUniqueId()))  {
                	org.bukkit.entity.Wolf wolf = (org.bukkit.entity.Wolf)entity;
                	
                    return wolf;
                }
            }
        }
    	
    	return null;
    }
}
