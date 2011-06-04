/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 *
 * This file is part of Wolf.
 *
 * Wolf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wolf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Wolf.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.halvors.Wolf.wolf;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a WolfTable
 * 
 * @author halvors
 */
@Entity()
@Table(name = "wolf_wolf")
public class WolfTable {
    @Id
    private int id;
    private int entityId;
    private String name;
    private String owner;
    private double locationX;
    private double locationY;
    private double locationZ;
    private String world;
    
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getEntityId() {
        return entityId;
    }
    
    public void setEntityId(final int entityId) {
        this.entityId = entityId;
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
    
    public double getLocationX() {
        return locationX;
    }
    
    public void setLocationX(final double locationX) {
        this.locationX = locationX;
    }
    
    public double getLocationY() {
        return locationY;
    }
    
    public void setLocationY(final double locationY) {
        this.locationY = locationY;
    }
    
    public double getLocationZ() {
        return locationZ;
    }
    
    public void setLocationZ(final double locationZ) {
        this.locationZ = locationZ;
    }
    
    public String getWorld() {
        return world;
    }
    
    public void setWorld(final String world) {
        this.world = world;
    }
}