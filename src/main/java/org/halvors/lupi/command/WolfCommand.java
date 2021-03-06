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

package org.halvors.lupi.command;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.util.ConfigurationManager;
import org.halvors.lupi.util.LupiUtil;
import org.halvors.lupi.util.WolfUtil;
import org.halvors.lupi.util.WorldConfiguration;
import org.halvors.lupi.wolf.SelectedWolfManager;
import org.halvors.lupi.wolf.Wolf;
import org.halvors.lupi.wolf.WolfManager;

/**
 * WolfCommand.
 * 
 * @author halvors
 */
public class WolfCommand implements CommandExecutor {
    private final Lupi plugin;
    private final ConfigurationManager configManager;
    private final WolfManager wolfManager;
    private final SelectedWolfManager selectedWolfManager;

    public WolfCommand(Lupi plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.wolfManager = plugin.getWolfManager();
        this.selectedWolfManager = plugin.getSelectedWolfManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (args.length == 0) {
    		if (sender.hasPermission("lupi.wolf.list")) {
    			if (sender instanceof Player) {
    				showPlayerWolves((Player) sender);
    			} else {
    				showWolves(sender);
    			}
    		}
    		
    		return true;
    	} else {
    		String subCommand = args[0];
    		
    		if (subCommand.equalsIgnoreCase("reload")) {
    			if (sender.hasPermission("lupi.admin.reload")) {
    				configManager.reload();
    				
    				sender.sendMessage(ChatColor.GREEN + "Reload complete.");
    			}
    			
    			return true;
    		} else if (subCommand.equalsIgnoreCase("help")) {
                if (sender.hasPermission("lupi.wolf.help")) {
                	showHelp(sender, label);
                }
                
                return true;
    		} else if (subCommand.equalsIgnoreCase("list")) {
                if (sender.hasPermission("lupi.wolf.list")) {
                    showWolves(sender);
                }
                
                return true;
            }
        
            if (sender instanceof Player) {
                Player player = (Player) sender;
                
                if (subCommand.equalsIgnoreCase("info")) {
                    if (sender.hasPermission("lupi.wolf.info")) {
                        Wolf wolf = null;
                        String owner = player.getName();
                        
                        if (args.length == 1) {
                            if (selectedWolfManager.hasSelectedWolf(player)) {
                                wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                            } else {
                                sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 2) {
                            String name = args[1];
                               
                            if (wolfManager.hasWolf(name, owner)) {
                                wolf = wolfManager.getWolf(name, owner);
                            } else {
                                sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	sender.sendMessage(ChatColor.RED + "Too many arguments.");
                        }
                        
                        if (wolf != null) {
                            WolfUtil.showInfo(sender, wolf);
                        }
                    }
                    
                    return true;
                } else if (subCommand.equalsIgnoreCase("setname")) {
                    if (sender.hasPermission("lupi.wolf.setname")) {
                        Wolf wolf = null;
                        String owner = player.getName();
                        String name = null;
                        
                        if (args.length == 2) {
                            if (selectedWolfManager.hasSelectedWolf(player)) {
                                wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                                name = args[1];
                            } else {
                            	sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 3){
                            String oldName = args[1];
                               
                            if (wolfManager.hasWolf(oldName, owner)) {
                                wolf = wolfManager.getWolf(oldName, owner);
                                name = args[2];
                            } else {
                            	sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	sender.sendMessage(ChatColor.RED + "Too many arguments.");
                        }

                        if (wolf != null && name != null) {
                        	sender.sendMessage(ChatColor.YELLOW + wolf.getName() + ChatColor.WHITE + " has changed name to " + ChatColor.YELLOW + name + ChatColor.WHITE + ".");
                            wolf.setName(name);
                        }
                    }
                    
                    return true;
                } else if (subCommand.equalsIgnoreCase("call")) {
                    if (sender.hasPermission("lupi.wolf.call")) {
                    	Wolf wolf = null;
                        String owner = player.getName();
                    	
                    	if (args.length == 1) {
                    		if (selectedWolfManager.hasSelectedWolf(player)) {
                    			wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                            } else {
                            	sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                    	} else if (args.length == 2) {
                    		String name = args[1];
                    		
                    		if (wolfManager.hasWolf(name, owner)) {
                                wolf = wolfManager.getWolf(name, owner);
                            } else {
                            	sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                    	} else {
                    		sender.sendMessage(ChatColor.RED + "Too many arguments.");
                        }
                    	
                    	if (wolf != null) {
                            org.bukkit.entity.Wolf bukkitWolf = wolf.getEntity();
                            bukkitWolf.teleport(player);

                            sender.sendMessage(ChatColor.GREEN + "Your wolf is on it's way.");
                    	}
                    }
                    
                    return true;
                } else if (subCommand.equalsIgnoreCase("stop")) {
                    if (sender.hasPermission("lupi.wolf.stop")) {
                        Wolf wolf = null;
                        String owner = player.getName();

                        if (args.length == 1) {
                            if (selectedWolfManager.hasSelectedWolf(player)) {
                                wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                            } else {
                            	sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 2){
                            String name = args[1];

                            if (wolfManager.hasWolf(name, owner)) {
                                wolf = wolfManager.getWolf(name, owner);
                            } else {
                            	sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	sender.sendMessage(ChatColor.RED + "Too many arguments.");
                        }

                        if (wolf != null) {
                            org.bukkit.entity.Wolf bukkitWolf = wolf.getEntity();

                            bukkitWolf.setTarget(null);

                            sender.sendMessage(ChatColor.YELLOW + wolf.getName() + ChatColor.WHITE + " has stopped attacking.");
                        }
                    }
                    
                    return true;
                } else if (subCommand.equalsIgnoreCase("give")) {
                    if (sender.hasPermission("lupi.wolf.give")) {
                        Wolf wolf = null;
                        Player receiver = null;
                        World world = player.getWorld();
                        WorldConfiguration worldConfig = configManager.get(world);
                        String owner = player.getName();
                           
                        if (args.length == 2) {
                            if (selectedWolfManager.hasSelectedWolf(player)) {
                                wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                                receiver = LupiUtil.getPlayer(args[1]);
                            } else {
                            	sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 3) {
                            String name = args[1];

                            if (wolfManager.hasWolf(name, owner)) {
                                wolf = wolfManager.getWolf(name, owner);
                                receiver = LupiUtil.getPlayer(args[2]);
                            } else {
                            	sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	sender.sendMessage(ChatColor.RED + "Too many arguments.");
                        }

                        if (wolf != null && receiver != null) {
                            org.bukkit.entity.Wolf bukkitWolf = wolf.getEntity();
                            String name = wolf.getName();
                            String to = receiver.getName();
                            int limit = worldConfig.wolfLimit;
                            
                            if (limit > 0) {
                                if (limit <= wolfManager.getWolves(player).size()) {
                                	sender.sendMessage("You can't give " + ChatColor.YELLOW + name + ChatColor.WHITE + " to " + ChatColor.YELLOW + to + ChatColor.WHITE + " because he has reached the limit, limit is " + ChatColor.YELLOW + Integer.toString(limit) + ChatColor.WHITE + " wolves.");
                                    receiver.sendMessage(owner + " tried to give you " + ChatColor.YELLOW + name + ChatColor.WHITE + " but you can't receive the wolf because the limit is " + ChatColor.YELLOW + Integer.toString(limit)  + ChatColor.WHITE + " wolves.");
                                    
                                    return false;
                                }
                            }
                               
                            wolf.setOwner(receiver);
                            bukkitWolf.teleport(receiver);
                               
                            sender.sendMessage(ChatColor.YELLOW + name + ChatColor.WHITE + " was given to " + ChatColor.YELLOW + to + ChatColor.WHITE + ".");
                            receiver.sendMessage("You have received " + ChatColor.YELLOW + name + ChatColor.WHITE + " from " + ChatColor.YELLOW + owner + ChatColor.WHITE + ".");
                        }
                    }
                    
                    return true;
                } else if (subCommand.equalsIgnoreCase("release")) {
                    if (sender.hasPermission("lupi.wolf.release")) {
                        Wolf wolf = null;
                        String owner = player.getName();

                        if (args.length == 1) {
                            if (selectedWolfManager.hasSelectedWolf(player)) {
                                wolf = wolfManager.getWolf(selectedWolfManager.getSelectedWolf(player));
                            } else {
                            	sender.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 2){
                            String name = args[1];

                            if (wolfManager.hasWolf(name, owner)) {
                                wolf = wolfManager.getWolf(name, owner);
                            } else {
                            	sender.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Too many arguments.");
                        }

                        if (wolf != null) {
                        	sender.sendMessage(ChatColor.YELLOW + wolf.getName() + ChatColor.WHITE + " has been released.");
                                
                            wolfManager.releaseWolf(wolf.getEntity());
                        }
                    }
                    
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry but these commands are for in-game players only.");
                }
            }
        }
    	
        return false;
    }

    /**
     * Show player wolves.
     * 
     * @param player
     */
    private void showPlayerWolves(Player player) {
        List<org.halvors.lupi.wolf.Wolf> wolves = wolfManager.getWolves(player);
        
        if (!wolves.isEmpty()) {
            player.sendMessage("Your wolves " + ChatColor.YELLOW + "(" + ChatColor.WHITE + wolves.size() + ChatColor.YELLOW + ")");
            
            for (org.halvors.lupi.wolf.Wolf wolf : wolves) {
                player.sendMessage(ChatColor.YELLOW + wolf.getName());
            }
        } else {
            player.sendMessage(ChatColor.RED + "You have no wolves.");
        }
    }
    
    /**
     * Show all wolves.
     * 
     * @param sender
     */
    private void showWolves(CommandSender sender) {
        List<org.halvors.lupi.wolf.Wolf> wolves;
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            wolves = wolfManager.getWolves(player.getWorld());
        } else {
            wolves = wolfManager.getWolves();
        }
        
        if (!wolves.isEmpty()) {
            sender.sendMessage("Wolves " + ChatColor.YELLOW + "(" + ChatColor.WHITE + wolves.size() + ChatColor.YELLOW + ")");
            
            for (org.halvors.lupi.wolf.Wolf wolf : wolves) {
                sender.sendMessage(ChatColor.YELLOW + wolf.getName() + ChatColor.WHITE + " - " + wolf.getOwner().getName());
            }
        } else {
            sender.sendMessage(ChatColor.RED + "There are no tamed wolves.");
        }
    }
    
    /**
     * Show help
     * 
     * @param sender
     * @param label
     */
    private void showHelp(CommandSender sender, String label) {
        String command = "/" + label + " ";
        
        sender.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        sender.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");
        
        if (sender.hasPermission("lupi.wolf.help")) {
            sender.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
        }
        
        if (sender.hasPermission("lupi.wolf.list")) {
            sender.sendMessage(command + "list" + ChatColor.YELLOW + " - Show a list of tamed wolves.");
        }
        
        if (sender.hasPermission("lupi.wolf.info")) {
        	sender.sendMessage(command + "info " + ChatColor.GREEN + "<" + ChatColor.WHITE + "wolf" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Show info about the wolf.");
        }
        
        if (sender.hasPermission("lupi.wolf.setname")) {
            sender.sendMessage(command + "setname " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Set your wolf's name.");
        }
        
        if (sender.hasPermission("lupi.wolf.call")) {
            sender.sendMessage(command + "call " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Call your wolf.");
        }
        
        if (sender.hasPermission("lupi.wolf.stop")) {
            sender.sendMessage(command + "stop " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Stop your wolf from attacking.");
        }
        
        if (sender.hasPermission("lupi.wolf.give")) {
            sender.sendMessage(command + "give " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + "> [" + ChatColor.WHITE + "player" + ChatColor.GREEN + "]" + ChatColor.YELLOW + " - Release your wolf.");
        }
        
        if (sender.hasPermission("lupi.wolf.release")) {
            sender.sendMessage(command + "release " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Release your wolf.");
        }
    }
}
