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

package org.halvors.lupi.listeners;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.wolf.WolfManager;

/**
 * Handle events for all World related events.
 * 
 * @author speeddemon92
 */
public class LupiWorldListener extends WorldListener {
//    private final Lupi plugin;
    
    public LupiWorldListener(Lupi plugin) {
//        this.plugin = plugin;
    }
    
    @Override
    public void onWorldLoad(WorldLoadEvent event) {
    	World world = event.getWorld();
    	
		// Add tamed wolves that not already exists in database.
    	for (Entity entity : world.getLivingEntities()) {
    		if (entity instanceof Wolf) {
    			Wolf wolf = (Wolf) entity;
    			
    			if (wolf.isTamed() && !WolfManager.hasWolf(wolf)) {
    				WolfManager.addWolf(wolf);
                }
    		}
    	}
    }
}
