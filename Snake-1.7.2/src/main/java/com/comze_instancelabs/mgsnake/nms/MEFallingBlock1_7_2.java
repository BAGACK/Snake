package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_7_R1.DamageSource;
import net.minecraft.server.v1_7_R1.EntityComplexPart;
import net.minecraft.server.v1_7_R1.EntityFallingBlock;
import net.minecraft.server.v1_7_R1.World;

public class MEFallingBlock1_7_2 extends EntityFallingBlock implements FallingBlock {

	private String arena;

	public MEFallingBlock1_7_2(String arena, Location loc, World world) {
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
	public void h() {
		motY = 0;
		move(motX, motY, motZ);
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
	
	public org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity getBukkitEntity(){
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