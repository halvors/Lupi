package com.halvors.Wolf.wolf;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents a wolf
 * 
 * @author halvors
 */
public class Wolf {
	private final WolfManager wolfManager;
	
	private UUID uniqueId;
	
	public Wolf(final com.halvors.Wolf.Wolf plugin, UUID uniqueId) {
		this.wolfManager = plugin.getWolfManager();
		this.uniqueId = uniqueId;
	}
	
	/**
	 * Get id
	 * 
	 * @return
	 */
	public int getId() {
		WolfTable wt = wolfManager.getWolfTable(uniqueId);
		
		if (wt != null) {
			return wt.getId();
		}
		
		return 0;
	}
	
	/**
	 * Get uniqueId
	 * 
	 * @return UUID
	 */
	public UUID getUniqueId() {
		return uniqueId;
	}
    
    /**
     * Get name
     * 
     * @return String
     */
    public String getName() {
        WolfTable wt = wolfManager.getWolfTable(uniqueId);
        
        if (wt != null) {
            return wt.getName();
        }
        
        return null;
    }
    
    /**
     * Get wolf's owner
     * 
     * @return Player
     */
    public Player getOwner() {
        WolfTable wt = wolfManager.getWolfTable(uniqueId);
        
        if (wt != null) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getName().equalsIgnoreCase(wt.getOwner())) {
                    return player;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Get wolf's world
     * 
     * @return World
     */
    public World getWorld() {
        WolfTable wt = wolfManager.getWolfTable(uniqueId);
        
        if (wt != null) {
            return Bukkit.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
}
