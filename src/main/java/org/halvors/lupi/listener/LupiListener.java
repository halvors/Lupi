package org.halvors.lupi.listener;

import org.halvors.lupi.Lupi;
import org.halvors.lupi.event.LupiServerTickEvent;
import org.halvors.lupi.util.WolfUtil;

public class LupiListener extends org.halvors.lupi.event.LupiListener {
//    private final Lupi plugin;
    
    public LupiListener(Lupi plugin) {
//        this.plugin = plugin;
    }
    
    @Override
    public void onLupiServerTick(LupiServerTickEvent event) {
        // Do the nearby entity check.
        WolfUtil.doNearbyEntityCheck();
    }
}
