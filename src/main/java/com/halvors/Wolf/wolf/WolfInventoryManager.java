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

import java.util.HashMap;
import java.util.List;

import org.bukkit.World;

import com.halvors.Wolf.Wolf;

/**
 * Handle WolfInventory's
 * 
 * @author halvors
 */
public class WolfInventoryManager {
	private final Wolf plugin;
	
    private final HashMap<Integer, WolfInventory> wolfInventorys = new HashMap<Integer, WolfInventory>();
    
    public WolfInventoryManager(final Wolf plugin) {
    	this.plugin = plugin;
    }
    
    public void load(final World world) {
    	wolfInventorys.clear();
        
        // TODO: Load WolfInventory here.
    	
    	List<WolfInventoryTable> wits = plugin.getDatabase().find(WolfInventoryTable.class).where().findList();
    	
    	for (WolfInventoryTable wit : wits) {
    		addWolfInventory(wit.getId(), wit.getInventory());
    	} 
    }
    
    public void save(final World world) {
        // TODO: Save WolfInventory here.
    	
    	for (WolfInventory wi : wolfInventorys.values()) {
    		WolfInventoryTable wit = new WolfInventoryTable();
    		wit.setId(1);
    		wit.setInventory(wi);
    		
    		plugin.getDatabase().save(wit);
    	}
    }
    
    /**
     * Add a WolfInventory
     * 
     * @param id
     * @param wi
     */
    public void addWolfInventory(final int id, final WolfInventory wi) {
        if (!wolfInventorys.containsKey(id)) {
            wolfInventorys.put(id, wi);
        }
    }
    
    /**
     * Add WolfInventory
     * 
     * @param id
     */
    public void addWolfInventory(final int id) {
    	addWolfInventory(id, new WolfInventory());
    }
    
    /**
     * Remove a WolfInventory
     * 
     * @param id
     */
    public void removeWolfInventory(final int id) {
        if (wolfInventorys.containsKey(id)) {
            wolfInventorys.remove(id);
        }
    }
    
    /**
     * Check if wolf has inventory by id
     * 
     * @param id
     * @return
     */
    public boolean hasWolfInventory(final int id) {
        return wolfInventorys.containsKey(id);
    }
    
    /**
     * Get wolf's inventory by id
     * 
     * @param id
     * @return
     */
    public WolfInventory getWolfInventory(final int id) {
        return wolfInventorys.get(id);
    }
}
