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

package com.halvors.lupi.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

import com.halvors.lupi.Lupi;

/**
 * Holds the configuration for individual worlds.
 *
 * @author halvors
 */
public class WorldConfig {
    //private final WolfPlugin plugin;
    
	private final ConfigManager configManager;
	
    private String worldName;
    private File configFile;

    /* Configuration data start */
    public boolean wolfEnable;
    public int wolfItem;
    public boolean wolfFriendly;
    public boolean wolfPvp;
    public int wolfLimit;
    
    public int infoItem;
    
    public boolean inventoryEnable;
    public int inventoryItem;
    /* Configuration data end */

    public WorldConfig(final Lupi plugin, final String worldName) {
        //this.plugin = plugin;
    	this.configManager = plugin.getConfigManager();
        this.worldName = worldName;
        
        File baseFolder = new File(plugin.getDataFolder(), "worlds/");
        configFile = new File(baseFolder, worldName + ".yml");

        configManager.checkConfig(configFile, "config_world.yml");

        load();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + "'");
    }

    /**
     * Load the configuration.
     */
    public void load() {    
        Configuration config = new Configuration(configFile);
        config.load();
        
        wolfEnable = config.getBoolean("wolf.enable", wolfEnable);
        wolfItem = config.getInt("wolf.item", wolfItem);
        wolfFriendly = config.getBoolean("wolf.friendly", wolfFriendly);
        wolfPvp = config.getBoolean("wolf.pvp", wolfPvp);
        wolfLimit = config.getInt("wolf.limit", wolfLimit);
        
        infoItem = config.getInt("info.item", infoItem);
        
        inventoryEnable = config.getBoolean("inventory.enable", inventoryEnable);
        inventoryItem = config.getInt("inventory.item", inventoryItem);
    }

    /**
     * Get world name.
     * 
     * @return worldName
     */
    public String getWorldName() {
        return this.worldName;
    }
}
