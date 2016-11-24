package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.EnumColor;
import net.minecraft.server.v1_11_R1.MinecraftKey;

public class register1_11 implements Register {
	public boolean registerEntities(){
		
		MinecraftKey localMinecraftKey = new MinecraftKey("MESheep");
		EntityTypes.b.a(91, localMinecraftKey, MESheep1_11.class);
		EntityTypes.d.add(localMinecraftKey);
		
		return true;
	}
	
	
	public FallingBlock spawnSheep(Plugin m, String arena, Location t, final Integer color) {
		final Object w = ((CraftWorld) t.getWorld()).getHandle();
		final MESheep1_11 t_ = new MESheep1_11(arena, t, (net.minecraft.server.v1_11_R1.World) ((CraftWorld) t.getWorld()).getHandle());

		Bukkit.getScheduler().runTask(m, new Runnable(){
			public void run(){
				//t_.id = Block.e(35);
				//t_.data = color;
				((net.minecraft.server.v1_11_R1.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
				t_.setColor(EnumColor.fromColorIndex(color));
				t_.setGoalTarget(null);
			}
		});
		
		return t_;
	}
	
	public FallingBlock spawnWool(Plugin m, String arena, Location t, final Integer color) {
		final Object w = ((CraftWorld) t.getWorld()).getHandle();
		final MEFallingBlock1_11 t_ = new MEFallingBlock1_11(arena, t, (net.minecraft.server.v1_11_R1.World) ((CraftWorld) t.getWorld()).getHandle(), color);

		Bukkit.getScheduler().runTask(m, new Runnable(){
			public void run(){
				((net.minecraft.server.v1_11_R1.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
			}
		});
		
		return t_;
	}

	@Override
	public boolean isSheep(LivingEntity entity) {
		return ((CraftLivingEntity) entity).getHandle() instanceof MESheep1_11;
	}
	
}
