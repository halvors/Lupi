package com.halvors.Wolf.wolf;

import java.util.UUID;

import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Represents a wolf
 * 
 * @author halvors
 */
public class Wolf {
	private final com.halvors.Wolf.Wolf plugin;
	
	private final WolfManager wolfManager;
	
	private UUID uniqueId;
	
	public Wolf(final com.halvors.Wolf.Wolf plugin) {
		this.plugin = plugin;
		this.wolfManager = plugin.getWolfManager();
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
	 * Set uniqueId
	 * 
	 * @param uniqueId
	 */
	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
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
            for (Player player : plugin.getServer().getOnlinePlayers()) {
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
            return plugin.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
}
