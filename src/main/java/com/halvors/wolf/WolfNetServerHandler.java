package com.halvors.wolf;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet31RelEntityMove;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.halvors.wolf.event.EntityMoveEvent;

public class WolfNetServerHandler extends NetServerHandler {
	private EntityPlayer player;
	
    public WolfNetServerHandler(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
        
        this.player = entityplayer;
    }

    public void a(Packet31RelEntityMove packet) {
    	super.a(packet);
    	
    	WorldServer worldserver = player.b.a(this.player.dimension);
        Entity entity = (Entity) worldserver.getEntity(packet.a);
    	Location location = new Location(null, packet.b, packet.c, packet.d, packet.e, packet.f);
    	
    	EntityMoveEvent event = new EntityMoveEvent(entity, location);
    	Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
