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

package com.halvors.Wolf.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;

import com.halvors.Wolf.wolf.WolfInventoryManager;
import com.halvors.Wolf.wolf.WolfManager;
import com.halvors.Wolf.wolf.WolfTable;

/**
 * Handle events for all World related events
 * 
 * @author speeddemon92
 */
public class WolfWorldListener extends WorldListener{
//    private final com.halvors.Wolf.Wolf plugin;
    
    private final WolfManager wolfManager;
    private final WolfInventoryManager wolfInventoryManager;
    
    public WolfWorldListener(final com.halvors.Wolf.Wolf plugin) {
//        this.plugin = plugin;
        this.wolfManager = plugin.getWolfManager();
        this.wolfInventoryManager = plugin.getWolfInventoryManager();
    }
    
    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        
        for (Entity entity : entities) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf)entity;
                
                if (wolf.isTamed()) {
                    WolfTable wt = wolfManager.getWolfTable(wolf.getLocation());
                    
                    if (wt != null) {
                        wt.setEntityId(wolf.getEntityId());
                        wolfManager.updateWolfTable(wt);
                    } else {
                        wolfManager.addWolf(wolf);
                    }
                }
            }
        }
    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {
        Entity[] entities = event.getChunk().getEntities();
        
        for (Entity entity : entities) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf)entity;
                
                if (wolf.isTamed()) {
                    WolfTable wt = wolfManager.getWolfTable(wolf.getEntityId());
                    
                    if (wt != null) {
                        wt.setLocation(wolf.getLocation());
                        wolfManager.updateWolfTable(wt);
                    } else {
                        wolfManager.addWolf(wolf);
                    }
                }
            }
        }
    }
    
    @Override
    public void onWorldLoad(WorldLoadEvent event) {
        wolfInventoryManager.load(event.getWorld());
        
        /*
        List<Entity> entities = event.getWorld().getEntities();
        
        for (Entity entity : entities) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (wolf.isTamed()) {
                    WolfTable wt = wolfManager.getWolfTable(wolf.getLocation());
                    
                    if (wt != null) {
                        wt.setEntityId(wolf.getEntityId());
                        wolfManager.updateWolfTable(wt);
                    }
                }
            }
        }
        */
    }
    
    @Override
    public void onWorldSave(WorldSaveEvent event) {
        wolfInventoryManager.save(event.getWorld());
        
        /*
        List<Entity> entities = event.getWorld().getEntities();
        
        for (Entity entity : entities) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf) entity;
                
                if (wolf.isTamed()) {
                    WolfTable wt = wolfManager.getWolfTable(wolf.getEntityId());
                    
                    if (wt != null) {
                        wt.setLocationX(wolf.getLocation().getBlockX());
                        wt.setLocationY(wolf.getLocation().getBlockY());
                        wt.setLocationZ(wolf.getLocation().getBlockZ());
                        wolfManager.updateWolfTable(wt);
                    }
                }
            }
        }
        */
    }
}