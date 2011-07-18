package org.halvors.lupi.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;

public class WolfUtil {
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
	public static Wolf getBukkitWolf(UUID uniqueId) {
		Entity entity = getBukkitEntity(uniqueId);
		
		if (entity instanceof Wolf) {
			return (Wolf) entity;
		}
		
		return null;
	}
}
