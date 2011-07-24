package org.halvors.lupi.event.wolf;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.halvors.lupi.event.wolf.inventory.LupiWolfDropItemEvent;
import org.halvors.lupi.event.wolf.inventory.LupiWolfPickupItemEvent;

public class LupiWolfListener extends CustomEventListener implements Listener {
	public LupiWolfListener() {
		
	}
	
	public void onLupiWolfDropItem(LupiWolfDropItemEvent event) {
		
	}
	
	public void onLupiWolfPickupItem(LupiWolfPickupItemEvent event) {
		
	}
	
	@Override
	public void onCustomEvent(Event event) {
		if (event instanceof LupiWolfDropItemEvent) {
			onLupiWolfDropItem((LupiWolfDropItemEvent) event);
		} else if (event instanceof LupiWolfPickupItemEvent) {
			onLupiWolfPickupItem((LupiWolfPickupItemEvent) event);
		}
	}
}
