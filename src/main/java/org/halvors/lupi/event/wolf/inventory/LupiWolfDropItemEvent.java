package org.halvors.lupi.event.wolf.inventory;

import org.bukkit.event.Cancellable;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class LupiWolfDropItemEvent extends LupiWolfInventoryEvent implements Cancellable {
	private static final long serialVersionUID = -2600345992877690749L;
	
	private boolean cancelled;
	
	public LupiWolfDropItemEvent(Wolf wolf, WolfInventory wi) {
		super("LupiWolfDropItemEvent", wolf, wi);
	}
	
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
