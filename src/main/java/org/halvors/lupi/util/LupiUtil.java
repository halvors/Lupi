package org.halvors.lupi.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LupiUtil {
    /**
     * Get the best matching player.
     * 
     * @param name
     * @return the Player
     */
    public static Player getPlayer(String name) {
    	return Bukkit.getServer().matchPlayer(name).get(0);
    }
}
