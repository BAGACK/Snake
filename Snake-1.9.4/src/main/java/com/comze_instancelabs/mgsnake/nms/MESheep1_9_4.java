package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.EntityComplexPart;
import net.minecraft.server.v1_9_R2.EntitySheep;
import net.minecraft.server.v1_9_R2.World;

public class MESheep1_9_4 extends EntitySheep implements FallingBlock {

	private String arena;

	public MESheep1_9_4(String arena, Location loc, World world) {
		super(world);
		this.arena = arena;
		setPosition(loc.getX(), loc.getY(), loc.getZ());
	}

	public void setYaw(Location l2) {
//		final Location l1 = this.getBukkitEntity().getLocation();
//		l1.setDirection(l1.toVector().subtract(l2.toVector()));
		this.h(l2.getYaw());
		this.yaw = l2.getYaw();
	}

	@Override
	public void m() {
		motY = 0;
		move(motX, motY, motZ);
	}

	/*
	 * @Override public void g(double x, double y, double z) {
	 * 
	 * }
	 */

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

	public org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity getBukkitEntity() {
		return super.getBukkitEntity();
	}

	public Vector toVector() {
		return getBukkitEntity().getLocation().toVector();
	}

	public void setVelocity(Vector vel) {
		getBukkitEntity().setVelocity(vel);
	}

}