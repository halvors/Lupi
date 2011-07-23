package org.halvors.lupi.event.wolf;

import org.halvors.lupi.event.LupiEvent;
import org.halvors.lupi.wolf.Wolf;

public class LupiWolfEvent extends LupiEvent {
	private static final long serialVersionUID = -683947048429868380L;

	public Wolf wolf;
	
	public LupiWolfEvent(String name, Wolf wolf) {
		super(name);
		this.wolf = wolf;
	}
	
	public Wolf getWolf() {
		return wolf;
	}
}
