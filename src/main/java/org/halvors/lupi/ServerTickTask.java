package org.halvors.lupi;

import org.halvors.lupi.event.EventFactory;

public class ServerTickTask implements Runnable {
	@Override
	public void run() {
		EventFactory.callLupiServerTickEvent();
	}
}
