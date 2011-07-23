package org.halvors.lupi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.halvors.lupi.Lupi;

public class RandomNameUtil {
	private final Lupi plugin;
	private final Random random;
	private final List<String> names;
	
	public RandomNameUtil(Lupi plugin) {
		this.plugin = plugin;
		this.random = new Random();
		this.names = new ArrayList<String>();
		initRandomNames();

	}
	
	public void reload() {
		names.clear();
		initRandomNames();
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
        	plugin.log(Level.SEVERE, "ERROR: wolfnames.txt either was empty or did not end with a new line!");
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
