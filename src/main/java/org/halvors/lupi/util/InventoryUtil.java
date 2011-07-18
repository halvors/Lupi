package org.halvors.lupi.util;

import org.bukkit.inventory.ItemStack;

public class InventoryUtil {
	/*
	public static ItemStack toItemStack(net.minecraft.server.ItemStack item) {
		return new CraftItemStack(1, item.getItem());
	}
	*/
	
	public static net.minecraft.server.ItemStack fromItemStack(ItemStack item) {
		return new net.minecraft.server.ItemStack(item.getTypeId(), item.getAmount(), item.getDurability());
	}
}
