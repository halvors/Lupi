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

package org.halvors.lupi.wolf;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

/**
 * Handle selected wolves.
 * 
 * @author halvors
 */
public class SelectedWolfManager {
    private final static HashMap<String, Wolf> selectedWolves = new HashMap<String, Wolf>();
    
    /**
     * Add a selected wolf
     * 
     * @param player
     * @param wolf
     */
    public static void addSelectedWolf(Player player, Wolf wolf) {
        if (wolf.isTamed()) {
            String name = player.getName();
            
            if (selectedWolves.containsKey(name)) {
                selectedWolves.remove(name);
            }
            
            selectedWolves.put(name, wolf);
        }
    }
    
    /**
     * Remove a selected wolf
     * 
     * @param player
     */
    public static void removeSelectedWolf(Player player) {
        String name = player.getName();
        
        if (selectedWolves.containsKey(name)) {
            selectedWolves.remove(name);
        }
    }
    
    /**
     * Get a selected wolf by owner
     * 
     * @param player
     * @return Wolf
     */
    public static Wolf getSelectedWolf(Player player) {
        return selectedWolves.get(player.getName());
    }
    
    /**
     * Check if Player has a selected wolf
     * 
     * @param owner
     * @return Boolean
     */
    public static boolean hasSelectedWolf(Player player) {
        return selectedWolves.containsKey(player.getName());
    }
}
