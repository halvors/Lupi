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

package org.halvors.lupi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.halvors.lupi.commands.LupiCommandExecutor;
import org.halvors.lupi.listeners.LupiEntityListener;
import org.halvors.lupi.listeners.LupiPlayerListener;
import org.halvors.lupi.listeners.LupiWorldListener;
import org.halvors.lupi.util.ConfigurationManager;
import org.halvors.lupi.wolf.SelectedWolfManager;
import org.halvors.lupi.wolf.WolfManager;
import org.halvors.lupi.wolf.WolfTable;
import org.halvors.lupi.wolf.inventory.WolfInventoryManager;
import org.halvors.lupi.wolf.inventory.WolfInventoryTable;

import com.avaje.ebean.EbeanServer;

public class Lupi extends JavaPlugin {
    private final Logger logger = Logger.getLogger("Minecraft");
    
    private PluginManager pm;
    private PluginDescriptionFile desc;
    
    private final ConfigurationManager configuration;
    private final LupiEntityListener entityListener;
    private final LupiPlayerListener playerListener;
    private final LupiWorldListener worldListener;
    
    private static Lupi instance;
    private static EbeanServer db;
    
    private static final WolfManager wolfManager = new WolfManager();
    private static final WolfInventoryManager wolfInventoryManager = new WolfInventoryManager();
    private static final SelectedWolfManager selectedWolfManager = new SelectedWolfManager();
    
    /**
     * Lupi is a wolf plugin for Bukkit.
     */
    public Lupi() {
    	Lupi.instance = this;
        this.configuration = new ConfigurationManager(this);
        this.entityListener = new LupiEntityListener(this);
        this.playerListener = new LupiPlayerListener(this);
        this.worldListener = new LupiWorldListener(this);
    }
    
    @Override
    public void onEnable() {
        pm = getServer().getPluginManager();
        desc = getDescription();
        
        // Load configuration.
        configuration.load();
        
        // Setup database.
        setupDatabase();
        db = getDatabase();
        
        // Load wolves to WolfManager.
		WolfManager.load();
        
        // Register our events.
        pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TAME, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this);

        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);
        
//        pm.registerEvent(Event.Type.CHUNK_LOAD, worldListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.CHUNK_UNLOAD, worldListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Event.Priority.Normal, this);
        
        // Register our commands.
        getCommand("wolf").setExecutor(new LupiCommandExecutor(this));
        
        log(Level.INFO, "version " + getVersion() + " is enabled!");
    }
    
    @Override
    public void onDisable() {
        // Save configuration.
    	configuration.unload();
        
        // Unload wolves from WolfManager.
        WolfManager.unload();
        
        log(Level.INFO, "version " + getVersion() + " is disabled!");
    }
    
    /**
     * Setup the database.
     */
    private void setupDatabase() {
        try {
            getDatabase().find(WolfTable.class).findRowCount();
            getDatabase().find(WolfInventoryTable.class).findRowCount();
        } catch (PersistenceException ex) {
            log(Level.INFO, "Installing database for " + getName() + " due to first time usage");
            installDDL();
        }
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(WolfTable.class);
        list.add(WolfInventoryTable.class);
        
        return list;
    }
    
    /**
     * Sends a console message.
     * 
     * @param level
     * @param msg
     */
    public void log(Level level, String msg) {
        logger.log(level, "[" + getName() + "] " + msg);
    }
    
    /**
     * Get the name.
     * 
     * @return the plugin name
     */
    public String getName() {
        return desc.getName();
    }
    
    /**
     * Get the version.
     * 
     * @return the plugin version
     */
    public String getVersion() {
        return desc.getVersion();
    }
    
    /**
     * Get the Lupi instance.
     * 
     * @return the Lupi instance
     */
    public static Lupi getInstance() {
    	return instance;
    }
    
    /**
     * Get the database.
     * 
     * @return the EbeanServer
     */
    public static EbeanServer getDB() {
    	return db;
    }
    
    /**
     * Get the ConfigurationManager.
     * 
     * @return the ConfigurationManager
     */
    public ConfigurationManager getConfigurationManager() {
        return configuration;
    }
    
    /**
     * Get the WolfManager.
     * 
     * @return the WolfManager
     */
    public static WolfManager getWolfManager() {
    	return wolfManager;
    }
    
    /**
     * Get the WolfInventoryManager.
     * 
     * @return the WolfInventoryManager
     */
    public static WolfInventoryManager getWolfInventoryManager() {
    	return wolfInventoryManager;
    }
    
    /**
     * Get the SelectedWolfManager.
     * 
     * @return the SelectedWolfManager
     */
    public static SelectedWolfManager getSelectedWolfManager() {
    	return selectedWolfManager;
    }
}
