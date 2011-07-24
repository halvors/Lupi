package org.halvors.lupi.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.halvors.lupi.event.EventFactory;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.WolfManager;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class WolfUtil {
	private static WolfManager wolfManager = WolfManager.getInstance();
	
	/**
	 * Show info about the wolf.
	 * 
	 * @param sender
	 * @param wolf
	 */
	public static void showInfo(CommandSender sender, Wolf wolf) {
		org.bukkit.entity.Wolf bukkitWolf = wolf.getEntity();
		int health = bukkitWolf.getHealth();
		int maxHealth = 20;
        
        // TODO: Improve information.
        sender.sendMessage("Name: " + ChatColor.YELLOW + wolf.getName());
        sender.sendMessage("Health: " + ChatColor.YELLOW + Integer.toString(health) + "/" + Integer.toString(maxHealth));
	}
	
	/**
	 * Get Wolf by uniqueId.
	 * 
	 * @param uniqueId
	 * @return the Wolf or null if not found
	 */
	public static org.bukkit.entity.Wolf getBukkitWolf(UUID uniqueId) {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity.getUniqueId().equals(uniqueId)) {
					return (org.bukkit.entity.Wolf) entity;
				}
			}
		}
			
		return null;
	}
    
	/**
	 * Do the nearby entity check.
	 */
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
    
    /**
     * Do the armor check.
     * 
     * @param wolf
     * @param event
     */
    public static void doArmorCheck(Wolf wolf, EntityDamageEvent event) {
    	int damage = event.getDamage();
    	
    	if (wolf.hasArmor()) {
    		ItemStack itemStack = wolf.findArmor();
    		int newDamage = damage - 2;
    		int armorPoint = 0;
    		
    		switch (itemStack.getType()) {
    		case LEATHER_CHESTPLATE:
    			armorPoint = 49;
    			break;
    			
    		case GOLD_CHESTPLATE:
    			armorPoint = 96;
    			break;
    			
    		case CHAINMAIL_CHESTPLATE:
    			armorPoint = 96;
    			break;
    			
    		case IRON_CHESTPLATE:
    			armorPoint = 192;
    			break;
    			
    		case DIAMOND_CHESTPLATE:
    			armorPoint = 384;
    			break;
    		}
    		
    		short newDurability = (short)(armorPoint * (itemStack.getDurability() / 384));
    		
    		if (itemStack != null && newDamage <= 20 && newDurability <= 384) {
    			itemStack.setDurability(newDurability);
    			event.setDamage(newDamage);
    		}
    	}
    }
}
