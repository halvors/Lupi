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

import org.bukkit.entity.Wolf;

/**
 * Handle selected wolves
 * 
 * @author halvors
 */
public class SelectedWolfManager {
//    private final com.halvors.Wolf.Wolf plugin;
    
    private final HashMap<String, Wolf> selectedWolfs;
    
    public SelectedWolfManager(final com.halvors.Wolf.Wolf plugin) {
//        this.plugin = plugin;
        this.selectedWolfs = new HashMap<String, Wolf>();
    }
    
    /**
     * Add a selected wolf
     * 
     * @param owner
     * @param wolf
     */
    public void addSelectedWolf(final String owner, final Wolf wolf) {
        if (!selectedWolfs.containsKey(owner)) {
            selectedWolfs.put(owner, wolf);
        }
    }
    
    /**
     * Remove a selected wolf
     * 
     * @param owner
     */
    public void removeSelectedWolf(final String owner) {
        if (selectedWolfs.containsKey(owner)) {
            selectedWolfs.remove(owner);
        }
    }
    
    /**
     * Get a selected wolf by owner
     * 
     * @param owner
     * @return Wolf
     */
    public Wolf getSelectedWolf(final String owner) {
        return selectedWolfs.get(owner);
    }
    
    /**
     * Check if Player has a selected wolf
     * 
     * @param owner
     * @return Boolean
     */
    public boolean hasSelectedWolf(final String owner) {
        return selectedWolfs.containsKey(owner);
    }
}