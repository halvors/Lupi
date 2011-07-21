package org.halvors.lupi.util;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
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
