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

package org.halvors.lupi.util;

import net.minecraft.server.EntityPlayer;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class InventoryUtil {
	/**
     * Open a virtual inventory.
     * 
     * @param player
     * @param inventory
     */
    public static void openInventory(Player player, WolfInventory inventory) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.a(inventory);
    }
    
    /**
     * Convert to ItemStack.
     * 
     * @param items
     * @return
     */
    public static ItemStack toBukkitItemStack(net.minecraft.server.ItemStack items) {
        return new CraftItemStack(items);
    }
    
    /**
     * Convert from ItemStack.
     * 
     * @param items
     * @return
     */
    public static net.minecraft.server.ItemStack fromBukkitItemStack(ItemStack items) {
        return new net.minecraft.server.ItemStack(items.getTypeId(), items.getAmount(), items.getDurability());
    }
}
