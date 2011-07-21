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

package org.halvors.lupi.wolf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.halvors.lupi.Lupi;

public class RandomNameManager {
//    private final Lupi plugin;
    
    private final List<String> names;
    private final Random random;
    
    public RandomNameManager(Lupi plugin) {
//        this.plugin = plugin;
        this.names = new ArrayList<String>();
        this.random = new Random();
        
        // Create the default configuration file
//        createDefaultFile(new File(plugin.getDataFolder(), "wolfNames.txt"), "wolfNames.txt");
        
        initialize();
    }
    
    /**
     * Generate the table of premade wolf names.
     */
    private void initialize() {  
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(RandomNameManager.class.getResourceAsStream("names.txt")));
           
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
    }
    
    /*
    public void createDefaultFile(File actual, String defaultName) {
        // Make parent directories
        File parent = actual.getParentFile();
        
        if (!parent.exists()) {
            parent.mkdirs();
        }
        
        if (actual.exists()) {
            return;
        }
        
        InputStream input = RandomNameManager.class.getResourceAsStream("/" + defaultName);
        
        if (input != null) {
            FileOutputStream output = null;

            try {
                output = new FileOutputStream(actual);
                byte[] buf = new byte[8192];
                int length = 0;
                
                while ((length = input.read(buf)) > 0) {
                    output.write(buf, 0, length);
                }
                
//                plugin.log(Level.INFO, "Default configuration file written: " + actual.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    
                }

                try {
                    if (output != null) {
                        output.close();
                    }
                } catch (IOException e) {
                    
                }
            }
        }
    }
    */
    
    /**
     * Get a random name.
     * 
     * @return String
     */
    public String getRandomName() {
        return names.get(random.nextInt(names.size() - 1));
    }
}
