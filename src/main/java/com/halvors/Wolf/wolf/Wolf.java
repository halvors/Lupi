package com.halvors.Wolf.wolf;

import org.bukkit.Location;
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
	
	private int id;
	
	public Wolf(final com.halvors.Wolf.Wolf plugin) {
		this.plugin = plugin;
		this.wolfManager = plugin.getWolfManager();
	}
	
	/**
	 * Get id
	 * 
	 * @return Integer
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
     * Get entityId
     * 
     * @return Integer
     */
    public int getEntityId() {
        WolfTable wt = wolfManager.getWolfTable(id);
        
        if (wt != null) {
            return wt.getEntityId();
        }
        
        return 0;
    }
    
    /**
     * Get name
     * 
     * @return String
     */
    public String getName() {
        WolfTable wt = wolfManager.getWolfTable(id);
        
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
        WolfTable wt = wolfManager.getWolfTable(id);
        
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
     * Get location
     * 
     * @return Location
     */
    public Location getLocation() {
        WolfTable wt = wolfManager.getWolfTable(id);
        
        if (wt != null) {
            return wt.getLocation();
        }
        
        return null;
    }
    
    /**
     * Get wolf's world
     * 
     * @return World
     */
    public World getWorld() {
        WolfTable wt = wolfManager.getWolfTable(id);
        
        if (wt != null) {
            return plugin.getServer().getWorld(wt.getWorld());
        }
        
        return null;
    }
}
