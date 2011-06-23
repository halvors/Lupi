/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
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

package com.halvors.lupi.wolf;

import java.util.List;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.halvors.lupi.Lupi;
import com.halvors.lupi.wolf.inventory.WolfInventory;
import com.halvors.lupi.wolf.inventory.WolfInventoryManager;

/**
 * Represents a wolf
 * 
 * @author halvors
 */
public class Wolf {
    private Lupi plugin;
    private UUID uniqueId;
    
    private final WolfInventoryManager wolfInventoryManager;
    
    public Wolf(Lupi plugin, UUID uniqueId) {
        this.plugin = plugin;
        this.uniqueId = uniqueId;
        this.wolfInventoryManager = plugin.getWolfInventoryManager();
    }
    
    /**
     * Get WolfTable.
     * 
     * @return WolfTable
     */
    public WolfTable getWolfTable() {
        return plugin.getDatabase().find(WolfTable.class).where()
            .ieq("uniqueId", uniqueId.toString()).findUnique();
    }
    
    /**
     * Get id.
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
     * Set id.
     * 
     * @param id
     */
    public void setId(int id) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setId(id);
            
            plugin.getDatabase().update(wt);
        }
    }
    
    /**
     * Get uniqueId.
     * 
     * @return UUID
     */
    public UUID getUniqueId() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return UUID.fromString(wt.getUniqueId());
        }
        
        return null;
    }
    
    /**
     * Get name.
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
     * Set name.
     * 
     * @param name
     */
    public void setName(String name) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setName(name);
            
            plugin.getDatabase().update(wt);
            
            if (hasInventory()) {
                getInventory().setName(name + "'s inventory");
            }
        }
    }
    
    /**
     * Get owner.
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
     * Set owner.
     * 
     * @param owner
     */
    public void setOwner(Player owner) {
        WolfTable wt = getWolfTable();
        org.bukkit.entity.Wolf wolf = getEntity();
        
        if (wt != null) {
            wt.setOwner(owner.getName());
            wolf.setOwner(owner);
            
            plugin.getDatabase().update(wt);
        }
    }
    
    /**
     * Get world.
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
     * Set world.
     * 
     * @param world
     */
    public void setWorld(World world) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setWorld(world.getName());
            
            plugin.getDatabase().update(wt);
        }
    }
    
    /**
     * Check if inventory exists.
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
     * Set inventory.
     * 
     * @param inventory
     */
    public void setInventory(boolean inventory) {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            wt.setInventory(inventory);
            
            plugin.getDatabase().update(wt);
        }
    }
    
    /**
     * Add inventory.
     */
    public void addInventory() {
        setInventory(true);
        wolfInventoryManager.addWolfInventory(uniqueId, getName() + "'s inventory");
    }

    /**
     * Remove inventory.
     */
    public void removeInventory() {
        setInventory(false);
        wolfInventoryManager.removeWolfInventory(uniqueId);
    }
    
    /**
     * Get inventory.
     * 
     * @return WolfInventory
     */
    public WolfInventory getInventory() {
        return wolfInventoryManager.getWolfInventory(uniqueId);
    }
    
    /**
     * Drop inventory contents.
     */
    public void dropInventory() {
    	if (hasInventory()) {
    		WolfInventory wi = getInventory();
    		World world = getWorld();
    		org.bukkit.entity.Wolf wolf = getEntity();
    		
    		for (ItemStack item : wi.getBukkitContents()) {
    			if (item != null && item.getAmount() > 0 && item.getDurability() > -1) {
                    world.dropItem(wolf.getLocation(), item);
                }
    		}
    	}
    }

    /**
     * Get the wolf entity.
     * 
     * @return Wolf
     */
    public org.bukkit.entity.Wolf getEntity() {
        List<Entity> entities = getWorld().getEntities();
        
        for (Entity entity : entities) {
            if (entity instanceof org.bukkit.entity.Wolf) {
                if (uniqueId.equals(entity.getUniqueId()))  {
                    return (org.bukkit.entity.Wolf)entity;
                }
            }
        }
        
        return null;
    }
    
    /*
    public void eat(ItemStack item) {
    	if (item.equals(Material.PORK) || item.equals(Material.GRILLED_PORK)) {
    		org.bukkit.entity.Wolf wolf = getEntity();
    		int health = wolf.getHealth() + 2;
    		
    		if (health < 20) {
    			wolf.setHealth(health);
    		}
    	}
    }
    */
}