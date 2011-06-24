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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.util.config.Configuration;

import com.halvors.lupi.Lupi;

/**
 * Represents the global configuration and also delegates configuration
 * for individual worlds.
 * 
 * @author halvors
 */
public class ConfigManager {
    private final Lupi plugin;
    
    private HashMap<String, WorldConfig> worlds = new HashMap<String, WorldConfig>();
    
    public ConfigManager(Lupi plugin) {
        this.plugin = plugin;
    }

    /**
     * Load the configuration.
     */
    public void load() {
        // Create the default configuration file
        checkConfig(new File(plugin.getDataFolder(), "config.yml"), "config.yml");
        
        Configuration config = plugin.getConfiguration();
        config.load();
        
        // Load configurations for each world
        for (World world : plugin.getServer().getWorlds()) {
            getWorldConfig(world);
        }
    }
    
    /**
     * Save the configuration.
     */
    public void save() {
        Configuration config = plugin.getConfiguration();
        
        worlds.clear();
        
        config.save();
    }
    
    /**
     * Reload the configuration.
     */
    public void reload()  {
        load();
    }
    
    /**
     * Create a default configuration file from the .jar.
     *
     * @param actual
     * @param defaultName
     */
    public void checkConfig(File actual, String defaultName) {
        if (!actual.exists()) {
        
            // Make parent directories
            File parent = actual.getParentFile();
        
            if (!parent.exists()) {
                parent.mkdirs();
            }
            
            if (!actual.exists()) {
                InputStream input = ConfigManager.class.getResourceAsStream(defaultName);
            
                if (input != null) {
                    FileOutputStream output = null;

                    try {
                        output = new FileOutputStream(actual);
                        byte[] buf = new byte[8192];
                        int length = 0;
                        while ((length = input.read(buf)) > 0) {
                            output.write(buf, 0, length);
                        }

                        plugin.log(Level.INFO, "Configuration file written: " + actual.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (input != null) {
                                input.close();
                            }
                        } catch (IOException e) {
                        }

                        try {
                            if (output != null) {
                                output.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get the configuration for a world.
     * 
     * @param world
     * @return
     */
    public WorldConfig getWorldConfig(World world) {
        String worldName = world.getName();
        WorldConfig worldConfig = worlds.get(worldName);
        
        if (worldConfig == null) {
            worldConfig = new WorldConfig(plugin, worldName);
            worlds.put(worldName, worldConfig);
        }

        return worldConfig;
    }
}
