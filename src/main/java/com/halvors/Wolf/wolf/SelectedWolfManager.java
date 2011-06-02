/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>.
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

import java.util.HashMap;

import org.bukkit.entity.Wolf;

/**
 * Handle selected wolves
 * 
 * @author halvors
 */
public class SelectedWolfManager {
	private final com.halvors.Wolf.Wolf plugin;
	
	private final HashMap<String, Wolf> selectedWolf = new HashMap<String, Wolf>();
	
	public SelectedWolfManager(final com.halvors.Wolf.Wolf plugin) {
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