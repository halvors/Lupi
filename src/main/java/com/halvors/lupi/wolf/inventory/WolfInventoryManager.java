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

package com.halvors.lupi.wolf.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.halvors.lupi.Lupi;

/**
 * Handle WolfInventory's
 * 
 * @author halvors
 */
public class WolfInventoryManager {
    private final static HashMap<UUID, WolfInventory> wolfInventorys = new HashMap<UUID, WolfInventory>();
    
    /**
     * Get a WolfInventoryTable
     * 
     * @param uniqueId
     * @return
     */
    public static WolfInventoryTable getWolfInventoryTable(UUID uniqueId) {
        return Lupi.getDb().find(WolfInventoryTable.class).where()
            .ieq("uniqueId", uniqueId.toString()).findUnique();
    }
    
    /**
     * Get all WolfInventoryTables
     * 
     * @return
     */
    public static List<WolfInventoryTable> getWolfInventoryTables() {
        return Lupi.getDb().find(WolfInventoryTable.class).where().findList();
    }
    
    /**
     * Load inventorys from database
     */
    public static void load() {
        List<WolfInventoryTable> wits = getWolfInventoryTables();
    
        for (WolfInventoryTable wit : wits) {
        	loadWolfInventory(UUID.fromString(wit.getUniqueId()));
        } 
    }
    
    /**
     * Unload inventorys from database
     */
    public static void unload() {
        for (WolfInventory wi : wolfInventorys.values()) {
            WolfInventoryTable wit = getWolfInventoryTable(wi.getUniqueId());
            
            if (wit != null) {
                String[] rows = wi.prepareTableForDB();
                wit.setChestRows(rows);
                
                Lupi.getDb().update(wit);
            } else {
                wit = new WolfInventoryTable();
                wit.setUniqueId(wi.getUniqueId().toString());
                String[] rows = wi.prepareTableForDB();
                wit.setChestRows(rows);
                
                Lupi.getDb().save(wit);
            }
        }
    }
    
    public static void loadWolfInventory(UUID unqiueId) {
    	WolfInventoryTable wit = getWolfInventoryTable(unqiueId);
    	
        WolfInventory wi = new WolfInventory(unqiueId);
        wi.fillFromDBTable(wit.getChestRows());
        
        addWolfInventory(unqiueId, wi);
    }
    
    public static void unloadWolfInventory(UUID uniqueId) {
    	WolfInventoryTable wit = getWolfInventoryTable(uniqueId);
    	WolfInventory wi = getWolfInventory(uniqueId);
    	
    	if (wit != null) {
            String[] rows = wi.prepareTableForDB();
            wit.setChestRows(rows);
            
            Lupi.getDb().update(wit);
        } else {
            wit = new WolfInventoryTable();
            wit.setUniqueId(uniqueId.toString());
            String[] rows = wi.prepareTableForDB();
            wit.setChestRows(rows);
            
            Lupi.getDb().save(wit);
        }
    	
    	removeWolfInventory(uniqueId);
    }
    
    public static void addWolfInventory(UUID uniqueId, String name) {    	
    	// Create a new WolfInventoryTable.
    	WolfInventoryTable wit = new WolfInventoryTable();
    	wit.setUniqueId(uniqueId.toString());
    	
    	// TODO: Maybe add rows here.
    	
    	// Save the WolfInventory to database.
    	Lupi.getDb().save(wit);
    	
        if (wolfInventorys.containsKey(uniqueId)) {
            wolfInventorys.remove(uniqueId);
        }
        
        wolfInventorys.put(uniqueId, new WolfInventory(uniqueId, name));
    }
    
    /**
     * Remove a WolfInventory
     * 
     * @param uniqueId
     */
    public static void removeWolfInventory(UUID uniqueId) {
        if (wolfInventorys.containsKey(uniqueId)) {
        	Lupi.getDb().delete(getWolfInventoryTable(uniqueId));
        	
            wolfInventorys.remove(uniqueId);
        }
    }
    
    /**
     * Check if wolf has inventory
     * 
     * @param uniqueId
     * @return
     */
    public static boolean hasWolfInventory(UUID uniqueId) {
        return wolfInventorys.containsKey(uniqueId);
    }
    
    /**
     * Get wolf's inventory
     * 
     * @param uniqueId
     * @return
     */
    public static WolfInventory getWolfInventory(UUID uniqueId) {
        return wolfInventorys.get(uniqueId);
    }
}
