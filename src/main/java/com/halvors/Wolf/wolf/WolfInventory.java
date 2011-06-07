package com.halvors.Wolf.wolf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private final int wolfId;
    
    public WolfInventory(int wolfId) {
        this.inventory = new TileEntityVirtualChest();
        this.wolfId = wolfId;
    }

    public TileEntityVirtualChest getInventory() {
        return inventory;
    }
    
    public int getWolfId() {
    	return wolfId;
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
}
