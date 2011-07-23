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
	private final List<String> wolfNames;
	
	public RandomNameUtil(Lupi plugin) {
		this.plugin = plugin;
		this.random = new Random();
		this.wolfNames = new ArrayList<String>();
		initRandomNames();

	}
	
	public void reload() {
		wolfNames.clear();
		initRandomNames();
	}
	
	 /**
     * Generate the table of premade wolf names.
     */
    private void initRandomNames() {  
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(RandomNameUtil.class.getResourceAsStream("wolfnames.txt")));
           
            while (true) {
                String s1;
                
                if ((s1 = bufferedReader.readLine()) == null) {
                    break;
                }
                
                s1 = s1.trim();
                
                if (s1.length() > 0) {
                    wolfNames.add(s1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(wolfNames.size() == 0) {
        	plugin.log(Level.SEVERE, "ERROR: wolfnames.txt either was empty or did not end with a new line!");
        	wolfNames.add("Wolf");
        }
    }
    
    /**
     * Generate a random name.
     * 
     * @return String
     */
    public String getRandomName() {
        return wolfNames.get(random.nextInt(wolfNames.size()));
    }
}
