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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a WolfTable.
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
    private int locationX;
    private int locationY;
    private int locationZ;
    private String world;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getEntityId() {
        return entityId;
    }
    
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public int getLocationX() {
        return this.locationX;
    }
    
    public void setLocationX(int locationX) {
        this.locationZ = locationX;
    }
    
    public int getLocationY() {
        return this.locationY;
    }
    
    public void setLocationY(int locationY) {
        this.locationZ = locationY;
    }
    
    public int getLocationZ() {
        return this.locationZ;
    }
    
    public void setLocationZ(int locationZ) {
        this.locationZ = locationZ;
    }

    public Location getLocation() {
        World world = Bukkit.getServer().getWorld(this.world);
        
        return new Location(world, locationX, locationY, locationZ);
    }
    
    public void setLocation(Location location) {
        this.locationX = location.getBlockX();
        this.locationY = location.getBlockY();
        this.locationZ = location.getBlockZ();
    }
    
    public String getWorld() {
        return world;
    }
    
    public void setWorld(String world) {
        this.world = world;
    }
}