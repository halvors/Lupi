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

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.util.ConfigurationManager;
import org.halvors.lupi.util.WorldConfiguration;
import org.halvors.lupi.wolf.WolfManager;

/**
 * Handle events for all World related events.
 * 
 * @author speeddemon92
 */
public class LupiWorldListener extends WorldListener {
//    private final Lupi plugin;
	
	private final ConfigurationManager configManager;
    
    public LupiWorldListener(Lupi plugin) {
//        this.plugin = plugin;
    	this.configManager = plugin.getConfigurationManager();
    }
    
    /*
    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
    	Chunk chunk = event.getChunk();
    	
    	// Add tamed wolves that not already exists in database.
    	for (Entity entity : chunk.getEntities()) {
    		if (entity instanceof Wolf) {
    			Wolf wolf = (Wolf) entity;
    			
    			if (wolf.isTamed() && !WolfManager.hasWolf(wolf)) {
    				WolfManager.addWolf(wolf);
    			}
    		}
    	}
    }
    */
    
    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {
    	if (!event.isCancelled()) {
    		Chunk chunk = event.getChunk();
    		World world = event.getWorld();
    		WorldConfiguration worldConfig = configManager.get(world);
    	
    		// Prevent chunk with a tamed wolf in from unload here.
    		for (Entity entity : chunk.getEntities()) {
    			if (entity instanceof Wolf) {
    				Wolf wolf = (Wolf) entity;
    			
    				if (wolf.isTamed() && WolfManager.hasWolf(wolf)) {
    					if (worldConfig.wolfKeepChunksLoaded) {
    						event.setCancelled(true);
    						return;
    					}
    				}
    			}
    		}
    	}
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
