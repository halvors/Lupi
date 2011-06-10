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

package com.halvors.Wolf.wolf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.halvors.Wolf.chest.TileEntityVirtualChest;

/**
 * Represents a WolfInventory.
 * 
 * @author halvors
 */
public class WolfInventory {
    private final TileEntityVirtualChest inventory;
    private final UUID uniqueId;
    
    public WolfInventory(UUID uniqueId) {
        this.inventory = new TileEntityVirtualChest();
        this.uniqueId = uniqueId;
    }

    public TileEntityVirtualChest getInventory() {
        return inventory;
    }
    
    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getSize() {
        return inventory.getSize();
    }

    public String getName() {
        return inventory.getName();
    }

    public ItemStack getItem(int index) {
        return new CraftItemStack(inventory.getItem(index));
    }

    public List<ItemStack> getContents() {
        List<ItemStack> items = new ArrayList<ItemStack>(getSize());
        net.minecraft.server.ItemStack[] mcItems = inventory.getContents();

        for (int i = 0; i < mcItems.length; i++) {
            items.add(mcItems[i] == null ? null : new CraftItemStack(mcItems[i]));
        }

        return items;
    }

    public void setContents(List<ItemStack> items) {
        if (inventory.getContents().length != items.size()) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getContents().length);
        }

        net.minecraft.server.ItemStack[] mcItems = getInventory().getContents();

        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            
            if (item == null || item.getTypeId() <= 0) {
                mcItems[i] = null;
            } else {
                mcItems[i] = new net.minecraft.server.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
            }
        }
    }

    public void setItem(int index, ItemStack item) {
        inventory.setItem(index, (item == null ? null : new net.minecraft.server.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability())));
    }

    public boolean contains(int materialId) {
        for (ItemStack item: getContents()) {
            if (item != null && item.getTypeId() == materialId) {
                return true;
            }
        } 
        
        return false;
    }

    public boolean contains(Material material) {
        return contains(material.getId());
    }

    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        
        for (ItemStack i: getContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        
        return false;
    }

    public boolean contains(int materialId, int amount) {
        int amt = 0;
        
        for (ItemStack item : getContents()) {
            if (item != null && item.getTypeId() == materialId) {
                amt += item.getAmount();
            }
        }
        
        return amt >= amount;
    }

    public boolean contains(Material material, int amount) {
        return contains(material.getId(), amount);
    }

    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        
        int amt = 0;
        
        for (ItemStack i : getContents()) {
            if (item.equals(i)) {
                amt += item.getAmount();
            }
        }
        
        return amt >= amount;
    }

    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        List<ItemStack> inventory = getContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack item = inventory.get(i);
            
            if (item != null && item.getTypeId() == materialId) {
                slots.put(i, item);
            }
        }
        
        return slots;
    }

    public HashMap<Integer, ItemStack> all(Material material) {
        return all(material.getId());
    }

    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
        
        if (item != null) {
            List<ItemStack> inventory = getContents();
            
            for (int i = 0; i < inventory.size(); i++) {
                if (item.equals(inventory.get(i))) {
                    slots.put(i, inventory.get(i));
                }
            }
        }
        
        return slots;
    }

    public int first(int materialId) {
        List<ItemStack> inventory = getContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack item = inventory.get(i);
            
            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }
        
        return -1;
    }

    public int first(Material material) {
        return first(material.getId());
    }

    public int first(ItemStack item) {
        if (item == null) {
            return -1;
        }
        
        List<ItemStack> inventory = getContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            if (item.equals(inventory.get(i))) {
                return i;
            }
        }
        
        return -1;
    }

    public int firstEmpty() {
        List<ItemStack> inventory = getContents();
        
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) == null) {
                return i;
            }
        }
        
        return -1;
    }

    public int firstPartial(int materialId) {
        List<ItemStack> inventory = getContents();
        
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
        List<ItemStack> inventory = getContents();
        
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

    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

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
                            setItem(firstFree, new CraftItemStack(item.getTypeId(), getMaxItemStack(), item.getDurability()));
                            item.setAmount(item.getAmount() - getMaxItemStack());
                        } else {
                            // Just store it
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

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

    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer,ItemStack> leftover = new HashMap<Integer,ItemStack>();

        // TODO: optimization

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
                    ItemStack itemStack = getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
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
        return inventory.getMaxStackSize();
    }

    public void remove(int materialId) {
        List<ItemStack> items = getContents();
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).getTypeId() == materialId) {
                clear(i);
            }
        }
    }

    public void remove(Material material) {
        remove(material.getId());
    }

    public void remove(ItemStack item) {
        List<ItemStack> items = getContents();
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).equals(item)) {
                clear(i);
            }
        }
    }

    public void clear(int index) {
        setItem(index, null);
    }

    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }
    
    public void fillFromDBTable(String[] rows)  {
    	String[] chestRow1 = rows[0].split(";");
    	String[] chestRow2 = rows[1].split(";");
    	String[] chestRow3 = rows[2].split(";");
    	
    	for (int i = 0; i < getSize(); i++) {
    		String[] item = null;
    		
    		if (i >= 0 && i <= 8) {
    			item = chestRow1[i].split(":");
    		} else if (i >= 9 && i <= 17) {
    			item = chestRow2[i-9].split(":");
    		} else if (i >= 18 && i <= 26) {
    			item = chestRow3[i-18].split(":");
    		}
    		
    		int typeId =  (item[0] == null ? 0 : Integer.valueOf(item[0]));
    		short damage = (item[1] == null ? -1 : Short.valueOf(item[1]));
    		int amount = (item[2] == null ? 0 : Integer.valueOf(item[2]));
    		
    		if (typeId < 1 || damage < 0 || amount < 1) {
    			setItem(i, null);
    		} else {
    			setItem(i, new ItemStack(typeId,amount,damage));
    		}
    	}
    }
    
    public String getStackString(ItemStack stack) {
    	String out = null;
    	
    	if (stack == null || stack.getAmount() < 1 || stack.getDurability() < 0 || stack.getTypeId() < 1) {
    		out = "0:-1:0";
    	} else {
    		out = stack.getTypeId() + ":" + stack.getDurability() + ":" + stack.getAmount();
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
    			stack = getStackString(getItem(i)) + ";";
    		} else {
    			stack = getStackString(getItem(i));
    		}
    		
    		if (i >= 0 && i <= 8) {
    				rows[0] += stack;
    		} else if (i >= 9 && i <= 17) {
    				rows[1] += stack;
    		} else if (i >= 18 && i <= 26) {
    				rows[2] += stack;
    		}
    	}
    	
    	// Debug Line
    	//System.out.println("\nrow1: " + rows[0] + "\nrow2: " + rows[1] + "\nrow3: " + rows[2]);
    	
    	return rows;
    }
}
