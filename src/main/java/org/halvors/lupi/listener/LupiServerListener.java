package org.halvors.lupi.listener;

import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.map.MapView;
import org.halvors.lupi.Lupi;

public class LupiServerListener extends ServerListener {
//	private final Lupi plugin;
	
	public LupiServerListener(Lupi plugin) {
//		this.plugin = plugin;
	}
	
	public void MapInitialize(MapInitializeEvent event) {
		MapView mapView = event.getMap();
	}
}
