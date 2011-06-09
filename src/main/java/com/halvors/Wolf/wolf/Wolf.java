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
	
	public Wolf(final com.halvors.Wolf.Wolf plugin, UUID uniqueId) {
		this.plugin = plugin;
		this.wolfManager = plugin.getWolfManager();
		this.uniqueId = uniqueId;
	}
	
	/**
	 * Get id
	 * 
	 * @return int
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
     * Get owner
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
     * Get world
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
    
    /**
     * Check if inventory exists
     * 
     * @return
     */
    public boolean hasInventory() {
    	WolfTable wt = wolfManager.getWolfTable(uniqueId);
    	
    	if (wt != null) {
    		return wt.isInventory();
    	}
    	
    	return false;
    }
}
