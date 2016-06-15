package com.comze_instancelabs.mgsnake.nms;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_9_R2.EntityTypes;
import net.minecraft.server.v1_9_R2.EnumColor;

public class register1_9_4 {
	public static boolean registerEntities() {

		try {
			Class entityTypeClass = EntityTypes.class;

			Field c = entityTypeClass.getDeclaredField("c");
			c.setAccessible(true);
			HashMap c_map = (HashMap) c.get(null);
			c_map.put("MESheep", MEFallingBlock1_9_4.class);

			Field d = entityTypeClass.getDeclaredField("d");
			d.setAccessible(true);
			HashMap d_map = (HashMap) d.get(null);
			d_map.put(MEFallingBlock1_9_4.class, "MESheep");

			Field e = entityTypeClass.getDeclaredField("e");
			e.setAccessible(true);
			HashMap e_map = (HashMap) e.get(null);
			e_map.put(Integer.valueOf(91), MEFallingBlock1_9_4.class);

			Field f = entityTypeClass.getDeclaredField("f");
			f.setAccessible(true);
			HashMap f_map = (HashMap) f.get(null);
			f_map.put(MEFallingBlock1_9_4.class, Integer.valueOf(91));

			Field g = entityTypeClass.getDeclaredField("g");
			g.setAccessible(true);
			HashMap g_map = (HashMap) g.get(null);
			g_map.put("MESheep", Integer.valueOf(91));

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static MEFallingBlock1_9_4 spawnSheep(Plugin m, String arena, Location t, final Integer i) {
		final Object w = ((CraftWorld) t.getWorld()).getHandle();
		final MEFallingBlock1_9_4 t_ = new MEFallingBlock1_9_4(arena, t, (net.minecraft.server.v1_9_R2.World) ((CraftWorld) t.getWorld()).getHandle());

		Bukkit.getScheduler().runTask(m, new Runnable() {
			public void run() {
				//t_.id = Block.getById(35);
				//t_.data = color;
				((net.minecraft.server.v1_9_R2.World) w).addEntity(t_, CreatureSpawnEvent.SpawnReason.CUSTOM);
				t_.setColor(EnumColor.fromColorIndex(i));
				t_.setGoalTarget(null);
			}
		});

		return t_;
	}
}
