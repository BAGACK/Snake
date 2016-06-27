package com.comze_instancelabs.mgsnake.nms;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public interface FallingBlock
{

	Vector toVector();

	void setYaw(Location location);

	void setVelocity(Vector multiply);
	
	void die();

}