package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public interface Register
{
	
	/**
	 * Registers the entitites.
	 * @return {@code true} on success
	 */
	public boolean registerEntities();

	/**
	 * Spawns a new sheep.
	 * @param m plugin.
	 * @param arena the arena.
	 * @param t the location.
	 * @param i color index.
	 * @return the falling block/ sheep.
	 */
	public FallingBlock spawnSheep(Plugin m, String arena, Location t, final Integer i);

	/**
	 * Spawns a new wool.
	 * @param m plugin.
	 * @param arena the arena.
	 * @param t the location.
	 * @param i color index.
	 * @return the falling block/ wool.
	 */
	public FallingBlock spawnWool(Plugin m, String arena, Location t, final Integer i);

	/**
	 * Checks if this is a snake sheep
	 * @param entity
	 * @return
	 */
	public boolean isSheep(LivingEntity entity);
	
}
