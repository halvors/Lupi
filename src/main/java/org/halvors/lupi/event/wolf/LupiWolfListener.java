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

package org.halvors.lupi.event.wolf;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.halvors.lupi.event.wolf.inventory.LupiWolfDropItemEvent;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;

public class LupiWolfListener extends CustomEventListener implements Listener {
    public LupiWolfListener() {
        
    }
    
    public void onLupiWolfDropItem(LupiWolfDropItemEvent event) {
        
    }
    
    public void onLupiWolfPickupItem(LupiWolfPickupItemEvent event) {
        
    }
    
    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof LupiWolfDropItemEvent) {
            onLupiWolfDropItem((LupiWolfDropItemEvent) event);
        } else if (event instanceof LupiWolfPickupItemEvent) {
            onLupiWolfPickupItem((LupiWolfPickupItemEvent) event);
        }
    }
}
