/* 
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
 * Copyright (C) 2011 adamonline45 <adamonline45@gmail.com>
 * 
 * This file is part of Lupi.
 * 
 * Lupi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Lupi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Lupi.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.halvors.lupi.wolf.inventory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a WolfInventoryTable
 * 
 * @author halvors
 */
@Entity()
@Table(name = "lupi_wolfInventory")
public class WolfInventoryTable {
    @Id
    private int id;
    private String uniqueId;
    private String chestRow1;
    private String chestRow2;
    private String chestRow3;
        
    public int getId() {
        return id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public String getChestRow1() {
        return chestRow1;
    }

    public void setChestRow1(String chestRow1) {
        this.chestRow1 = chestRow1;
    }

    public String getChestRow2() {
        return chestRow2;
    }

    public void setChestRow2(String chestRow2) {
        this.chestRow2 = chestRow2;
    }

    public String getChestRow3() {
        return chestRow3;
    }
    
    public void setChestRow3(String chestRow3) {
        this.chestRow3 = chestRow3;
    }
    
    public String[] getChestRows() {
        String[] rows = new String[3];
        rows[0] = this.chestRow1;
        rows[1] = this.chestRow2;
        rows[2] = this.chestRow3;
        
        return rows;
    }
    
    public void setChestRows(String[] rows) {
        this.chestRow1 = rows[0];
        this.chestRow2 = rows[1];
        this.chestRow3 = rows[2];
    }
}
