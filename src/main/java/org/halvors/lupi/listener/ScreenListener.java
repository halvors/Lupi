package org.halvors.lupi.listener;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Screen;
import org.halvors.lupi.Lupi;

public class ScreenListener extends org.getspout.spoutapi.event.screen.ScreenListener {
//	private final Lupi plugin;
	
	public ScreenListener(Lupi plugin) {
//		this.plugin = plugin;
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
		if (!event.isCancelled()) {
			Screen screen = event.getScreen();
			
			if (screen instanceof PopupScreen) {
				PopupScreen popup = (PopupScreen) screen;
				popup.close();
			}
		}
	}
}
