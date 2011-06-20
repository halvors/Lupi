package com.halvors.wolf.event;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;

public class EntityMoveEvent extends EntityEvent {
	private static final long serialVersionUID = 1L;
	private Location location;
	
	public EntityMoveEvent(Entity entity, Location location) {
		super(Event.Type.CUSTOM_EVENT, entity);
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
}
