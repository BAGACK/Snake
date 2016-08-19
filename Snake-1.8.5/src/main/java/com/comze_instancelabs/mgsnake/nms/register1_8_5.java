package com.comze_instancelabs.mgsnake.nms;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import com.comze_instancelabs.minigamesapi.MinigamesAPI;

import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.EnumColor;

public class register1_8_5 implements Register {
	public boolean registerEntities(){
		
		try {
			Class entityTypeClass = EntityTypes.class;

			Field c = entityTypeClass.getDeclaredField("c");
			c.setAccessible(true);
			HashMap c_map = (HashMap) c.get(null);
			c_map.put("MESheep", MESheep1_8_5.class);
			c_map.put("MEWool", MEFallingBlock1_8_5.class);

			Field d = entityTypeClass.getDeclaredField("d");
			d.setAccessible(true);
			HashMap d_map = (HashMap) d.get(null);
			d_map.put(MESheep1_8_5.class, "MESheep");
			d_map.put(MEFallingBlock1_8_5.class, "MEWool");

			Field e = entityTypeClass.getDeclaredField("e");
			e.setAccessible(true);
			HashMap e_map = (HashMap) e.get(null);
			e_map.put(Integer.valueOf(91), MESheep1_8_5.class);
			e_map.put(Integer.valueOf(21), MEFallingBlock1_8_5.class);

			Field f = entityTypeClass.getDeclaredField("f");
			f.setAccessible(true);
			HashMap f_map = (HashMap) f.get(null);
			f_map.put(MESheep1_8_5.class, Integer.valueOf(91));
			f_map.put(MEFallingBlock1_8_5.class, Integer.valueOf(21));

			Field g = entityTypeClass.getDeclaredField("g");
			g.setAccessible(true);
			HashMap g_map = (HashMap) g.get(null);
			g_map.put("MESheep", Integer.valueOf(91));
			g_map.put("MEWool", Integer.valueOf(21));

			return true;
		} catch (Exception ex) {
			MinigamesAPI.getAPI().getLogger().log(Level.WARNING, "exception", ex);
			return false;
		}
	}
	
	
	public FallingBlock spawnSheep(Plugin m, String arena, Location t, final Integer color) {
		final Object w = ((CraftWorld) t.getWorld()).getHandle();
		final MESheep1_8_5 t_ = new MESheep1_8_5(arena, t, (net.minecraft.server.v1_8_R2.World) ((CraftWorld) t.getWorld()).getHandle());

		Bukkit.getScheduler().runTask(m, new Runnable(){
			public void run(){
				//t_.id = Block.e(35);
				//t_.data = color;
				((net.minecraft.server.v1_8_R2.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
				t_.setColor(EnumColor.fromColorIndex(color));
				t_.setGoalTarget(null);
			}
		});
		
		return t_;
	}
	
	public FallingBlock spawnWool(Plugin m, String arena, Location t, final Integer color) {
		final Object w = ((CraftWorld) t.getWorld()).getHandle();
		final MEFallingBlock1_8_5 t_ = new MEFallingBlock1_8_5(arena, t, (net.minecraft.server.v1_8_R2.World) ((CraftWorld) t.getWorld()).getHandle(), color);

		Bukkit.getScheduler().runTask(m, new Runnable(){
			public void run(){
				((net.minecraft.server.v1_8_R2.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
			}
		});
		
		return t_;
	}

	@Override
	public boolean isSheep(LivingEntity entity) {
		return ((CraftLivingEntity) entity).getHandle() instanceof MESheep1_8_5;
	}
}
