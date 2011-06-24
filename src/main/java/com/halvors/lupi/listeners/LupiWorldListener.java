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

package com.halvors.lupi.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.halvors.lupi.Lupi;
import com.halvors.lupi.wolf.WolfManager;

/**
 * Handle events for all World related events.
 * 
 * @author speeddemon92
 */
public class LupiWorldListener extends WorldListener {
//    private Lupi plugin;
    
    public LupiWorldListener(Lupi plugin) {
//        this.plugin = plugin;
    }
    
    @Override
    public void onWorldLoad(WorldLoadEvent event) {
    	World world = event.getWorld();
    	
        // Load wolves from database.
		WolfManager.load(world);
		
		// Add tamed wolves that not already exists in database.
    	for (Entity entity : world.getEntities()) {
    		if (entity instanceof Wolf) {
    			Wolf wolf = (Wolf) entity;
    			
    			if (wolf.isTamed()) {
                	if (!WolfManager.hasWolf(wolf)) {
                        WolfManager.addWolf(wolf);
                    }
                }
    		}
    	}
    }
    
    @Override
    public void onWorldUnload(WorldUnloadEvent event) {
    	if (!event.isCancelled()) {
    		World world = event.getWorld();
    		
            // Unload wolves from database.
    		WolfManager.unload(world);
    	}
    }
}
