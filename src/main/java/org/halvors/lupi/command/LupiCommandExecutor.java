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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.util.ConfigurationManager;
import org.halvors.lupi.util.WolfUtil;
import org.halvors.lupi.util.WorldConfiguration;
import org.halvors.lupi.wolf.SelectedWolfManager;
import org.halvors.lupi.wolf.WolfManager;
import org.halvors.lupi.wolf.WolfTable;

/**
 * Represents a CommandExecutor.
 * 
 * @author halvors
 */
public class LupiCommandExecutor implements CommandExecutor {
    private final Lupi plugin;
    private final ConfigurationManager configManager;
    private final WolfManager wolfManager;
    private final SelectedWolfManager selectedWolfManager;

    public LupiCommandExecutor(Lupi plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigurationManager();
        this.wolfManager = plugin.getWolfManager();
        this.selectedWolfManager = plugin.getSelectedWolfManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (args.length == 0) {
    		if (sender.hasPermission("lupi.wolf.help")) {
    			if (sender instanceof Player) {
    				showPlayerWolves((Player) sender);
    			} else {
    				showWolves(sender);
    			}
    			
    			return true;
    		}
    	} else {
    		String subCommand = args[0];
    		
    		if (subCommand.equalsIgnoreCase("help")) {
                if (sender.hasPermission("lupi.wolf.help")) {
                	showHelp(sender, label);

                    return true;
                }
    		} else if (subCommand.equalsIgnoreCase("list")) {
                if (sender.hasPermission("lupi.wolf.list")) {
                    showWolves(sender);
                    
                    return true;
                }
    		}
    	
    		if (sender instanceof Player) {
    			Player player = (Player) sender;
                
    			if (subCommand.equalsIgnoreCase("info")) {
    				if (sender.hasPermission("lupi.wolf.info")) {
    					org.halvors.lupi.wolf.Wolf wolf = null;
    					String owner = player.getName();
    					
    					if (args.length == 2) {
                        	if (selectedWolfManager.hasSelectedWolf(player)) {
                        		wolf = (org.halvors.lupi.wolf.Wolf) selectedWolfManager.getSelectedWolf(player);;
                            } else {
                                player.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 3){
                        	String oldName = args[1];
                               
                            if (wolfManager.hasWolf(oldName, owner)) {
                            	wolf = wolfManager.getWolf(oldName, owner);
                            } else {
                                player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	player.sendMessage(ChatColor.RED + "Too many arguments.");
                        }
    					
    					if (wolf != null) {
    						WolfUtil.showInfo(sender, wolf);
    					}
    					
    					return true;
    				}
    			}
    			
    			if (subCommand.equalsIgnoreCase("name")) {
    				if (player.hasPermission("lupi.wolf.name")) {
    				    if (selectedWolfManager.hasSelectedWolf(player)) {
                            Wolf wolf = selectedWolfManager.getSelectedWolf(player);
                            
                        	if (wolfManager.hasWolf(wolf)) {
                            	org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                            	
                                player.sendMessage("This wolf's name is " + ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + ".");
                            }
                        }
    				    
    				    return true;
    				}
    			} else if (subCommand.equalsIgnoreCase("setname")) {
    			    if (player.hasPermission("lupi.wolf.setname")) {
    					org.halvors.lupi.wolf.Wolf wolf = null;
                        String owner = player.getName();
                        String name = null;
                        String grammar = null;
                        
                        if (args.length == 2) {
                        	if (selectedWolfManager.hasSelectedWolf(player)) {
                        		wolf = (org.halvors.lupi.wolf.Wolf) selectedWolfManager.getSelectedWolf(player);
                                name = args[1];
                                grammar = "this";
                            } else {
                                player.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else if (args.length == 3){
                        	String oldName = args[1];
                               
                            if (wolfManager.hasWolf(oldName, owner)) {
                            	wolf = wolfManager.getWolf(oldName, owner);
                                name = args[2];
                                grammar = "that";
                            } else {
                                player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
                        } else {
                        	player.sendMessage(ChatColor.RED + "Too many arguments.");
                        }

                        if (wolf != null && name != null) {
                       		wolf.setName(name);
                               
                            player.sendMessage("The name of " + grammar + " wolf has been set to " + ChatColor.YELLOW + name + ChatColor.WHITE + ".");
                        }
                           
                        return true;
    				}
    			} else if (subCommand.equalsIgnoreCase("call")) {
    				if (player.hasPermission("lupi.wolf.call")) {
    					if (args.length == 2) {
    						String name = args[1];
                			String owner = player.getName();
                			   
                			if (wolfManager.hasWolf(name, owner)) {
                				Wolf wolf = wolfManager.getWolf(name, owner).getEntity();
                            	wolf.teleport(player);

                            	player.sendMessage(ChatColor.GREEN + "Your wolf is on its way.");
                            }
    					}
                           
    					return true;
    				}
    			} else if (subCommand.equalsIgnoreCase("stop")) {
    				if (player.hasPermission("lupi.wolf.stop")) {
    					Wolf wolf = null;
                        String owner = player.getName();

                        if (args.length <= 1) {
                        	if (selectedWolfManager.hasSelectedWolf(player)) {
                        		wolf = selectedWolfManager.getSelectedWolf(player);
                            } else {
                            	player.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else {
                        	String name = args[1];

                        	if (wolfManager.hasWolf(name, owner)) {
                        		wolf = wolfManager.getWolf(name, owner).getEntity();
                        	} else {
                        		player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                        	}
                        }

                        if (wolfManager.hasWolf(wolf) && wolf != null) {
                        	org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

                        	wolf.setTarget(null);

                        	player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " has stopped attacking.");
                        }

                        return true;
    				}
    			} else if (subCommand.equalsIgnoreCase("give")) {
    				if (player.hasPermission("lupi.wolf.give")) {
    					Wolf wolf = null;
    					String owner = player.getName();
    					Player receiver = null;
    					World world = player.getWorld();
    					WorldConfiguration worldConfig = configManager.get(world);
                           
    					if (args.length == 2) {
    						if (selectedWolfManager.hasSelectedWolf(player)) {
    							wolf = selectedWolfManager.getSelectedWolf(player);
    							receiver = getPlayer(args[1]);
    						} else {
    							player.sendMessage(ChatColor.RED + "No wolf selected.");
    						}
    					} else if (args.length == 3) {
    						String name = args[1];

    						if (wolfManager.hasWolf(name, owner)) {
    							wolf = wolfManager.getWolf(name, owner).getEntity();
    							receiver = getPlayer(args[2]);;
                            } else {
                            	player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                            }
    					} else {
    						player.sendMessage(ChatColor.RED + "Too many arguments.");
    					}

                    	if (wolfManager.hasWolf(wolf) && wolf != null && receiver != null) {
                    		org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                    		String name = wolf1.getName();
                    		String to = receiver.getName();
                    		int limit = worldConfig.wolfLimit;
                               
                    		if (limit > 0) {
                    			List<WolfTable> wts = wolfManager.getWolfTables(player);
                                   
                    			if (limit <= wts.size()) {
                    				player.sendMessage("You can't give " + ChatColor.YELLOW + name + ChatColor.WHITE + " to " + ChatColor.YELLOW + to + ChatColor.WHITE + " because he has reached the limit, limit is " + ChatColor.YELLOW + Integer.toString(limit) + ChatColor.WHITE + " wolves.");
                    				receiver.sendMessage(owner + " tried to give you " + ChatColor.YELLOW + name + ChatColor.WHITE + " but you can't receive the wolf because the limit is " + ChatColor.YELLOW + Integer.toString(limit)  + ChatColor.WHITE + " wolves.");
                    			}
                        	}
                               
                           	wolf1.setOwner(receiver);
                           	wolf.teleport(receiver);
                               
                           	player.sendMessage(ChatColor.YELLOW + name + ChatColor.WHITE + " was given to " + ChatColor.YELLOW + to + ChatColor.WHITE + ".");
                           	receiver.sendMessage("You have received " + ChatColor.YELLOW + name + ChatColor.WHITE + " from " + ChatColor.YELLOW + owner + ChatColor.WHITE + ".");
                    	}

                    	return true;
    				}
    			} else if (subCommand.equalsIgnoreCase("release")) {
    				if (player.hasPermission("lupi.wolf.release")) {
    					Wolf wolf = null;
    					String owner = player.getName();

    					if (args.length == 1) {
    						if (selectedWolfManager.hasSelectedWolf(player)) {
    							wolf = selectedWolfManager.getSelectedWolf(player);
    						} else {
    							player.sendMessage(ChatColor.RED + "No wolf selected.");
    						}
    					} else if (args.length == 2){
    						String name = args[1];

    						if (wolfManager.hasWolf(name, owner)) {
    							wolf = wolfManager.getWolf(name, owner).getEntity();
    						} else {
    							player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
    						}
    					} else {
    						player.sendMessage(ChatColor.RED + "Too many arguments.");
    					}

    					if (wolfManager.hasWolf(wolf) && wolf != null) {
    						org.halvors.lupi.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);

    						player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " has been released.");
                                
    						wolfManager.releaseWolf(wolf);
    					}

    					return true;
                	}
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
        	int size = wolves.size();
        	
        	// TODO: Add pages support here.
        	player.sendMessage(ChatColor.YELLOW + "Your wolves (" + ChatColor.WHITE + size + ChatColor.YELLOW + ")");
        	
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
        	sender.sendMessage(ChatColor.YELLOW + "Wolves (" + ChatColor.WHITE + wolves.size() + ChatColor.YELLOW + ")");
        	
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
        
        if (sender.hasPermission("lupi.wolf")) {
        	sender.sendMessage(command + ChatColor.YELLOW + " - Show your wolves.");
        }
        
        if (sender.hasPermission("lupi.help")) {
        	sender.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
        }
        
        if (sender.hasPermission("lupi.list")) {
        	sender.sendMessage(command + "list" + ChatColor.YELLOW + " - Show a list of tamed wolves.");
        }
        
        if (sender.hasPermission("lupi.wolf.name")) {
        	sender.sendMessage(command + "name " + ChatColor.YELLOW + " - Show your wolf's name.");
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
    
    /**
     * Get the best matching player.
     * 
     * @param name
     * @return the Player
     */
    public Player getPlayer(String name) {
    	return Bukkit.getServer().matchPlayer(name).get(0);
    }
}
