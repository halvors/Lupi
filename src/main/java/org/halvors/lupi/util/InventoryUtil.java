package org.halvors.lupi.util;

import net.minecraft.server.EntityPlayer;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.halvors.lupi.wolf.inventory.WolfInventory;

public class InventoryUtil {
	/**
	 * Open a virtual inventory.
	 * 
	 * @param player
	 * @param inventory
	 */
	public static void openInventory(Player player, WolfInventory inventory) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
    	entityPlayer.a(inventory);
	}
	
	/**
	 * Convert to ItemStack.
	 * 
	 * @param items
	 * @return
	 */
	public static ItemStack toBukkitItemStack(net.minecraft.server.ItemStack items) {
		return new CraftItemStack(items);
	}
	
	/**
	 * Convert from ItemStack.
	 * 
	 * @param items
	 * @return
	 */
	public static net.minecraft.server.ItemStack fromBukkitItemStack(ItemStack items) {
        return new net.minecraft.server.ItemStack(items.getTypeId(), items.getAmount(), items.getDurability());
    }
}
