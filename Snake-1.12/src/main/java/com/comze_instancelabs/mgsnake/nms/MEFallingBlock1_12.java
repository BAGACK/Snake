package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityComplexPart;
import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import net.minecraft.server.v1_12_R1.EnumMoveType;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.World;

public class MEFallingBlock1_12 extends EntityFallingBlock implements FallingBlock {

	private String arena;

	public MEFallingBlock1_12(String arena, Location loc, World world, Integer color) {
		super(world, loc.getX(), loc.getY(), loc.getZ(), woolData(color));
		this.arena = arena;
	}
	
	private static IBlockData woolData(Integer color)
	{
		return Block.getByCombinedId(35 + (color << 12));
	}

	public void setYaw(Location l2) {
//		final Location l1 = this.getBukkitEntity().getLocation();
//		l1.setDirection(l1.toVector().subtract(l2.toVector()));
		this.yaw = l2.getYaw();
	}

	@Override
	public void B_() {
		motY = 0;
		move(EnumMoveType.SELF, motX, motY, motZ);
	}

	
	@Override public void f(double x, double y, double z) {
	 
	}
	

	public boolean damageEntity(DamageSource damagesource, int i) {
		return false;
	}

	public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
		return false;
	}

	public CraftEntity getBukkitEntity() {
		return super.getBukkitEntity();
	}

	public Vector toVector() {
		return getBukkitEntity().getLocation().toVector();
	}

	public void setVelocity(Vector vel) {
		getBukkitEntity().setVelocity(vel);
	}

}