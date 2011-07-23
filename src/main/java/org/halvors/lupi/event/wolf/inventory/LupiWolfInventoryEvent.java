package org.halvors.lupi.event.wolf.inventory;

import org.halvors.lupi.event.wolf.LupiWolfEvent;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class LupiWolfInventoryEvent extends LupiWolfEvent {
	private static final long serialVersionUID = 472165692667983345L;

	private WolfInventory inventory;
	
	public LupiWolfInventoryEvent(String name, Wolf wolf, WolfInventory wi) {
		super(name, wolf);
		this.inventory = wi;
	}
	
	public WolfInventory getInventory() {
		return inventory;
	}
}
