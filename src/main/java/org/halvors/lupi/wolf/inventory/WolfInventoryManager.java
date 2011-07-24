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

package org.halvors.lupi.wolf.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.halvors.lupi.Lupi;
import org.halvors.lupi.wolf.WolfManager;

/**
 * Handle WolfInventory's.
 * 
 * @author halvors
 */
public class WolfInventoryManager {
    private final Lupi plugin;
//    private final EbeanServer database;
    private final WolfManager wolfManager;
    private final HashMap<UUID, WolfInventory> wolfInventorys;
    
    private static WolfInventoryManager instance;
    
    public WolfInventoryManager(Lupi plugin) {
        this.plugin = plugin;
//        this.database = plugin.getDatabase();
        this.wolfManager = plugin.getWolfManager();
        this.wolfInventorys = new HashMap<UUID, WolfInventory>();
        
        WolfInventoryManager.instance = this;
    }
    
    public static WolfInventoryManager getInstance() {
        return instance;
    }
    
    /**
     * Get a WolfInventoryTable.
     * 
     * @param uniqueId
     * @return
     */
    public WolfInventoryTable getWolfInventoryTable(UUID uniqueId) {
        return plugin.getDatabase().find(WolfInventoryTable.class).where()
            .ieq("uniqueId", uniqueId.toString()).findUnique();
    }
    
    /**
     * Get all WolfInventoryTable's.
     * 
     * @return
     */
    public List<WolfInventoryTable> getWolfInventoryTables() {
        return plugin.getDatabase().find(WolfInventoryTable.class).where().findList();
    }
    
    /**
     * Load a WolfInventory.
     * 
     * @param uniqueId
     * @return
     */
    public boolean loadWolfInventory(UUID uniqueId) {
        if (!hasWolfInventory(uniqueId)) {
            WolfInventoryTable wit = getWolfInventoryTable(uniqueId);
            
            // Load the WolfInventory from plugin.getDatabase().
            WolfInventory wi = new WolfInventory(uniqueId, wolfManager.getWolf(uniqueId).getName());
            
            if (wit != null) {
                wi.fillFromDBTable(wit.getChestRows());
            }
            
            wolfInventorys.put(uniqueId, wi);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Unload a WolfInventoryTable.
     * 
     * @param uniqueId
     * @return
     */
    public boolean unloadWolfInventory(UUID uniqueId) {
        if (hasWolfInventory(uniqueId)) {
            WolfInventory wi = getWolfInventory(uniqueId);
            
            // Save the WolfInventory to plugin.getDatabase().
            WolfInventoryTable wit = getWolfInventoryTable(uniqueId);
            
            if (wit != null) {
                wit.setChestRows(wi.prepareTableForDB());
            }
            
            plugin.getDatabase().update(wit);
            
            wolfInventorys.remove(uniqueId);
            
            return true;
        }
        
        return false;
    }
  
    /**
     * Add a WolfInventory.
     * 
     * @param uniqueId
     * @return
     */
    public boolean addWolfInventory(UUID uniqueId, String name) {
        if (!hasWolfInventory(uniqueId)) {
            // Create the WolfInventoryTable.
            WolfInventoryTable wit = new WolfInventoryTable();
            wit.setUniqueId(uniqueId.toString());
            
            // Save the WolfInventoryTable to database.
            plugin.getDatabase().save(wit);
            
            // Create the WolfInventory.
            WolfInventory wi = new WolfInventory(uniqueId, name);
            
            wolfInventorys.put(uniqueId, wi);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a WolfInventory.
     * 
     * @param uniqueId
     * @return
     */
    public boolean removeWolfInventory(UUID uniqueId) {
        if (hasWolfInventory(uniqueId)) {
            plugin.getDatabase().delete(getWolfInventoryTable(uniqueId));
            
            wolfInventorys.remove(uniqueId);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if wolf has inventory.
     * 
     * @param uniqueId
     * @return
     */
    public boolean hasWolfInventory(UUID uniqueId) {
        return wolfInventorys.containsKey(uniqueId);
    }
    
    /**
     * Get wolf's inventory.
     * 
     * @param uniqueId
     * @return
     */
    public WolfInventory getWolfInventory(UUID uniqueId) {
        if (!hasWolfInventory(uniqueId)) {
            addWolfInventory(uniqueId, wolfManager.getWolf(uniqueId).getName());
        }
        
        return wolfInventorys.get(uniqueId);
    }
    
    /**
     * Get all WolfInventory's.
     * 
     * @return
     */
    public List<WolfInventory> getWolfInventorys() {
        return new ArrayList<WolfInventory>(wolfInventorys.values());
    }
}
