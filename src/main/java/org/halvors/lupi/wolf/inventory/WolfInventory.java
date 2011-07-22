/*
 * Copyright (C) 2011 halvors <halvors@skymiastudios.com>
 * Copyright (C) 2011 speeddemon92 <speeddemon92@gmail.com>
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

package org.halvors.lupi.wolf.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.TileEntityChest;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a WolfInventory.
 * 
 * @author speeddemon92
 */
public class WolfInventory extends TileEntityChest {
	private String name;
    private UUID uniqueId;
    
    public WolfInventory(UUID uniqueId) {
        super();
        setUniqueId(uniqueId);
    }
    
    public WolfInventory(UUID uniqueId, String name) {
        super();
        setUniqueId(uniqueId);
        setName(name);
    }
    
    /**
     * Get the uniqueId
     * 
     * @return
     */
    public UUID getUniqueId() {
        return uniqueId;
    }
    
    /**
     * Set the uniqueId
     * 
     * @param uniqueId
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    /**
     * Return the name of the inventory
     *
     * @return The inventory name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of the inventory
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public int getSize() {
        return super.getSize();
    }
    
    public ItemStack getBukkitItem(int index) {
        return new CraftItemStack(super.getItem(index));
    }
    
    public void setBukkitItem(int index, ItemStack item) {
        super.setItem(index, (item == null ? null : new net.minecraft.server.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability())));
    }
    
    /**
     * Get all ItemStacks from the inventory
     *
     * @return All the ItemStacks from all slots
     */
    public List<ItemStack> getBukkitContents() {
        List<ItemStack> items = new ArrayList<ItemStack>(getSize());
        List<net.minecraft.server.ItemStack> mcItems = Arrays.asList(super.getContents());

        for (int i = 0; i < mcItems.size(); i++) {
            items.add(mcItems.get(i) == null ? null : new CraftItemStack(mcItems.get(i)));
        }

        return items;
    }
    
