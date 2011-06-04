package com.halvors.Wolf.wolf;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.TileEntityChest;

/**
 * Represents a WolfInventory
 * 
 * @author halvors
 */
public class WolfInventory extends TileEntityChest {
    public WolfInventory() {
        
    }
    
    @Override
    public boolean a_(EntityHuman entityhuman) {
        return true;
    }
}
