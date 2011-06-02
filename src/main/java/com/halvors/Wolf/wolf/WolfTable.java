/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
 *
 * This file is part of WolfControl.
 *
 * WolfControl is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WolfControl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WolfControl.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.halvors.Wolf.wolf;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity()
@Table(name = "wc_wolf")
public class WolfTable {
	@Id
    private int id;
	@NotNull
	private int entityid;
	@NotEmpty
    private String name;
	@NotEmpty
    private String owner;
	@NotEmpty
    private String world;
    
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getEntityId() {
    	return entityid;
    }
    
    public void setEntityId(final int entityid) {
    	this.entityid = entityid;
    }
    
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getWorld() {
    	return world;
    }
    
    public void setWorld(final String world) {
    	this.world = world;
    }
}