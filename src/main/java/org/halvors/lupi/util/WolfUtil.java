package org.halvors.lupi.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.halvors.lupi.event.EventFactory;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.WolfManager;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class WolfUtil {
	private static WolfManager wolfManager = WolfManager.getInstance();
	
	/**
	 * Get Entity by uniqueId.
	 * 
	 * @param uniqueId
	 * @return the Entity or null if not found
	 */
	public static Entity getBukkitEntity(UUID uniqueId) {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity.getUniqueId().equals(uniqueId)) {
					return entity;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Get Wolf by uniqueId.
	 * 
	 * @param uniqueId
	 * @return the Wolf or null if not found
	 */
	public static org.bukkit.entity.Wolf getBukkitWolf(UUID uniqueId) {
		Entity entity = getBukkitEntity(uniqueId);
		
		if (entity instanceof Wolf) {
			return (org.bukkit.entity.Wolf) entity;
		}
		
		return null;
	}
	
	/**
	 * Show info about the wolf.
	 * 
	 * @param sender
	 * @param wolf
	 */
	public static void showInfo(CommandSender sender, Wolf wolf) {
		org.bukkit.entity.Wolf bukkitWolf = wolf.getEntity();
		
		int health = bukkitWolf.getHealth() / 2;
        int maxHealth = 10;
        
        // TODO: Improve information.
        sender.sendMessage("Name: " + ChatColor.YELLOW + wolf.getName());
        sender.sendMessage("Health: " + ChatColor.YELLOW + Integer.toString(health) + "/" + Integer.toString(maxHealth));
	}
    
    public static void doNearbyEntityCheck() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof org.bukkit.entity.Wolf) {
					org.bukkit.entity.Wolf bukkitWolf = (org.bukkit.entity.Wolf) entity;
						
					if (bukkitWolf.isTamed() && wolfManager.hasWolf(bukkitWolf)) {
						Wolf wolf = wolfManager.getWolf(bukkitWolf);
							
						for (Entity nearbyEntity : bukkitWolf.getNearbyEntities(1, 1, 1)) {
							// Make wolf pickup item, remove the dropped item and add it to wolf's inventory.
							if (nearbyEntity instanceof Item) {
								if (wolf.hasLoadedInventory()) {
									WolfInventory wi = wolf.getInventory();
									
									LupiWolfPickupItemEvent event = EventFactory.callLupiWolfPickupItemEvent(wolf, wi);
									
									if (!event.isCancelled()) {
										Item item = (Item) nearbyEntity;
										
										wi.addItem(item.getItemStack());
										item.remove();
									}
								}
							}
						}
					}
				}
			}
		}
	}
    
    public static void doArmorCheck(Wolf wolf, EntityDamageEvent event) {
    	int damage = event.getDamage();
    	
    	if (wolf.hasArmor()) {
    		int newDamage = damage + 5;
    		short newDurability = 0;
    		
    		if (newDamage <= 20 && newDurability <= 384) {
    			// Set armor durability here.
    			event.setDamage(newDamage);
    		}
    	}
    }
    
    public static void doInventoryFoodCheck(Wolf wolf, EntityDamageEvent event) {
    	int damage = event.getDamage();
    	
    	if (wolf.hasInventoryFood()) {
    		int newDamage = damage + 2;
    		
    		if (newDamage <= 20) {
    			// Remove food from inventory here.
    			event.setDamage(newDamage);
    		}
    	}
    }
}
