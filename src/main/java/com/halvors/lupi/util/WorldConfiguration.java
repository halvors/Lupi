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
public class WorldConfiguration {
//    private Lupi plugin;
    
    private final ConfigurationManager configManager;
    
    private String worldName;
    private Configuration config;
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
    
    public WorldConfiguration(Lupi plugin, String worldName) {
//        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.worldName = worldName;

        File baseFolder = new File(plugin.getDataFolder(), "worlds/" + worldName);
        configFile = new File(baseFolder, "config.yml");
        
        configManager.createDefaultConfiguration(configFile, "config_world.yml");

        config = new Configuration(this.configFile);
        loadConfiguration();

        plugin.log(Level.INFO, "Loaded configuration for world '" + worldName + '"');
    }
    
    /**
     * Load the configuration.
     */
    private void loadConfiguration() {
        config.load();

        wolfEnable = config.getBoolean("wolf.enable", wolfEnable);
        wolfItem = config.getInt("wolf.item", wolfItem);
        wolfFriendly = config.getBoolean("wolf.friendly", wolfFriendly);
        wolfPvp = config.getBoolean("wolf.pvp", wolfPvp);
        wolfLimit = config.getInt("wolf.limit", wolfLimit);
        
        infoItem = config.getInt("info.item", infoItem);
        
        inventoryEnable = config.getBoolean("inventory.enable", inventoryEnable);
        inventoryItem = config.getInt("inventory.item", inventoryItem);
        
        config.save();
    }
    
    public String getWorldName() {
        return this.worldName;
    }
}
