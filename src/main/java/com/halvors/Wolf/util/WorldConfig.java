/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
 *
 * This file is part of WolfControl.
 *
 * WolfControl is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WolfControl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WolfControl.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.halvors.Wolf.util;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

import com.halvors.Wolf.Wolf;

/**
 * Holds the configuration for individual worlds.
 *
 * @author halvors
 */
public class WorldConfig {
	private final Wolf plugin;
	
    private String worldName;
    private File configFile;

    /* Configuration data start */
    public int item;
    
    public boolean wolfEnable;
    public boolean wolfRespawn;
    public boolean wolfPeaceful;
    /* Configuration data end */

    /**
     * Construct the object.
     *
     * @param plugin
     * @param worldName
     */
    public WorldConfig(final Wolf plugin, final String worldName) {
    	this.plugin = plugin;
    	this.worldName = worldName;
    	
    	File baseFolder = new File(plugin.getDataFolder(), "worlds/");
        configFile = new File(baseFolder, worldName + ".yml");

        plugin.getConfigManager().checkConfig(configFile, "config_world.yml");

        load();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + "'");
    }

    /**
     * Load the configuration.
     */
    public void load() {	
        Configuration config = new Configuration(configFile);
        config.load();
        
        item = config.getInt("item", item);
        
        wolfEnable = config.getBoolean("wolf.enable", wolfEnable);
        wolfRespawn = config.getBoolean("wolf.respawn", wolfRespawn);
        wolfPeaceful = config.getBoolean("wolf.peaceful", wolfPeaceful);
    }

    /**
     * Get world name.
     * @return worldName
     */
    public String getWorldName() {
        return this.worldName;
    }
}