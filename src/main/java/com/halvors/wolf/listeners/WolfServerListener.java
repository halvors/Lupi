package com.halvors.wolf.listeners;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.halvors.wolf.WolfPlugin;
import com.halvors.wolf.wolf.WolfInventoryManager;
import com.halvors.wolf.wolf.WolfManager;

/**
 * Handle events for all Server related events.
 * 
 * @author halvors
 */
public class WolfServerListener extends ServerListener {
//    private final WolfPlugin plugin;
    
    private final WolfManager wolfManager;
    private final WolfInventoryManager wolfInventoryManager;
    
    public WolfServerListener(final WolfPlugin plugin) {
//        this.plugin = plugin;
        this.wolfManager = plugin.getWolfManager();
        this.wolfInventoryManager = plugin.getWolfInventoryManager();
    }
    
    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        // Load wolves from database
        wolfManager.load();
        
        // Load inventorys from database
        wolfInventoryManager.load();
    }
    
    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        // Save wolves to database
        wolfManager.save();
        
        // Save inventorys to database
        wolfInventoryManager.save();
    }
}
