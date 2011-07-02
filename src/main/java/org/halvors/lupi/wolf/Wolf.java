/* 
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 * Copyright (C) 2011 adamonline45 <adamonline45@gmail.com>
 * 
 * This file is part of Lupi.
 * 
 * Lupi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Lupi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Lupi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.halvors.lupi.wolf;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.wolf.inventory.WolfInventory;
import org.halvors.lupi.wolf.inventory.WolfInventoryManager;

import com.avaje.ebean.EbeanServer;

/**
 * Represents a tamed wolf.
 * 
 * @author halvors
 */
public class Wolf {
	private final static EbeanServer db = Lupi.getDb();
	
    private UUID uniqueId;
    
    public Wolf(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    /**
     * Get WolfTable.
     * 
     * @return
     */
    public WolfTable getWolfTable() {
        return db.find(WolfTable.class).where()
            .ieq("uniqueId", uniqueId.toString()).findUnique();
    }
    
    /**
     * Get uniqueId.
     * 
     * @return
     */
    public UUID getUniqueId() {
    	return uniqueId;
    }
    
    /**
     * Get name.
     * 
     * @return
     */
    public String getName() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return wt.getName();
        }
        
        return null;
    }
    
    /**
     * Set name.
     * 
     * @param name
     */
    public void setName(String name) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setName(name);
            
            db.update(wt);
            
            if (hasLoadedInventory()) {
                getInventory().setName(name + "'s inventory");
            }
        }
    }
    
    /**
     * Get owner.
     * 
     * @return
     */
    public Player getOwner() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(wt.getOwner())) {
                    return player;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Set owner.
     * 
     * @param owner
     */
    public void setOwner(Player player) {
        WolfTable wt = getWolfTable();
        org.bukkit.entity.Wolf wolf = getEntity();
        
        if (wt != null) {
            wt.setOwner(player.getName());
            
            // Set the wolf owner.
            wolf.setOwner(player);
            
            db.update(wt);
        }
    }
    
    /**
     * Get world.
     * 
     * @return
     */
    public World getWorld() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return Bukkit.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
    
    /**
     * Set world.
     * 
     * @param world
     */
    public void setWorld(World world) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setWorld(world.getName());
            
            db.update(wt);
        }
    }
    
    /**
     * Update world to wolf's current world.
     */
    public void updateWorld() {
    	WolfTable wt = getWolfTable();
    	World world = getEntity().getWorld();
    	
    	if (wt != null && world.getName() != wt.getWorld()) {
            wt.setWorld(world.getName());
            
            db.update(wt);
        }
    }
    
    /**
     * Check if wolf has inventory.
     * 
     * @return
     */
    public boolean hasInventory() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
        	return wt.isInventory();
        }
        
        return false;
    }
    
    public boolean hasLoadedInventory() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            if (wt.isInventory() && WolfInventoryManager.hasWolfInventory(uniqueId)) {
            	return true;
            }
        }
        
        return false;
    }
    
    /**
     * Set inventory.
     * 
     * @param inventory
     */
    public void setInventory(boolean inventory) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setInventory(inventory);
            
            db.update(wt);
        }
    }
    
    /**
     * Add inventory.
     */
    public void addInventory() {
    	if (!hasInventory() || !hasLoadedInventory()) {
    		WolfInventoryManager.addWolfInventory(uniqueId, getName() + "'s inventory");
    		setInventory(true);
    	}
    }

    /**
     * Remove inventory.
     */
    public void removeInventory() {
    	if (hasLoadedInventory()) {
    		WolfInventoryManager.removeWolfInventory(uniqueId);
    	}
    	
        setInventory(false);
    }
    
    /**
     * Get inventory.
     * 
     * @return
     */
    public WolfInventory getInventory() {
    	if (hasLoadedInventory()) {
    		return WolfInventoryManager.getWolfInventory(uniqueId);
    	}
    	
    	return null;
    }
    
    /**
     * Drop the inventory contents.
     */
    public void dropInventory() {
        if (hasLoadedInventory()) {
            WolfInventory wi = getInventory();
            World world = getWorld();
            Location location = getEntity().getLocation();
            
            for (ItemStack item : wi.getBukkitContents()) {
                if (item != null && item.getType() != Material.AIR && item.getAmount() > 0 && item.getDurability() > -1) {
                    world.dropItem(location, item);
                }
            }
        }
    }

    /**
     * Get the wolf entity.
     * 
     * @return
     */
    public org.bukkit.entity.Wolf getEntity() {
        for (Entity entity : getWorld().getEntities()) {
            if (entity instanceof org.bukkit.entity.Wolf) {
                if (uniqueId.equals(entity.getUniqueId()))  {
                    return (org.bukkit.entity.Wolf) entity;
                }
            }
        }
        
        return null;
    }
}
