package org.halvors.lupi.event.wolf;

import org.halvors.lupi.wolf.Wolf;

public class LupiWolfUnloadEvent extends LupiWolfEvent {
	private static final long serialVersionUID = 9011036228572898533L;

	public LupiWolfUnloadEvent(Wolf wolf) {
		super("LupiWolfUnloadEvent", wolf);
	}
}
