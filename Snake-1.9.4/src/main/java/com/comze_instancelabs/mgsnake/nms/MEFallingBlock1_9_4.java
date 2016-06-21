package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_9_R2.DamageSource;
import net.minecraft.server.v1_9_R2.EntityComplexPart;
import net.minecraft.server.v1_9_R2.EntitySheep;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import net.minecraft.server.v1_9_R2.World;

public class MEFallingBlock1_9_4 extends EntitySheep {

	private boolean onGround = false;
	private String arena;

	public MEFallingBlock1_9_4(String arena, Location loc, World world) {
		super(world);
		this.arena = arena;
		setPosition(loc.getX(), loc.getY(), loc.getZ());
	}

	int X;
	int Y;
	int Z;

	public void setYaw(Location target) {
		double disX = (this.locX - target.getX());
		double disY = (this.locY - target.getY());
		double disZ = (this.locZ - target.getZ());

		this.X = (int) (Math.abs(disX));
		this.Y = (int) (Math.abs(disY));
		this.Z = (int) (Math.abs(disZ));

		if (this.locX <= target.getX()) {
			if (this.locZ >= target.getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 180F;
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) - 90F;
			}
		} else { // (this.locX > target.getX())
			if (this.locZ >= target.getZ()) {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z)) + 90F;
			} else {
				this.yaw = getLookAtYaw(new Vector(this.X, this.Y, this.Z));
			}
		}
	}

	public static float getLookAtYaw(Vector motion) {
		double dx = motion.getX();
		double dz = motion.getZ();
		double yaw = 0;

		if (dx != 0) {
			if (dx < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * 180 / Math.PI - 90);
	}

	@Override
	public MinecraftServer h() {
		motY = 0;
		move(motX, motY, motZ);
		return null;
	}

	/*@Override
	public void g(double x, double y, double z) {

	}*/

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

	public org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity getBukkitEntity() {
		return super.getBukkitEntity();
	}
	
	public Vector toVector()
	{
		return getBukkitEntity().getLocation().toVector();
	}

	public void setVelocity(Vector vel) {
		getBukkitEntity().setVelocity(vel);
	}

}