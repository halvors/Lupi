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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.avaje.ebean.EbeanServer;
import com.halvors.lupi.Lupi;
import com.halvors.lupi.wolf.WolfManager;

/**
 * Handle WolfInventory's.
 * 
 * @author halvors
 */
public class WolfInventoryManager {
    private final static HashMap<UUID, WolfInventory> wolfInventorys = new HashMap<UUID, WolfInventory>();
    private final static EbeanServer db = Lupi.getDb();
    
    /**
     * Get a WolfInventoryTable.
     * 
     * @param uniqueId
     * @return
     */
    public static WolfInventoryTable getWolfInventoryTable(UUID uniqueId) {
        return db.find(WolfInventoryTable.class).where()
            .ieq("uniqueId", uniqueId.toString()).findUnique();
    }
    
    /**
     * Get all WolfInventoryTable's.
     * 
     * @return
     */
    public static List<WolfInventoryTable> getWolfInventoryTables() {
        return db.find(WolfInventoryTable.class).where().findList();
    }
    
    /**
     * Load a WolfInventory.
     * 
     * @param uniqueId
     * @return
     */
    public static boolean loadWolfInventory(UUID uniqueId) {
    	if (!hasWolfInventory(uniqueId)) {
	    	WolfInventoryTable wit = getWolfInventoryTable(uniqueId);
	    	
	    	// Load the WolfInventory from database.
	        WolfInventory wi = new WolfInventory(uniqueId, WolfManager.getWolf(uniqueId).getName());
	        wi.fillFromDBTable(wit.getChestRows());
	        
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
    public static boolean unloadWolfInventory(UUID uniqueId) {
    	if (hasWolfInventory(uniqueId)) {
    		WolfInventory wi = getWolfInventory(uniqueId);
    		
    		// Save the WolfInventory to database.
    		WolfInventoryTable wit = getWolfInventoryTable(uniqueId);
    		wit.setChestRows(wi.prepareTableForDB());
    		
    		db.update(wit);
    		
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
    public static boolean addWolfInventory(UUID uniqueId, String name) {
    	if (!hasWolfInventory(uniqueId)) {
	    	// Create the WolfInventoryTable.
	    	WolfInventoryTable wit = new WolfInventoryTable();
	    	wit.setUniqueId(uniqueId.toString());
	    	
	    	// Save the WolfInventoryTable to database.
	        db.save(wit);
	        
	        //Create the WolfInventory.
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
    public static boolean removeWolfInventory(UUID uniqueId) {
        if (hasWolfInventory(uniqueId)) {
        	db.delete(getWolfInventoryTable(uniqueId));
        	
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
    public static boolean hasWolfInventory(UUID uniqueId) {
        return wolfInventorys.containsKey(uniqueId);
    }
    
    /**
     * Get wolf's inventory.
     * 
     * @param uniqueId
     * @return
     */
    public static WolfInventory getWolfInventory(UUID uniqueId) {
    	if (!hasWolfInventory(uniqueId)) {
    		addWolfInventory(uniqueId, WolfManager.getWolf(uniqueId).getName());
    	}
    	
        return wolfInventorys.get(uniqueId);
    }
    
    /**
     * Get all WolfInventory's.
     * 
     * @return
     */
    public static List<WolfInventory> getWolfInventorys() {
        return new ArrayList<WolfInventory>(wolfInventorys.values());
    }
}
