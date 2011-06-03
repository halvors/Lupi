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

package com.halvors.Wolf.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import com.halvors.Wolf.util.ConfigManager;
import com.halvors.Wolf.util.WorldConfig;
import com.halvors.Wolf.wolf.SelectedWolfManager;
import com.halvors.Wolf.wolf.WolfManager;
import com.halvors.Wolf.wolf.WolfTable;

/**
 * Command executor
 * 
 * @author halvors
 */
public class WolfCommandExecutor implements CommandExecutor {
	private final com.halvors.Wolf.Wolf plugin;

	private final ConfigManager configManager;
	private final WolfManager wolfManager;
	private final SelectedWolfManager selectedWolfManager;

	public WolfCommandExecutor(final com.halvors.Wolf.Wolf plugin) {
		this.plugin = plugin;
		this.configManager = plugin.getConfigManager();
		this.wolfManager = plugin.getWolfManager();
		this.selectedWolfManager = plugin.getSelectedWolfManager();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender; 
			World world = player.getWorld();
			WorldConfig worldConfig = configManager.getWorldConfig(world);
			
			if (args.length == 0) {
				if (plugin.hasPermissions(player, "WolfControl.list")) {
					showWolves(player);
					
					return true;
				}
			} else {
				String subCommand = args[0];
			
				if (subCommand.equalsIgnoreCase("help")) {
					if (plugin.hasPermissions(player, "WolfControl.help")) {
						showHelp(player, label);
						
						return true;
					}
				} else if (subCommand.equalsIgnoreCase("list")) {
					if (plugin.hasPermissions(player, "WolfControl.list")) {
						showList(player);
						
						return true;
					}
				} else if (subCommand.equalsIgnoreCase("call")) {
					if (plugin.hasPermissions(player, "WolfControl.call")) {
						if (args.length == 1) {
							String name = args[1];
							
							if (wolfManager.hasWolf(name)) {
								Wolf wolf = (Wolf) wolfManager.getWolf(name);
								wolf.teleport(player);
								
								player.sendMessage(ChatColor.GREEN + "Your wolf is comming.");
							}
						}
					
						return true;
					}
				} else if (subCommand.equalsIgnoreCase("stop")) {
					if (plugin.hasPermissions(player, "WolfControl.stop")) {
						if (args.length >= 1) {
							String name = args[1];
							
							if (wolfManager.hasWolf(name)) {
								Wolf wolf = (Wolf) wolfManager.getWolf(name);
								wolf.teleport(player);
								
								player.sendMessage(ChatColor.GREEN + "Your wolf is comming.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "No wolf specified");
						}
					
						return true;
					}
				} else if (subCommand.equalsIgnoreCase("name")) {
					if (plugin.hasPermissions(player, "WolfControl.name")) {
						Wolf wolf = (Wolf) selectedWolfManager.getSelectedWolf(player.getName());
						String name = args[1];
						
//						if (!wolfManager.hasWolf(name)) {
							wolfManager.addWolf(wolf, name);
							
							player.sendMessage(ChatColor.GREEN + "Your wolf was named: " + ChatColor.YELLOW + wolfManager.getName(wolf.getEntityId()));
//						} else {
//							player.sendMessage(ChatColor.RED + "Wolf exists.");
//						}
						
						return true;
					}
									
				/*
				} else if (subCommand.equalsIgnoreCase("name")) {
					if (plugin.hasPermissions(player, "WolfControl.name")) {
						String name = player.getName();
						
						if (args.length == 2) {
							if (selectedWolfManager.hasSelectedWolf(name)) {
								Wolf wolf = (Wolf) selectedWolfManager.getSelectedWolf(name);
								
								if (wolf.isTamed()) {
									String wolfName = args[1];
									WolfTable wolfTable = database.find(WolfTable.class).where().ieq("owner", name).findUnique();

									if (wolfTable != null) {
										wolfTable.setName(wolfName);
									}
									
									database.save(wolfTable);
									
									player.sendMessage(ChatColor.GREEN + "Your wolf was successfully named '" + wolfName + "'.");
								}
							}
						} else {
							player.sendMessage(ChatColor.RED + "No name specified.");
						}
					
						return true;
					}
				*/
				} else if (subCommand.equalsIgnoreCase("release")) {
					if (plugin.hasPermissions(player, "WolfControl.release")) {
						String name = player.getName();
						
						if (args.length <= 1) {
							if (selectedWolfManager.hasSelectedWolf(name)) {
								wolfManager.releaseWolf(selectedWolfManager.getSelectedWolf(name));
								
								player.sendMessage(ChatColor.GREEN + "Your wolf has been released.");
							} else {
								player.sendMessage(ChatColor.RED + "No wolf selected.");
							}
						} else {
							String wolfName = args[2];
							
							if (wolfManager.hasWolf(wolfName) && wolfManager.getOwner(wolfName).getName() == player.getName()) {
								wolfManager.releaseWolf(wolfManager.getWolf(wolfName));
								
								player.sendMessage(ChatColor.GREEN + "Your wolf has been released.");
							} else {
								player.sendMessage(ChatColor.RED + "Wolf does not exists.");
							}
						}
						
						return true;
					}
				} else if (subCommand.equalsIgnoreCase("item")) {
					if (plugin.hasPermissions(player, "WolfControl.item")) {
						int item = worldConfig.item;
						
						if (item != 0) {
							player.getInventory().addItem(new ItemStack(item, 1));
							player.sendMessage(ChatColor.GREEN + "You got the wolf item.");
						}
						
						return true;
					}
				} else {
					if (plugin.hasPermissions(player, "WolfControl.help")) {
						showHelp(player, label);
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	private void showWolves(Player player) {
		List<WolfTable> wolfTables = wolfManager.getWolfTables(player.getName());
		
		player.sendMessage(ChatColor.GREEN + plugin.name + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.version + ChatColor.GREEN + ")");
		
		if (!wolfTables.isEmpty()) {
			for (WolfTable wolfTable : wolfTables) {
				Wolf wolf = wolfManager.getWolf(wolfTable.getEntityId());
				Location pos = wolf.getLocation();
				
				player.sendMessage(ChatColor.YELLOW + wolfTable.getName() + ChatColor.WHITE + " (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You have no wolves.");
		}
	}

	private void showList(Player player) {
		List<WolfTable> wolfTables = wolfManager.getWolfTables();
		
		player.sendMessage(ChatColor.GREEN + plugin.name + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.version + ChatColor.GREEN + ")");
		
		if (!wolfTables.isEmpty()) {
			for (WolfTable wolfTable : wolfTables) {
				player.sendMessage(ChatColor.YELLOW + wolfTable.getName() + ChatColor.WHITE + " - " + wolfTable.getOwner());
			}
		} else {
			player.sendMessage(ChatColor.RED + "There is no tame wolves.");
		}
	}
	
	private void showHelp(Player player, String label) {
		String command = "/" + label + " ";
		
		player.sendMessage(ChatColor.GREEN + plugin.name + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.version + ChatColor.GREEN + ")");
		player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

		if (plugin.hasPermissions(player, "WolfControl.help")) {
			player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.list")) {
			player.sendMessage(command + "list" + ChatColor.YELLOW + " - Show a list of all wolves.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.stop")) {
			player.sendMessage(command + "stop " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show a list of all wolves.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.call")) {
			player.sendMessage(command + "call " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show a list of all wolves.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.name")) {
			player.sendMessage(command + "name " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show or name your wolf.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.release")) {
			player.sendMessage(command + "release " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Release your wolf.");
		}
		
		if (plugin.hasPermissions(player, "WolfControl.item")) {
			player.sendMessage(command + "item" + ChatColor.YELLOW + " - Give you the wolf item.");
		}
	}
}