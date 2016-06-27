package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_10_R1.DamageSource;
import net.minecraft.server.v1_10_R1.EntityComplexPart;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.World;

public class MESheep1_10 extends EntitySheep implements FallingBlock {

	private String arena;

	public MESheep1_10(String arena, Location loc, World world) {
		super(world);
		this.arena = arena;
		setPosition(loc.getX(), loc.getY(), loc.getZ());
	}

	public void setYaw(Location target) {
		final Location loc = new Location(target.getWorld(), this.locX, this.locY, this.locZ);
		loc.setDirection(target.toVector().subtract(loc.toVector()));
		this.yaw = loc.getYaw();
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

	public CraftEntity getBukkitEntity() {
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