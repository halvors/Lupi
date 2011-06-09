/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
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

package com.halvors.Wolf.command;

import java.util.List;
import java.util.UUID;

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
 * Represents a CommandExecutor
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
                if (plugin.hasPermissions(player, "Wolf.list")) {
                    showWolves(player);
                    
                    return true;
                }
            } else {
                String subCommand = args[0];
            
                if (subCommand.equalsIgnoreCase("help")) {
                    if (plugin.hasPermissions(player, "Wolf.help")) {
                        showHelp(player, label);
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("list")) {
                    if (plugin.hasPermissions(player, "Wolf.list")) {
                        showList(player);
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("name")) {
                    if (plugin.hasPermissions(player, "Wolf.name")) {
                        String name = player.getName();
                        
                        if (selectedWolfManager.hasSelectedWolf(name)) {
                            Wolf wolf = (Wolf) selectedWolfManager.getSelectedWolf(name);
                            
                            if (wolfManager.hasWolf(wolf)) {
                            	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                            	
                                player.sendMessage("This is: " + ChatColor.YELLOW + wolf1.getName());
                            } else {
                                // TODO: Some error message here.
                            }
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("setname")) {
                    if (plugin.hasPermissions(player, "Wolf.setname")) {
                        String owner = player.getName();
                        
                        if (selectedWolfManager.hasSelectedWolf(player.getName())) {
                            Wolf wolf = (Wolf) selectedWolfManager.getSelectedWolf(owner);
                            
                            if (wolfManager.hasWolf(wolf)) {
                            	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                                String name = null;
                            
                                if (args.length >= 2) {
                                    name = args[1];
                                } else {
                                    name = wolfManager.getRandomName();
                                }
                                
                                wolf1.setName(name);
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "No wolf selected.");
                        }

                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("call")) {
                    if (plugin.hasPermissions(player, "Wolf.call")) {
                        if (args.length == 1) {
                            String name = args[1];
                            String owner = player.getName();
                            
                            if (wolfManager.hasWolf(name, owner)) {
                                Wolf wolf = (Wolf) wolfManager.getWolf(name, owner);
                                wolf.teleport(player);
                                
                                player.sendMessage(ChatColor.GREEN + "Your wolf is comming.");
                            }
                        }
                    
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("stop")) {
                    if (plugin.hasPermissions(player, "Wolf.stop")) {
                        if (args.length >= 1) {
                            String name = args[1];
                            String owner = player.getName();
                            
                            if (wolfManager.hasWolf(name, player.getName())) {
                                Wolf wolf = (Wolf) wolfManager.getWolf(name, owner);
                                wolf.teleport(player);
                                
                                player.sendMessage(ChatColor.GREEN + "Your wolf has stopped the attack.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "No wolf specified");
                        }
                    
                        return true;
                    }
                /*
                } else if (subCommand.equalsIgnoreCase("target")){
                    if (plugin.hasPermissions(player, "Wolf.target")) {
                        if (args.length == 2) {
                            Player target = plugin.getServer().getPlayer(args[1]);
                            
                            if (target != null) {
                                
                            } else {
                                player.sendMessage(ChatColor.RED + "Target doesn't exist.");
                            }
                        }
                        
                        return true;
                    }
                */
                } else if (subCommand.equalsIgnoreCase("give")) {
                    if (plugin.hasPermissions(player, "Wolf.give")) {
                        Wolf wolf = null;
                        String owner = player.getName();
                        Player receiver = null;
                        
                        if (selectedWolfManager.hasSelectedWolf(owner) && args.length <= 1) {
                            wolf = (Wolf) selectedWolfManager.getSelectedWolf(owner);
                            receiver = (Player) plugin.getServer().getPlayer(args[1]);
                        } else {
                            wolf = (Wolf) wolfManager.getWolf(args[1], owner);
                            receiver = (Player) plugin.getServer().getPlayer(args[2]);
                        }
                        
                        if (wolf != null && receiver != null && wolfManager.hasWolf(wolf)) {
                        	com.halvors.Wolf.wolf.Wolf wolf1 = wolfManager.getWolf(wolf);
                            String name = wolf1.getName();
                            
                            wolf1.setOwner(receiver);
                            
                            wolf.teleport(receiver);
                                
                            player.sendMessage(ChatColor.YELLOW + name + ChatColor.WHITE + " is now given to " + ChatColor.YELLOW + receiver.getName());
                            receiver.sendMessage("You have got the wolf " + ChatColor.YELLOW + name + ChatColor.WHITE + " from " + ChatColor.YELLOW + owner);
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("release")) {
                    if (plugin.hasPermissions(player, "Wolf.release")) {
                        String owner = player.getName();
                        
                        if (args.length == 1) {
                            if (selectedWolfManager.hasSelectedWolf(owner)) {
                                wolfManager.releaseWolf(selectedWolfManager.getSelectedWolf(owner));
                                
                                player.sendMessage(ChatColor.GREEN + "Your wolf has been released.");
                            } else {
                                player.sendMessage(ChatColor.RED + "No wolf selected.");
                            }
                        } else {
                            String name = args[2];

                            if (wolfManager.hasWolf(name, owner)) {
                                Wolf wolf = (Wolf) wolfManager.getWolf(name, owner);
                                wolfManager.releaseWolf(wolf);
                                
                                player.sendMessage(ChatColor.YELLOW + name + ChatColor.GREEN + " has been released.");
                            } else {
                                player.sendMessage(ChatColor.RED + "Wolf does not exists.");
                            }
                        }
                        
                        return true;
                    }
                } else if (subCommand.equalsIgnoreCase("item")) {
                    if (plugin.hasPermissions(player, "Wolf.item")) {
                        int item = worldConfig.item;
                        
                        if (item != 0) {
                            player.getInventory().addItem(new ItemStack(item, 1));
                            player.sendMessage(ChatColor.GREEN + "You got the wolf item.");
                        }
                        
                        return true;
                    }
                } else {
                    if (plugin.hasPermissions(player, "Wolf.help")) {
                        showHelp(player, label);
                        
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    private void showWolves(Player player) {
        List<com.halvors.Wolf.wolf.Wolf> wolves = wolfManager.getWolves(player);
        
        player.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        
        if (!wolves.isEmpty()) {
            for (com.halvors.Wolf.wolf.Wolf wolf1 : wolves) {
                Wolf wolf = wolf1.getWolf();
                Location pos = wolf.getLocation();
                
                player.sendMessage(ChatColor.YELLOW + wolf1.getName() + ChatColor.WHITE + " (" + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() + ")");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You have no wolves.");
        }
    }

    private void showList(Player player) {
        List<WolfTable> wolfTables = wolfManager.getWolfTables();
        
        player.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        
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
        
        player.sendMessage(ChatColor.GREEN + plugin.getName() + ChatColor.GREEN + " (" + ChatColor.WHITE + plugin.getVersion() + ChatColor.GREEN + ")");
        player.sendMessage(ChatColor.RED + "[]" + ChatColor.WHITE + " Required, " + ChatColor.GREEN + "<>" + ChatColor.WHITE + " Optional.");

        if (plugin.hasPermissions(player, "Wolf.help")) {
            player.sendMessage(command + "help" + ChatColor.YELLOW + " - Show help.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.list")) {
            player.sendMessage(command + "list" + ChatColor.YELLOW + " - Show a list of tamed wolves.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.name")) {
            player.sendMessage(command + "name " + ChatColor.YELLOW + " - Show your wolf's name.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.setname")) {
            player.sendMessage(command + "setname " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Set your wolf's name.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.call")) {
            player.sendMessage(command + "call " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Call your wolf.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.stop")) {
            player.sendMessage(command + "stop " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name"  + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Stop your wolf from attacking.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.release")) {
            player.sendMessage(command + "release " + ChatColor.GREEN + "<" + ChatColor.WHITE + "name" + ChatColor.GREEN + ">" + ChatColor.YELLOW + " - Release your wolf.");
        }
        
        if (plugin.hasPermissions(player, "Wolf.item")) {
            player.sendMessage(command + "item" + ChatColor.YELLOW + " - Give you the wolf item.");
        }
    }
}