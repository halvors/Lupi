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

package org.halvors.lupi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.halvors.lupi.Lupi;

public class RandomNameUtil {
//    private final Lupi plugin;
    private final Random random;
    private final List<String> names;
    
    private final Logger logger = Logger.getLogger("Minecraft");
    
    public RandomNameUtil(Lupi plugin) {
//        this.plugin = plugin;
        this.random = new Random();
        this.names = new ArrayList<String>();
        initRandomNames();
    }
    
    /**
     * Reload name list
     */    
    public void reload() {
        names.clear();
        initRandomNames();
    }
    
    /**
     * Get the size of the name list
     * @return list size
     */
    public int getSize()
    {
        return names.size();
    }
    
     /**
     * Generate the table of premade wolf names.
     */
    private void initRandomNames() {  
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(RandomNameUtil.class.getResourceAsStream("wolfNames.txt")));
           
            while (true) {
                String s1;
                
                if ((s1 = bufferedReader.readLine()) == null) {
                    break;
                }
                
                s1 = s1.trim();
                
                if (s1.length() > 0) {
                    names.add(s1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (names.size() == 0) {
            logger.log(Level.SEVERE, "ERROR: wolfNames.txt either was empty or did not end with a new line!");
            names.add("Wolf");
        }
    }
    
    /**
     * Generate a random name.
     * 
     * @return String
     */
    public String getRandomName() {
        return names.get(random.nextInt(names.size()));
    }
}
