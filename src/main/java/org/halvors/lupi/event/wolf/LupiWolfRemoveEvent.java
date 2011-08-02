package org.halvors.lupi.event.wolf;

import org.halvors.lupi.wolf.Wolf;

public class LupiWolfRemoveEvent extends LupiWolfEvent {
	private static final long serialVersionUID = 9011036228572898533L;

	public LupiWolfRemoveEvent(Wolf wolf) {
		super("LupiWolfRemoveEvent", wolf);
	}
}
