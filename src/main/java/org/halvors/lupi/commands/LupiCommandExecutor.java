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

package org.halvors.lupi.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.halvors.lupi.Lupi;
import org.halvors.lupi.util.ConfigurationManager;
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

    private final ConfigurationManager configuration;

    public LupiCommandExecutor(Lupi plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfigurationManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (Lupi.hasPermissions(player, "Lupi.wolf.list")) {
                    showPlayerWolves(player);
                }
            } else {
                String subCommand = args[0];

                if (subCommand.equalsIgnoreCase("help")) {
                    if (Lupi.hasPermissions(player, "Lupi.help")) {
                        showHelp(player, label);

                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("list")) {
                       if (Lupi.hasPermissions(player, "Lupi.list")) {
                           showWolves(player);
                           
                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("name")) {
                       if (Lupi.hasPermissions(player, "Lupi.wolf.name")) {
                           if (SelectedWolfManager.hasSelectedWolf(player)) {
                               Wolf wolf = SelectedWolfManager.getSelectedWolf(player);

                               if (WolfManager.hasWolf(wolf)) {
                                   org.halvors.lupi.wolf.Wolf wolf1 = WolfManager.getWolf(wolf);

                                   player.sendMessage("This wolf's name is " + ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + ".");
                               }
                           }

                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("setname")) {
                       if (Lupi.hasPermissions(player, "Lupi.wolf.setname")) {
                           Wolf wolf = null;
                           String owner = player.getName();
                           String name = null;
                           String grammar = "";

                           if (args.length == 2) {
                               if (SelectedWolfManager.hasSelectedWolf(player)) {
                                   wolf = SelectedWolfManager.getSelectedWolf(player);
                                   name = args[1];
                                   grammar = "this";
                               } else {
                                   player.sendMessage(ChatColor.RED + "No wolf selected.");
                                    
                                   return true;
                               }
                           } else if (args.length == 3){
                               String oldName = args[1];
                               
                               if (WolfManager.hasLoadedWolf(oldName, owner)) {
                                   wolf = WolfManager.getWolf(oldName, owner).getEntity();
                                   name = args[2];
                                   grammar = "that";
                               } else {
                                   player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                                    
                                   return true;
                               }
                           } else {
                               player.sendMessage(ChatColor.RED + "Too many arguments.");
                                
                               return true;
                           }

                           if (WolfManager.hasWolf(wolf) && wolf != null && name != null) {
                               org.halvors.lupi.wolf.Wolf wolf1 = WolfManager.getWolf(wolf);
                               wolf1.setName(name);
                               
                               player.sendMessage("The name of " + grammar + " wolf has been set to " + ChatColor.YELLOW + name + ChatColor.WHITE + ".");
                           }
                           
                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("call")) {
                       if (Lupi.hasPermissions(player, "Lupi.wolf.call")) {
                        if (args.length == 2) {
                            String name = args[1];
                            String owner = player.getName();

                            if (WolfManager.hasLoadedWolf(name, owner)) {
                                Wolf wolf = WolfManager.getWolf(name, owner).getEntity();
                                wolf.teleport(player);

                                player.sendMessage(ChatColor.GREEN + "Your wolf is on its way.");
                            }
                           }
                            
                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("stop")) {
                       if (Lupi.hasPermissions(player, "Lupi.wolf.stop")) {
                           Wolf wolf = null;
                           String owner = player.getName();

                           if (args.length <= 1) {
                               if (SelectedWolfManager.hasSelectedWolf(player)) {
                                   wolf = SelectedWolfManager.getSelectedWolf(player);
                               } else {
                                   player.sendMessage(ChatColor.RED + "No wolf selected.");
                               }
                           } else {
                               String name = args[1];

                               if (WolfManager.hasLoadedWolf(name, owner)) {
                                   wolf = WolfManager.getWolf(name, owner).getEntity();
                               } else {
                                  player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                               }
                           }

                           if (WolfManager.hasWolf(wolf) && wolf != null) {
                               org.halvors.lupi.wolf.Wolf wolf1 = WolfManager.getWolf(wolf);

                               wolf.setTarget(null);

                               player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " has stopped attacking.");
                           }

                           return true;
                       }
                   } else if (subCommand.equalsIgnoreCase("give")) {
                       if (Lupi.hasPermissions(player, "Lupi.wolf.give")) {
                           Wolf wolf = null;
                           String owner = player.getName();
                           Player receiver = null;
                           World world = player.getWorld();
                           WorldConfiguration worldConfig = configuration.get(world);
                           
                           if (args.length == 2) {
                               if (SelectedWolfManager.hasSelectedWolf(player)) {
                                   wolf = SelectedWolfManager.getSelectedWolf(player);
                                   receiver = getPlayer(args[1]);
                               } else {
                                   player.sendMessage(ChatColor.RED + "No wolf selected.");
                                    
                                   return true;
                               }
                           } else if (args.length == 3) {
                               String name = args[1];

                               if (WolfManager.hasLoadedWolf(name, owner)) {
                                   wolf = WolfManager.getWolf(name, owner).getEntity();
                                   receiver = getPlayer(args[2]);;
                               } else {
                                   player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                                    
                                   return true;
                               }
                           } else {
                               player.sendMessage(ChatColor.RED + "Too many arguments.");
                                
                               return true;
                           }

                           if (WolfManager.hasWolf(wolf) && wolf != null && receiver != null) {
                               org.halvors.lupi.wolf.Wolf wolf1 = WolfManager.getWolf(wolf);
                               String name = wolf1.getName();
                               String to = receiver.getName();
                               int limit = worldConfig.wolfLimit;
                               
                        	   if (limit > 0) {
                                   List<WolfTable> wts = WolfManager.getWolfTables(player);
                                   
                                   if (limit <= wts.size()) {
                                       player.sendMessage("You can't give " + ChatColor.YELLOW + name + ChatColor.WHITE + " to " + ChatColor.YELLOW + to + ChatColor.WHITE + " because he has reached the limit, limit is " + ChatColor.YELLOW + Integer.toString(limit) + ChatColor.WHITE + " wolves.");
                                       receiver.sendMessage(owner + " tried to give you " + ChatColor.YELLOW + name + ChatColor.WHITE + " but you can't receive the wolf because the limit is " + ChatColor.YELLOW + Integer.toString(limit)  + ChatColor.WHITE + " wolves.");
                                       
                                       return true;
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
                       if (Lupi.hasPermissions(player, "Lupi.wolf.release")) {
                           Wolf wolf = null;
                           String owner = player.getName();

                           if (args.length == 1) {
                               if (SelectedWolfManager.hasSelectedWolf(player)) {
                                   wolf = SelectedWolfManager.getSelectedWolf(player);
                               } else {
                                   player.sendMessage(ChatColor.RED + "No wolf selected.");
                                    
                                   return true;
                               }
                           } else if (args.length == 2){
                               String name = args[1];

                               if (WolfManager.hasLoadedWolf(name, owner)) {
                                   wolf = WolfManager.getWolf(name, owner).getEntity();
                               } else {
                                   player.sendMessage(ChatColor.RED + "That wolf doesn't exists.");
                                    
                                   return true;
                               }
                           } else {
                               player.sendMessage(ChatColor.RED + "Too many arguments.");
                                
                               return true;
                           }


                           if (WolfManager.hasWolf(wolf) && wolf != null) {
                               org.halvors.lupi.wolf.Wolf wolf1 = WolfManager.getWolf(wolf);

                               player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " has been released.");
                                
                               WolfManager.releaseWolf(wolf);
                           }

                           return true;
                       }
                   } else {
                        if (Lupi.hasPermissions(player, "Wolf.help")) {
                            showHelp(player, label);

                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage("Sorry but these commands are for in-game players only.");
            }
        
        return true;
    }

    private void showPlayerWolves(Player player) {
        List<org.halvors.lupi.wolf.Wolf> wolves = WolfManager.getWolves(player);
        
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

    private void showWolves(Player player) {
        List<org.halvors.lupi.wolf.Wolf> wolves = WolfManager.getWolves(player.getWorld());
        
        if (!wolves.isEmpty()) {
        	player.sendMessage(ChatColor.YELLOW + "Wolves (" + ChatColor.WHITE + wolves.size() + ChatColor.YELLOW + ")");
        	
            for (org.halvors.lupi.wolf.Wolf wolf : wolves) {
                player.sendMessage(ChatColor.YELLOW + wolf.getName() + ChatColor.WHITE + " - " + wolf.getOwner().getName());
            }
        } else {
            player.sendMessage(ChatColor.RED + "There are no tamed wolves.");
        }
    }
    
    private void showHelp(Player player, String label) {
        String command = "/" + label + " ";
        
        player.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

        if (Lupi.hasPermissions(player, "Wolf.help")) {
            player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.list")) {
            player.sendMessage(command + "list" + ChatColor.YELLOW + " - Show a list of tamed wolves.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.name")) {
            player.sendMessage(command + "name " + ChatColor.YELLOW + " - Show your wolf's name.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.setname")) {
            player.sendMessage(command + "setname " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Set your wolf's name.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.call")) {
            player.sendMessage(command + "call " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Call your wolf.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.stop")) {
            player.sendMessage(command + "stop " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Stop your wolf from attacking.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.give")) {
            player.sendMessage(command + "give " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + "> [" + ChatColor.WHITE + "player" + ChatColor.GREEN + "]" + ChatColor.YELLOW + " - Release your wolf.");
        }
        
        if (Lupi.hasPermissions(player, "Wolf.wolf.release")) {
            player.sendMessage(command + "release " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Release your wolf.");
        }
    }
    
    /**
     * Get the best matching player.
     * 
     * @param name
     * @return
     */
    public Player getPlayer(String name) {
    	return plugin.getServer().matchPlayer(name).get(0);
    }
}
