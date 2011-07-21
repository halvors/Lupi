package org.halvors.lupi;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.WolfManager;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class ServerTickTask implements Runnable {
	private final WolfManager wolfManager = WolfManager.getInstance();
	
	@Override
	public void run() {
		checkNearbyEntities();
	}
	
	private void checkNearbyEntities() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity instanceof org.bukkit.entity.Wolf) {
					org.bukkit.entity.Wolf bukkitWolf = (org.bukkit.entity.Wolf) entity;
					
					if (bukkitWolf.isTamed() && wolfManager.hasWolf(bukkitWolf)) {
						Wolf wolf = wolfManager.getWolf(bukkitWolf);
						
						for (Entity entityItem : bukkitWolf.getNearbyEntities(1, 1, 1)) {
							// Make wolf pickup item, remove the dropped item and add it to wolf's inventory.
							if (entityItem instanceof Item) {
								WolfInventory wi = wolf.getInventory();
								Item item = (Item) entityItem;
								
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
