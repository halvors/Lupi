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

package org.halvors.lupi.event.wolf.inventory;

import org.halvors.lupi.event.wolf.LupiWolfEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class LupiWolfInventoryEvent extends LupiWolfEvent {
    private static final long serialVersionUID = 472165692667983345L;

    private WolfInventory inventory;
    
    public LupiWolfInventoryEvent(String name, Wolf wolf, WolfInventory wi) {
        super(name, wolf);
        this.inventory = wi;
    }
    
    public WolfInventory getInventory() {
        return inventory;
    }
}
