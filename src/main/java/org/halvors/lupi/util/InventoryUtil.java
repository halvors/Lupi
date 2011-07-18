package org.halvors.lupi.util;

import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
	/**
	 * Convert to ItemStack.
	 * 
	 * @param items
	 * @return
	 */
	/*
	public static ItemStack toItemStack(net.minecraft.server.ItemStack items) {
		return new CraftItemStack(items.getItem());
	}
	*/
	
	/**
	 * Convert from ItemStack.
	 * 
	 * @param items
	 * @return
	 */
	public static net.minecraft.server.ItemStack fromItemStack(ItemStack items) {
		return new net.minecraft.server.ItemStack(items.getTypeId(), items.getAmount(), items.getDurability());
	}
}
