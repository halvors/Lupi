package org.halvors.lupi.event.wolf;

import org.halvors.lupi.wolf.Wolf;

public class LupiWolfLoadEvent extends LupiWolfEvent {
	private static final long serialVersionUID = -5040398435708770833L;

	public LupiWolfLoadEvent(Wolf wolf) {
		super("LupiWolfLoadEvent", wolf);
	}
}
