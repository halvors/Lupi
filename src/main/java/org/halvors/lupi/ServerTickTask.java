package org.halvors.lupi;

import org.halvors.lupi.event.EventFactory;
import org.halvors.lupi.util.WolfUtil;

public class ServerTickTask implements Runnable {
	@Override
	public void run() {
		EventFactory.callLupiServerTickEvent();
		
		WolfUtil.doNearbyEntityCheck();
	}
}
