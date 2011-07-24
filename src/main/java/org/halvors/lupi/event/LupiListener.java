package org.halvors.lupi.event;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class LupiListener extends CustomEventListener implements Listener {
	public LupiListener() {
		
	}
	
	public void onLupiServerTick(LupiServerTickEvent event) {
		
	}
	
	public void onCustomEvent(Event event) {
		if (event instanceof LupiServerTickEvent) {
			onLupiServerTick((LupiServerTickEvent) event);
		}
	}
}
