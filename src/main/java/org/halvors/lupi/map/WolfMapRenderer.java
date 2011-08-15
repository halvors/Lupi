package org.halvors.lupi.map;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.halvors.lupi.wolf.WolfManager;

public class WolfMapRenderer extends org.bukkit.map.MapRenderer {
	WolfManager wm = WolfManager.getInstance();
	
	public WolfMapRenderer() {
		
	}
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		List<Wolf> wolves = wm.getBukkitWolves(player);
		
		for (Wolf wolf : wolves) {
			// TODO: Draw all wolves cursors here.
		}
		
		canvas.drawText(10, 10, MinecraftFont.Font, "Ubuntu");
	}
}