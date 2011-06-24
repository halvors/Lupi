/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
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

package com.halvors.lupi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.PersistenceException;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.avaje.ebean.EbeanServer;
import com.halvors.lupi.commands.LupiCommandExecutor;
import com.halvors.lupi.listeners.LupiEntityListener;
import com.halvors.lupi.listeners.LupiPlayerListener;
import com.halvors.lupi.listeners.LupiWorldListener;
import com.halvors.lupi.util.ConfigManager;
import com.halvors.lupi.wolf.WolfManager;
import com.halvors.lupi.wolf.WolfTable;
import com.halvors.lupi.wolf.inventory.WolfInventoryManager;
import com.halvors.lupi.wolf.inventory.WolfInventoryTable;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Lupi extends JavaPlugin {
    private final Logger log = Logger.getLogger("Minecraft");
    
    private PluginManager pm;
    private PluginDescriptionFile desc;

    private final ConfigManager configManager = new ConfigManager(this);

    private final LupiEntityListener entityListener = new LupiEntityListener(this);
    private final LupiPlayerListener playerListener = new LupiPlayerListener(this);
    private final LupiWorldListener worldListener = new LupiWorldListener(this);
    
    private static EbeanServer database;
    
    public static PermissionHandler Permissions;
    
    public void onEnable() {
        pm = getServer().getPluginManager();
        desc = getDescription();
        
        // Load configuration.
        configManager.load();
        
        // Register our events Type.
        pm.registerEvent(Event.Type.CREATURE_SPAWN, entityListener, Event.Priority.Normal, this);
//        pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TAME, entityListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_TARGET, entityListener, Event.Priority.Normal, this);

        pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, playerListener, Event.Priority.Normal, this);

        pm.registerEvent(Event.Type.WORLD_LOAD, worldListener, Event.Priority.Normal, this);
        pm.registerEvent(Event.Type.WORLD_UNLOAD, worldListener, Event.Priority.Normal, this);
        
        // Register our commands.
        getCommand("wolf").setExecutor(new LupiCommandExecutor(this));
        
        log(Level.INFO, "version " + getVersion() + " is enabled!");
        
        setupPermissions();
        setupDatabase();
        
        database = getDatabase();
    }
    
    public void onDisable() {
        // Save configuration.
        configManager.save();
        
        log(Level.INFO, "version " + getVersion() + " is disabled!");
    }
    
    private void setupPermissions() {
        Plugin permissions = getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions != null) {
                Permissions = ((Permissions) permissions).getHandler();
            } else {
                log(Level.INFO, "Permission system not detected, defaulting to OP");
            }
        }
    }
    
    private void setupDatabase() {
        try {
            getDatabase().find(WolfTable.class).findRowCount();
            getDatabase().find(WolfInventoryTable.class).findRowCount();
        } catch (PersistenceException ex) {
            log(Level.INFO, "Installing database for " + getDescription().getName() + " due to first time usage");
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
    
    public boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
            return Permissions.has(player, node);
        } else {
            return player.isOp();
        }
    }
    
    public void log(Level level, String msg) {
        this.log.log(level, "[" + getName() + "] " + msg);
    }
    
    public String getName() {
        return desc.getName();
    }
    
    public String getVersion() {
        return desc.getVersion();
    }
    
    public static EbeanServer getDb() {
    	return database;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
