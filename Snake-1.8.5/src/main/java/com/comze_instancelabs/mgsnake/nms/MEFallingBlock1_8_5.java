package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R2.Block;
import net.minecraft.server.v1_8_R2.DamageSource;
import net.minecraft.server.v1_8_R2.EntityComplexPart;
import net.minecraft.server.v1_8_R2.EntityFallingBlock;
import net.minecraft.server.v1_8_R2.IBlockData;
import net.minecraft.server.v1_8_R2.World;

public class MEFallingBlock1_8_5 extends EntityFallingBlock implements FallingBlock {

	private String arena;

	public MEFallingBlock1_8_5(String arena, Location loc, World world, Integer color) {
		super(world, loc.getX(), loc.getY(), loc.getZ(), woolData(color));
		this.arena = arena;
	}
	
	private static IBlockData woolData(Integer color)
	{
		return Block.getByCombinedId(35 + (color << 12));
	}


	public void setYaw(Location target) {
		final Location loc = new Location(target.getWorld(), this.locX, this.locY, this.locZ);
		loc.setDirection(target.toVector().subtract(loc.toVector()));
		this.yaw = loc.getYaw();
	}
	@Override
	public void t_() {
		motY = 0;
		move(motX, motY, motZ);
	}

	@Override public void g(double x, double y, double z) {
	 
	}
	

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

	public org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity getBukkitEntity() {
		return super.getBukkitEntity();
	}

	public Vector toVector() {
		return getBukkitEntity().getLocation().toVector();
	}

	public void setVelocity(Vector vel) {
		getBukkitEntity().setVelocity(vel);
	}

}