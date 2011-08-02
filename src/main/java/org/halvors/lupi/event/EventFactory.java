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

package org.halvors.lupi.event;

import org.bukkit.Bukkit;
import org.halvors.lupi.event.wolf.LupiWolfAddEvent;
import org.halvors.lupi.event.wolf.LupiWolfLoadEvent;
import org.halvors.lupi.event.wolf.LupiWolfRemoveEvent;
import org.halvors.lupi.event.wolf.LupiWolfUnloadEvent;
import org.halvors.lupi.event.wolf.inventory.LupiWolfDropItemEvent;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class EventFactory {
	
	/**
	 * LupiServerTickEvent
	 * 
	 * @return
	 */
    public static LupiServerTickEvent callLupiServerTickEvent() {
        LupiServerTickEvent event = new LupiServerTickEvent();
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        return event;
    }
    
    /**
     * LupiWolfLoadEvent
     * 
     * @param wolf
     * @return
     */
    public static LupiWolfLoadEvent callLupiWolfLoadEvent(Wolf wolf) {
    	LupiWolfLoadEvent event = new LupiWolfLoadEvent(wolf);
    	Bukkit.getServer().getPluginManager().callEvent(event);
    	
    	return event;
    }
    
    /**
     * LupiWolfUnloadEvent
     * 
     * @param wolf
     * @return
     */
    public static LupiWolfUnloadEvent callLupiWolfUnloadEvent(Wolf wolf) {
    	LupiWolfUnloadEvent event = new LupiWolfUnloadEvent(wolf);
    	Bukkit.getServer().getPluginManager().callEvent(event);
    	
    	return event;
    }
    
    /**
     * LupiWolfAddEvent
     * 
     * @param wolf
     * @return
     */
    public static LupiWolfAddEvent callLupiWolfAddEvent(Wolf wolf) {
    	LupiWolfAddEvent event = new LupiWolfAddEvent(wolf);
    	Bukkit.getServer().getPluginManager().callEvent(event);
    	
    	return event;
    }
    
    /**
     * LupiWolfRemoveEvent
     * 
     * @param wolf
     * @return
     */
    public static LupiWolfRemoveEvent callLupiWolfRemoveEvent(Wolf wolf) {
    	LupiWolfRemoveEvent event = new LupiWolfRemoveEvent(wolf);
    	Bukkit.getServer().getPluginManager().callEvent(event);
    	
    	return event;
    }
    
    /**
     * LupiWolfDropItemEvent
     * 
     * @param wolf
     * @param wi
     * @return
     */
    public static LupiWolfDropItemEvent callLupiWolfDropItemEvent(Wolf wolf, WolfInventory wi) {
        LupiWolfDropItemEvent event = new LupiWolfDropItemEvent(wolf, wi);
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        return event;
    }
    
    /**
     * LupiWolfPickupEvent
     * 
     * @param wolf
     * @param wi
     * @return
     */
    public static LupiWolfPickupItemEvent callLupiWolfPickupItemEvent(Wolf wolf, WolfInventory wi) {
        LupiWolfPickupItemEvent event = new LupiWolfPickupItemEvent(wolf, wi);
        Bukkit.getServer().getPluginManager().callEvent(event);
        
        return event;
    }
}
