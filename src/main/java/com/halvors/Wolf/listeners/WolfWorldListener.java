package com.halvors.Wolf.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import com.halvors.Wolf.wolf.WolfManager;
import com.halvors.Wolf.wolf.WolfTable;

public class WolfWorldListener extends WorldListener{

	//private final com.halvors.Wolf.Wolf plugin;
	
	private final WolfManager wolfManager;
	
	public WolfWorldListener(final com.halvors.Wolf.Wolf plugin) {
		//this.plugin = plugin;
		this.wolfManager = plugin.getWolfManager();
	}

	public void onChunkLoad(ChunkLoadEvent event)
	{
		Entity cEntity[] = event.getChunk().getEntities();
		for (Entity entity : cEntity)
		{
			if (entity instanceof Wolf)
			{
				Wolf wolf = (Wolf)entity;
				if (wolf.isTamed())
				{
					WolfTable wt = null;
					wt = wolfManager.getWolf(wolf.getLocation());
					if (wt != null)
					{
						wt.setEntityId(wolf.getEntityId());
						wolfManager.updateWolf(wt);
					}
				}
			}
		}
	}

	public void onChunkUnload(ChunkUnloadEvent event) {
		Entity cEntity[] = event.getChunk().getEntities();
		for (Entity entity : cEntity)
		{
			if (entity instanceof Wolf)
			{
				Wolf wolf = (Wolf)entity;
				if (wolf.isTamed())
				{
					WolfTable wt = null;
					wt = wolfManager.getWolfTable(wolf.getEntityId());
					if (wt != null)
					{
						wt.setLocationX(wolf.getLocation().getX());
						wt.setLocationY(wolf.getLocation().getY());
						wt.setLocationZ(wolf.getLocation().getZ());
						wolfManager.updateWolf(wt);
					}
				}
			}
		}
	}	
	
	
}