    /**
     * Set the inventory's contents
     *
     * @return All the ItemStacks from all slots
     */
    public void setBukkitContents(List<ItemStack> items) {
        if (super.getContents().length != items.size()) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + super.getContents().length);
        }

        List<net.minecraft.server.ItemStack> mcItems = Arrays.asList(super.getContents());

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            
            if (item == null || item.getTypeId() <= 0) {
                mcItems.set(i, null);
            } else {
                mcItems.add(new net.minecraft.server.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability()));
            }
        }
    }
    
    /**
     * Check if the inventory contains any ItemStacks with the given materialId
     *
     * @param materialId The materialId to check for
     * @return If any ItemStacks were found
     */
    public boolean contains(int materialId) {
        for (ItemStack item : getBukkitContents()) {
            if (item != null && item.getTypeId() == materialId) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if the inventory contains any ItemStacks with the given material
     *
     * @param material The material to check for
     * @return If any ItemStacks were found
     */
    public boolean contains(Material material) {
        return contains(material.getId());
    }
    
    /**
     * Check if the inventory contains any ItemStacks matching the given ItemStack
     * This will only match if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return If any matching ItemStacks were found
     */
    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        
        for (ItemStack i : getBukkitContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if the inventory contains any ItemStacks with the given materialId and at least the minimum amount specified
     *
     * @param materialId The materialId to check for
     * @param amount The minimum amount to look for
     * @return If any ItemStacks were found
     */
    public boolean contains(int materialId, int amount) {
        int amt = 0;
        
        for (ItemStack item : getBukkitContents()) {
            if (item != null && item.getTypeId() == materialId) {
                amt += item.getAmount();
            }
        }
        
        return amt >= amount;
    }
    
    /**
     * Check if the inventory contains any ItemStacks with the given material and at least the minimum amount specified
     *
     * @param material The material to check for
     * @return If any ItemStacks were found
     */
    public boolean contains(Material material, int amount) {
        return contains(material.getId(), amount);
    }
    
    /**
     * Check if the inventory contains any ItemStacks matching the given ItemStack and at least the minimum amount specified
     * This will only match if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return If any matching ItemStacks were found
     */
    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        
        int amt = 0;
        
        for (ItemStack i : getBukkitContents()) {
            if (item.equals(i)) {
                amt += item.getAmount();
            }
        }
        
        return amt >= amount;
    }
    
    /**
     * Find all slots in the inventory containing any ItemStacks with the given materialId
     *
     * @param materialId The materialId to look for
     * @return The Slots found.
     */
    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();

        List<ItemStack> inventory = getBukkitContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack item = inventory.get(i);
            if (item != null && item.getTypeId() == materialId) {
                slots.put(i, item);
            }
        }
        
        return slots;
    }
    
    /**
     * Find all slots in the inventory containing any ItemStacks with the given material
     *
     * @param materialId The material to look for
     * @return The Slots found.
     */
    public HashMap<Integer, ItemStack> all(Material material) {
        return all(material.getId());
    }
    
    /**
     * Find all slots in the inventory containing any ItemStacks with the given ItemStack
     * This will only match slots if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The Slots found.
     */
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        
        if (item != null) {
            List<ItemStack> inventory = getBukkitContents();
            
            for (int i = 0; i < inventory.size(); i++) {
                if (item.equals(inventory.get(i))) {
                    slots.put(i, inventory.get(i));
                }
            }
        }
        
        return slots;
    }
    
    /**
     * Find the first slot in the inventory containing an ItemStack with the given materialId
     *
     * @param materialId The materialId to look for
     * @return The Slot found.
     */
    public int first(int materialId) {
        List<ItemStack> inventory = getBukkitContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack item = inventory.get(i);
            
            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Find the first slot in the inventory containing an ItemStack with the given material
     *
     * @param materialId The material to look for
     * @return The Slot found.
     */
    public int first(Material material) {
        return first(material.getId());
    }
    
    /**
     * Find the first slot in the inventory containing an ItemStack with the given stack
     * This will only match a slot if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The Slot found.
     */
    public int first(ItemStack item) {
        if (item == null) {
            return -1;
        }
        
        List<ItemStack> inventory = getBukkitContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            if (item.equals(inventory.get(i))) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Find the first empty Slot.
     *
     * @return The first empty Slot found.
     */
    public int firstEmpty() {
        List<ItemStack> inventory = getBukkitContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) == null) {
                return i;
            }
        }
        
        return -1;
    }
    
    public int firstPartial(int materialId) {
        List<ItemStack> inventory = getBukkitContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack item = inventory.get(i);
            if (item != null && item.getTypeId() == materialId && item.getAmount() < item.getMaxStackSize()) {
                return i;
            }
        }
        
        return -1;
    }

    public int firstPartial(Material material) {
        return firstPartial(material.getId());
    }

    public int firstPartial(ItemStack item) {
        List<ItemStack> inventory = getBukkitContents();
        
        if (item == null) {
            return -1;
        }
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack cItem = inventory.get(i);
            if (cItem != null && cItem.getTypeId() == item.getTypeId() && cItem.getAmount() < cItem.getMaxStackSize() && cItem.getDurability() == item.getDurability()) {
                return i;
            }
        }
        
        return -1;
    }
    
    /**
     * Stores the given ItemStacks in the inventory.
     *
     * This will try to fill existing stacks and empty slots as good as it can.
     * It will return a HashMap of what it couldn't fit.
     *
     * @param items The ItemStacks to add
     * @return
     */
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxItemStack()) {
                            setBukkitItem(firstFree, new CraftItemStack(item.getTypeId(), getMaxItemStack(), item.getDurability()));
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                        	setBukkitItem(firstFree, item);
                        	
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getBukkitItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        
        return leftover;
    }

    /**
     * Removes the given ItemStacks from the inventory.
     *
     * It will try to remove 'as much as possible' from the types and amounts you
     * give as arguments. It will return a HashMap of what it couldn't remove.
     *
     * @param items The ItemStacks to remove
     * @return
     */
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                int first = first(item.getType());

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    
                    break;
                } else {
                    ItemStack itemStack = getBukkitItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        setBukkitItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        
        return leftover;
    }

    private int getMaxItemStack() {
        return super.getMaxStackSize();
    }
    
    /**
     * Remove all stacks in the inventory matching the given materialId.
     *
     * @param materialId The material to remove
     */
    public void remove(int materialId) {
        List<ItemStack> items = getBukkitContents();
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).getTypeId() == materialId) {
                clear(i);
            }
        }
    }
    
    /**
     * Remove all stacks in the inventory matching the given material.
     *
     * @param material The material to remove
     */
    public void remove(Material material) {
        remove(material.getId());
    }
    
    /**
     * Remove all stacks in the inventory matching the given stack.
     * This will only match a slot if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     */
    public void remove(ItemStack item) {
        List<ItemStack> items = getBukkitContents();
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).equals(item)) {
                clear(i);
            }
        }
    }
    
    /**
     * Clear out a particular slot in the index
     *
     * @param index The index to empty.
     */
    public void clear(int index) {
        super.setItem(index, null);
    }
    
    /**
     * Clear out the whole index
     */
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }
    
    public void fillFromDBTable(String[] rows) {
        String[] chestRow1 = rows[0].split(";");
        String[] chestRow2 = rows[1].split(";");
        String[] chestRow3 = rows[2].split(";");
       
        System.out.println("fillFromDBTable: " + uniqueId.toString());
        
        for (int i = 0; i < getSize(); i++) {
            String[] item = null;
       
            if (i >= 0 && i <= 8) {
                item = chestRow1[i].split(":");
            } else if (i >= 9 && i <= 17) {
                item = chestRow2[i-9].split(":");
            } else if (i >= 18 && i <= 26) {
                item = chestRow3[i-18].split(":");
            }
       
            int typeId = (item[0] == null ? 0 : Integer.valueOf(item[0]));
            short damage = (item[1] == null ? -1 : Short.valueOf(item[1]));
            int amount = (item[2] == null ? 0 : Integer.valueOf(item[2]));
       
            if (typeId < 1 || damage < 0 || amount < 1) {
            	setBukkitItem(i, null); // TODO: Don't know what this should be.
            } else {
            	setBukkitItem(i, new ItemStack(typeId, amount, damage));
            }
        }
    }
       
    public String getItemStackAsString(ItemStack itemstack) {
        String out = null;
       
        if (itemstack == null || itemstack.getAmount() < 1 || itemstack.getDurability() < 0 || itemstack.getTypeId() < 1) {
            out = "0:-1:0";
        } else {
            out = itemstack.getTypeId() + ":" + itemstack.getDurability() + ":" + itemstack.getAmount();
        }
       
        return out;
    }
       
    public String[] prepareTableForDB() {
        String[] rows = new String[3];
        rows[0] = "";
        rows[1] = "";
        rows[2] = "";
       
        for (int i = 0; i < getSize(); i++) {
            String stack = null;
            
            if ((i + 1) % 9 != 0) {
                stack = getItemStackAsString(getBukkitItem(i)) + ";";
            } else {
                stack = getItemStackAsString(getBukkitItem(i));
            }
            
            if (i >= 0 && i <= 8) {
                rows[0] += stack;
            } else if (i >= 9 && i <= 17) {
                rows[1] += stack;
            } else if (i >= 18 && i <= 26) {
                rows[2] += stack;
            }
        }
        
        return rows;
    }
     
    @Override
    public boolean a_(EntityHuman entityhuman) {
        return true;
    }
}
