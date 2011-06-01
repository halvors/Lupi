package com.halvors.WolfControl.wolf;

import java.util.HashMap;

import org.bukkit.entity.Wolf;

import com.halvors.WolfControl.WolfControl;

/**
 * Handle selected wolves
 * 
 * @author halvors
 */
public class SelectedWolfManager {
	private final WolfControl plugin;
	
	private final HashMap<String, Wolf> selectedWolf = new HashMap<String, Wolf>();
	
	public SelectedWolfManager(final WolfControl plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Add a selected wolf
	 * 
	 * @param name
	 * @param wolf
	 */
	public void addSelectedWolf(final String name, final Wolf wolf) {
		if (selectedWolf.containsKey(name)) {
			selectedWolf.remove(name);
		}
		
		selectedWolf.put(name, wolf);
	}
	
	/**
	 * Remove a selected wolf
	 * 
	 * @param name
	 */
	public void removeSelectedWolf(final String name) {
		if (selectedWolf.containsKey(name)) {
			selectedWolf.remove(name);
		}
	}
	
	/**
	 * Get a selected wolf
	 * 
	 * @param name
	 * @return Wolf
	 */
	public Wolf getSelectedWolf(final String name) {
		return selectedWolf.get(name);
	}
	
	/**
	 * Check if Player has a selected wolf
	 * 
	 * @param name
	 * @return if Player has selected wolf
	 */
	public boolean hasSelectedWolf(final String name) {
		if (selectedWolf.containsKey(name)) {
			return true;
		}
		
		return false;
	}
}