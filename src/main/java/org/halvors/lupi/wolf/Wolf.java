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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkitcontrib.BukkitContrib;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.event.EventFactory;
import org.halvors.lupi.event.wolf.inventory.LupiWolfDropItemEvent;
import org.halvors.lupi.util.WolfUtil;
import org.halvors.lupi.wolf.inventory.WolfInventory;
import org.halvors.lupi.wolf.inventory.WolfInventoryManager;

/**
 * Represents a wolf.
 * 
 * @author halvors
 */
public class Wolf {    
    private final WolfInventoryManager wolfInventoryManager = WolfInventoryManager.getInstance();
    
    private UUID uniqueId;
    
    public Wolf(UUID uniqueId) {
        setUniqueId(uniqueId);
        setTitle(getName());
    }
    
    /**
     * Get WolfTable.
     * 
     * @return
     */
    public WolfTable getWolfTable() {
        return Lupi.getDB().find(WolfTable.class).where()
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
     * Set uniqueId.
     * 
     * @param uniqueId
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    /**
     * Get name.
     * 
     * @return the wolf's name
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
            
            Lupi.getDB().update(wt);
            
            if (hasLoadedInventory()) {
                getInventory().setName(name + "'s inventory");
            }
            
            setTitle(name);
        }
    }
    
    /**
     * Get owner.
     * 
     * @return the Player that is the owner.
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
     * @param player
     */
    public void setOwner(Player player) {
        WolfTable wt = getWolfTable();
        org.bukkit.entity.Wolf wolf = getEntity();
        
        if (wt != null) {
            wt.setOwner(player.getName());
            
            // Set the wolf owner.
            wolf.setOwner(player);
            
            Lupi.getDB().update(wt);
        }
    }
    
    /**
     * Get world.
     * 
     * @return the World
     */
    public World getWorld() {
    	WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return Bukkit.getServer().getWorld(UUID.fromString(wt.getWorld()));
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
            wt.setWorld(world.getUID().toString());
        }
    }
    
    /**
     * Update world in database with the entity's current world.
     */
    public void updateWorld() {
    	World world = getEntity().getWorld();
    	
    	if (!world.equals(getWorld())) {
    		setWorld(world);
    	}
    }

    /**
     * Check if wolf has inventory.
     * 
     * @return true if wolf has inventory.
     */
    public boolean hasInventory() {
        WolfTable wt = getWolfTable();
        
        if (wt != null) {
            return wt.isInventory();
        }
        
        return false;
    }
    
    /**
     * Check if wolf has loaded inventory.
     * 
     * @return true if has inventory and it's loaded.
     */
    public boolean hasLoadedInventory() {
        return hasInventory() && wolfInventoryManager.hasWolfInventory(uniqueId);
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
            
            Lupi.getDB().update(wt);
        }
    }
    
    /**
     * Add inventory.
     */
    public void addInventory() {
        if (!hasInventory()) {
            wolfInventoryManager.addWolfInventory(uniqueId, getName() + "'s inventory");
            setInventory(true);
        }
    }

    /**
     * Remove inventory.
     */
    public void removeInventory() {
        if (hasInventory()) {
            if (hasLoadedInventory()) {
                dropInventory();
            }
            
            wolfInventoryManager.removeWolfInventory(uniqueId);
        }
        
        setInventory(false);
    }
    
    /**
     * Get inventory.
     * 
     * @return the WolfInventory
     */
    public WolfInventory getInventory() {
        if (hasLoadedInventory()) {
            return wolfInventoryManager.getWolfInventory(uniqueId);
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
                    LupiWolfDropItemEvent event = EventFactory.callLupiWolfDropItemEvent(this, getInventory());
                    
                    if (!event.isCancelled()) {
                        world.dropItem(location, item);
                    }
                }
            }
        }
    }
    
    public boolean hasArmor() {
        if (findArmor() != null) {
            return true;
        }
        
        return false;
    }
    
    public ItemStack findArmor() {
        WolfInventory wi = getInventory();
        
        for (ItemStack itemStack : wi.getBukkitContents()) {
            Material type = itemStack.getType();
            
            if (type.equals(Material.DIAMOND_CHESTPLATE)) {
                return itemStack;
            }
        }
        
        return null;
    }
    
    /**
     * Set wolf's overhead title.
     * 
     * @param name
     */
    public void setTitle(String name) {
        if (Lupi.hasBukkitContrib()) {
            BukkitContrib.getAppearanceManager().setGlobalTitle(getEntity(), name);
        }
    }
    
    /**
     * Get the wolf entity.
     * 
     * @return the Wolf
     */
    public org.bukkit.entity.Wolf getEntity() {
        return WolfUtil.getBukkitWolf(uniqueId);
    }
}
