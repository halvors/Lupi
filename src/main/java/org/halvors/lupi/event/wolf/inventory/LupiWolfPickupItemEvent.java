package org.halvors.lupi.event.wolf.inventory;

import org.bukkit.event.Cancellable;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class LupiWolfPickupItemEvent extends LupiWolfInventoryEvent implements Cancellable {
	private static final long serialVersionUID = 8521022015490922212L;

	private boolean cancelled;
	
	public LupiWolfPickupItemEvent(Wolf wolf, WolfInventory wi) {
		super("LupiWolfPickupItemEvent", wolf, wi);
	}
	
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
