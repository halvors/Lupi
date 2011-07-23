package org.halvors.lupi.event;

import org.bukkit.Bukkit;
import org.halvors.lupi.event.wolf.inventory.LupiWolfDropItemEvent;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class EventFactory {
	public static LupiWolfDropItemEvent callLupiWolfDropItemEvent(Wolf wolf, WolfInventory wi) {
		LupiWolfDropItemEvent event = new LupiWolfDropItemEvent(wolf, wi);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		return event;
	}
	
	public static LupiWolfPickupItemEvent callLupiWolfPickupItemEvent(Wolf wolf, WolfInventory wi) {
		LupiWolfPickupItemEvent event = new LupiWolfPickupItemEvent(wolf, wi);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		return event;
	}
}
