package org.halvors.lupi;

import org.halvors.lupi.util.WolfUtil;

public class ServerTickTask implements Runnable {
	@Override
	public void run() {
		WolfUtil.doNearbyEntityCheck();
	}
}
