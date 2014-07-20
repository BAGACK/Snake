package com.comze_instancelabs.mgsnake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgsnake.nms.MESheep1_7_10;
import com.comze_instancelabs.mgsnake.nms.MESheep1_7_2;
import com.comze_instancelabs.mgsnake.nms.MESheep1_7_5;
import com.comze_instancelabs.mgsnake.nms.MESheep1_7_9;
import com.comze_instancelabs.mgsnake.nms.register1_7_10;
import com.comze_instancelabs.mgsnake.nms.register1_7_2;
import com.comze_instancelabs.mgsnake.nms.register1_7_5;
import com.comze_instancelabs.mgsnake.nms.register1_7_9;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class IArena extends Arena {

	Main m = null;
	BukkitTask task = null;
	public static HashMap<String, Integer> pteam = new HashMap<String, Integer>();

	public IArena(Main m, String arena) {
		super(m, arena);
		this.m = m;
	}

	@Override
	public void started() {
		initPlayerMovements(this.getName());
	}

	@Override
	public void leavePlayer(final String playername, final boolean fullLeave) {
		Player p = Bukkit.getPlayer(playername);
		for (Entity t : p.getNearbyEntities(64, 64, 64)) {
			if (t.getType() == EntityType.SHEEP) {
				t.remove();
			}
		}
		p.removePotionEffect(PotionEffectType.JUMP);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		super.leavePlayer(playername, fullLeave);
	}

	@Override
	public void joinPlayerLobby(String playername) {
		if (pteam.containsKey(playername)) {
			pteam.remove(playername);
		}

		for (int i = 0; i < 16; i++) {
			if (!pteam.values().contains(i)) {
				pteam.put(playername, i);
			}
		}
		super.joinPlayerLobby(playername);
	}

	@Override
	public void spectate(String playername) {
		super.spectate(playername);
		Player p = Bukkit.getPlayer(playername);
		p.removePotionEffect(PotionEffectType.INVISIBILITY);

		if (this.getPlayerAlive() < 2) {
			// last man standing
			this.stop();
			return;
		}
	}

	@Override
	public void stop() {
		if (task != null) {
			task.cancel();
		}
		super.stop();
	}

	Random r = new Random();

	private void initPlayerMovements(final String arena) {
		for (String p_ : this.getAllPlayers()) {
			final Player p = Bukkit.getPlayer(p_);

			p.setWalkSpeed(0.0F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -5));
			p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 2));
			Vector v = p.getLocation().getDirection().normalize();
			Location l = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ())));
			Location l_ = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ()).multiply(2D)));
			if (m.v1_7_2) {
				ArrayList<MESheep1_7_2> temp = new ArrayList<MESheep1_7_2>(Arrays.asList(register1_7_2.spawnSheep(m, arena, l.add(0D, 1D, 0D), pteam.get(p.getName())), register1_7_2.spawnSheep(m, arena, l_.add(0D, 1D, 0D), pteam.get(p.getName()))));
				psheep1_7_2.put(p, temp);
			} else if (m.v1_7_5) {
				ArrayList<MESheep1_7_5> temp = new ArrayList<MESheep1_7_5>(Arrays.asList(register1_7_5.spawnSheep(m, arena, l.add(0D, 1D, 0D), pteam.get(p.getName())), register1_7_5.spawnSheep(m, arena, l_.add(0D, 1D, 0D), pteam.get(p.getName()))));
				psheep1_7_5.put(p, temp);
			} else if (m.v1_7_9) {
				ArrayList<MESheep1_7_9> temp = new ArrayList<MESheep1_7_9>(Arrays.asList(register1_7_9.spawnSheep(m, arena, l.add(0D, 1D, 0D), pteam.get(p.getName())), register1_7_9.spawnSheep(m, arena, l_.add(0D, 1D, 0D), pteam.get(p.getName()))));
				psheep1_7_9.put(p, temp);
			} else if (m.v1_7_10) {
				ArrayList<MESheep1_7_10> temp = new ArrayList<MESheep1_7_10>(Arrays.asList(register1_7_10.spawnSheep(m, arena, l.add(0D, 1D, 0D), pteam.get(p.getName())), register1_7_10.spawnSheep(m, arena, l_.add(0D, 1D, 0D), pteam.get(p.getName()))));
				psheep1_7_10.put(p, temp);
			}

		}

		BukkitTask t = null;

		arenasize.put(arena, 3);

		final IArena a = this;

		t = Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			public void run() {
				ArrayList<String> temparr = new ArrayList<String>(a.getAllPlayers());
				for (String p_ : temparr) {
					if (!MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p_)) {
						final Player p = Bukkit.getPlayer(p_);
						Location l_ = p.getLocation();
						l_.setPitch(0F);
						Vector dir = l_.getDirection().normalize().multiply(0.4D);
						Vector dir_ = new Vector(dir.getX(), 0.0001D, dir.getZ());
						p.setVelocity(dir_);

						Vector v = p.getLocation().getDirection().normalize();
						Location l = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ()).multiply(-1D)));

						if (l.getBlock().getType() != Material.AIR) {
							a.spectate(p.getName());
						}

						int chance = r.nextInt(100);
						if (chance < 3) {
							int temp = r.nextInt(10) - 5;
							if (temp < 0) {
								temp -= 3;
							} else {
								temp += 3;
							}
							p.getWorld().spawnEntity(p.getLocation().add(temp, 0, temp), EntityType.SLIME);
						}

						for (Entity ent : p.getNearbyEntities(1, 1, 1)) {
							if (ent.getType() == EntityType.SHEEP) {
								Sheep s = (Sheep) ent;
								if (s.getColor() != DyeColor.getByData((byte) pteam.get(p.getName()).byteValue())) {
									a.spectate(p.getName());
								}
							} else if (ent.getType() == EntityType.SLIME) {
								arenasize.put(arena, arenasize.get(arena) + 1);
								ent.remove();
								for (String p__ : a.getAllPlayers()) {
									final Player pp = Bukkit.getPlayer(p__);
									if (!MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p__)) {
										if (m.v1_7_2) {
											ArrayList<MESheep1_7_2> temp = new ArrayList<MESheep1_7_2>(psheep1_7_2.get(pp));
											temp.add(register1_7_2.spawnSheep(m, arena, pp.getLocation(), pteam.get(pp.getName())));
											psheep1_7_2.put(pp, temp);
										} else if (m.v1_7_5) {
											ArrayList<MESheep1_7_5> temp = new ArrayList<MESheep1_7_5>(psheep1_7_5.get(pp));
											temp.add(register1_7_5.spawnSheep(m, arena, pp.getLocation(), pteam.get(pp.getName())));
											psheep1_7_5.put(pp, temp);
										} else if (m.v1_7_9) {
											ArrayList<MESheep1_7_9> temp = new ArrayList<MESheep1_7_9>(psheep1_7_9.get(pp));
											temp.add(register1_7_9.spawnSheep(m, arena, pp.getLocation(), pteam.get(pp.getName())));
											psheep1_7_9.put(pp, temp);
										} else if (m.v1_7_10) {
											ArrayList<MESheep1_7_10> temp = new ArrayList<MESheep1_7_10>(psheep1_7_10.get(pp));
											temp.add(register1_7_10.spawnSheep(m, arena, pp.getLocation(), pteam.get(pp.getName())));
											psheep1_7_10.put(pp, temp);
										}
									}
								}
							}
						}

						if (!pvecs.containsKey(p)) {
							pvecs.put(p, new ArrayList<Vector>(Arrays.asList(v.multiply(0.45D))));
						} else {
							ArrayList<Vector> temp = new ArrayList<Vector>(pvecs.get(p));
							if (temp.size() > arenasize.get(arena)) {
								temp.remove(0);
							}
							temp.add(v.multiply(0.45D));
							pvecs.put(p, temp);
						}

						// System.out.println("[A] " + pvecs.size() + pvecs.get(p));

						updateLocs(arena);

					}
				}
			}
		}, 3L, 3L);

		task = t;
	}

	public HashMap<Player, ArrayList<Location>> plocs = new HashMap<Player, ArrayList<Location>>();
	public HashMap<String, Integer> arenasize = new HashMap<String, Integer>();

	public HashMap<Player, ArrayList<MESheep1_7_2>> psheep1_7_2 = new HashMap<Player, ArrayList<MESheep1_7_2>>();
	public HashMap<Player, ArrayList<MESheep1_7_5>> psheep1_7_5 = new HashMap<Player, ArrayList<MESheep1_7_5>>();
	public HashMap<Player, ArrayList<MESheep1_7_9>> psheep1_7_9 = new HashMap<Player, ArrayList<MESheep1_7_9>>();
	public HashMap<Player, ArrayList<MESheep1_7_10>> psheep1_7_10 = new HashMap<Player, ArrayList<MESheep1_7_10>>();

	public HashMap<Player, ArrayList<Vector>> pvecs = new HashMap<Player, ArrayList<Vector>>();

	private void updateLocs(String arena) {
		IArena a = this;
		for (String p__ : a.getAllPlayers()) {
			Player p_ = Bukkit.getPlayer(p__);
			if (!MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p_)) {

				if (!plocs.containsKey(p_)) {
					plocs.put(p_, new ArrayList<Location>(Arrays.asList(p_.getLocation())));
				} else {
					ArrayList<Location> temp = new ArrayList<Location>(plocs.get(p_));
					if (temp.size() > arenasize.get(arena)) {
						temp.remove(0);
					}
					temp.add(p_.getLocation());
					plocs.put(p_, temp);

					int c = 0;

					if (m.v1_7_2) {
						for (MESheep1_7_2 ms : psheep1_7_2.get(p_)) {
							if (c < pvecs.get(p_).size()) {
								Vector direction = plocs.get(p_).get(c).toVector().subtract(psheep1_7_2.get(p_).get(c).getBukkitEntity().getLocation().toVector()).normalize();
								psheep1_7_2.get(p_).get(c).setYaw(plocs.get(p_).get(c));
								psheep1_7_2.get(p_).get(c).getBukkitEntity().setVelocity(direction.multiply(0.5D));
								c++;
							}
						}
					} else if (m.v1_7_5) {
						for (MESheep1_7_5 ms : psheep1_7_5.get(p_)) {
							if (c < pvecs.get(p_).size()) {
								Vector direction = plocs.get(p_).get(c).toVector().subtract(psheep1_7_5.get(p_).get(c).getBukkitEntity().getLocation().toVector()).normalize();
								psheep1_7_5.get(p_).get(c).setYaw(plocs.get(p_).get(c));
								psheep1_7_5.get(p_).get(c).getBukkitEntity().setVelocity(direction.multiply(0.5D));
								c++;
							}
						}
					} else if (m.v1_7_9) {
						for (MESheep1_7_9 ms : psheep1_7_9.get(p_)) {
							if (c < pvecs.get(p_).size()) {
								Vector direction = plocs.get(p_).get(c).toVector().subtract(psheep1_7_9.get(p_).get(c).getBukkitEntity().getLocation().toVector()).normalize();
								psheep1_7_9.get(p_).get(c).setYaw(plocs.get(p_).get(c));
								psheep1_7_9.get(p_).get(c).getBukkitEntity().setVelocity(direction.multiply(0.5D));
								c++;
							}
						}
					} else if (m.v1_7_10) {
						for (MESheep1_7_10 ms : psheep1_7_10.get(p_)) {
							if (c < pvecs.get(p_).size()) {
								Vector direction = plocs.get(p_).get(c).toVector().subtract(psheep1_7_10.get(p_).get(c).getBukkitEntity().getLocation().toVector()).normalize();
								psheep1_7_10.get(p_).get(c).setYaw(plocs.get(p_).get(c));
								psheep1_7_10.get(p_).get(c).getBukkitEntity().setVelocity(direction.multiply(0.5D));
								c++;
							}
						}
					}

				}

			}
		}
	}

}
