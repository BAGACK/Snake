package com.comze_instancelabs.mgsnake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgsnake.nms.FallingBlock;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.Util;

public class IArena extends Arena {

	Main m = null;
	BukkitTask task = null;
	HashMap<String, Integer> pteam = new HashMap<String, Integer>();
	HashMap<String, Integer> pslimes = new HashMap<String, Integer>();
	BukkitTask powerup_task;
	private boolean useSheeps = true;
	private long tickCount;
	private long remainingTickCount;

	public IArena(Main m, String arena, boolean useSheeps, long tickCount) {
		super(m, arena);
		this.m = m;
		this.useSheeps = useSheeps;
		this.tickCount = tickCount;
	}

	int failcount = 0;
	
	private FallingBlock spawn(Location location, Integer color)
	{
		return this.useSheeps ? this.m.getNmsRegister().spawnSheep(this.m, this.getInternalName(), location, color) : this.m.getNmsRegister().spawnWool(this.m, this.getInternalName(), location, color);
	}

	@Override
	public void started() {
		initPlayerMovements(this.getInternalName());
		final IArena a = this;
		powerup_task = Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			public void run() {
				if (a.getArenaState() != ArenaState.INGAME) {
					if (powerup_task != null) {
						powerup_task.cancel();
						return;
					}
				}
				if (Math.random() * 100 <= m.getConfig().getInt("config.powerup_spawn_percentage")) {
					try {
						Player p = Bukkit
								.getPlayer(a.getAllPlayers().get((int) Math.random() * (a.getAllPlayers().size() - 1)));
						if (p != null) {
							boolean spawn = true;
							if (MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p.getName())) {
								// player is a spectator, retry
								p = Bukkit.getPlayer(
										a.getAllPlayers().get((int) Math.random() * (a.getAllPlayers().size() - 1)));
								if (p != null) {
									if (MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p.getName())) {
										spawn = false;
									}
								} else {
									spawn = false;
								}
							}
							if (spawn) {
								Util.spawnPowerup(m, a, p.getLocation().clone().add(0D, 5D, 0D), getItemStack());
							}
						}
					} catch (Exception e) {
						if (a != null) {
							if (a.getArenaState() != ArenaState.INGAME) {
								if (powerup_task != null) {
									powerup_task.cancel();
								}
							}
						}
						Bukkit.getLogger().log(Level.WARNING, "Use the latest MinigamesLib version to get powerups.", e);
						failcount++;
						if (failcount > 2) {
							if (powerup_task != null) {
								powerup_task.cancel();
							}
						}
					}
				}
			}
		}, 60, 60);
	}

	@Override
	public void leavePlayer(final String playername, final boolean fullLeave) {
		Player p = Bukkit.getPlayer(playername);
		
		// saveDestroyBlocks
		final ArrayList<FallingBlock> plist = psheep.get(p);
		if (plist != null)
		{
			for (FallingBlock ms : plist)
			{
				ms.die();
			}
			plist.clear();
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
				break;
			}
		}
		if (pteam.containsKey(playername)) {
			super.joinPlayerLobby(playername);
		}
	}

	@Override
	public void spectate(String playername) {
		Player p = Bukkit.getPlayer(playername);
		if (p != null) {
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
		super.spectate(playername);
		
		// saveDestroyBlocks
		final ArrayList<FallingBlock> plist = psheep.get(p);
		for (FallingBlock ms : plist)
		{
			ms.die();
		}
		plist.clear();
	}

	@Override
	public void stop() {
		if (task != null) {
			task.cancel();
		}
		super.stop();
		plocs.clear();
		
		// saveDestroyBlocks
		for (final ArrayList<FallingBlock> plist : psheep.values())
		{
			for (FallingBlock ms : plist)
			{
				ms.die();
			}
			plist.clear();
		}
		
		this.psheep.clear();
	}

	public ItemStack getItemStack() {
		double i = Math.random() * 100;
		ItemStack ret = new ItemStack(Material.IRON_BOOTS);
		if (i <= 60) {
			// get a speed boost
			ret = new ItemStack(Material.IRON_BOOTS);
		} else {
			// get a jump boost
			ret = new ItemStack(Material.GOLD_BOOTS);
		}
		return ret;
	}

	Random r = new Random();
	
	private void debugPSheep(String player, ArrayList<FallingBlock> blocks)
	{
		if (MinigamesAPI.debug)
		{
			getPlugin().getLogger().fine("SHEEPS[" + player + "]:");
			for (final FallingBlock block : blocks)
			{
				getPlugin().getLogger().fine("  " + block.toVector());
			}
		}
	}

	private void initPlayerMovements(final String arena) {
		this.remainingTickCount = this.tickCount;
		boolean invisible = m.getConfig().getBoolean("config.players_invisible");
		for (String p_ : this.getAllPlayers()) {
			final Player p = Bukkit.getPlayer(p_);

			p.setWalkSpeed(0.0F);
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -5));
			if (invisible) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 2));
			}
			Vector v = p.getLocation().getDirection().normalize();
			Location l = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ())));
			Location l_ = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ()).multiply(2D)));
			final Location l2 = l.add(0D, 1D, 0D);
			final Location l_2 = l_.add(0D, 1D, 0D);
			l2.setY(Math.floor(l2.getY())); // Seems to correct the first sheeps (they were not on the ground)
			l_2.setY(Math.floor(l_2.getY())); // Seems to correct the first sheeps (they were not on the ground)
			final ArrayList<FallingBlock> temp = new ArrayList<>(Arrays.asList(
					this.spawn(l2, pteam.get(p.getName())),
					this.spawn(l_2, pteam.get(p.getName()))));
			debugPSheep(p_, temp);
			psheep.put(p, temp);
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
						double multiplier = 0.4D;
						double jump_multiplier = 0.0001D;
						if (m.pspeed.contains(p.getName())) {
							multiplier = 1D;
						}
						if (m.pjump.contains(p.getName())) {
							jump_multiplier = 1.4D;
							m.pjump.remove(p.getName());
						}
						Vector dir = l_.getDirection().normalize().multiply(multiplier);
						Vector dir_ = new Vector(dir.getX(), jump_multiplier, dir.getZ());
						p.setVelocity(dir_);

						Vector v = p.getLocation().getDirection().normalize();
						Location l = p.getLocation().subtract((new Vector(v.getX(), 0.0001D, v.getZ()).multiply(-1D)));

						Location pl_ = l.clone().add(0D, 1D, 0D);
						if (pl_.getBlock().getType() != Material.AIR) {
							if (MinigamesAPI.debug) getPlugin().getLogger().fine("Player " + p_ + " ran into solid block @ " + pl_);
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
							Slime s = (Slime) p.getWorld().spawnEntity(p.getLocation().add(temp, 0, temp),
									EntityType.SLIME);
							s.setSize(1);
						}

						for (Entity ent : p.getNearbyEntities(1, 1, 1)) {
							if (useSheeps && ent.getType() == EntityType.SHEEP) {
								Sheep s = (Sheep) ent;
								DyeColor color = ((Colorable) s).getColor();
								if (color.getData() != (byte) pteam.get(p.getName()).byteValue()) {
									if (MinigamesAPI.debug) getPlugin().getLogger().fine("Player " + p_ + " ran into sheeps with color " + color);
									a.spectate(p.getName());
								}
							} else if (!useSheeps && ent.getType() == EntityType.FALLING_BLOCK) {
								org.bukkit.entity.FallingBlock s = (org.bukkit.entity.FallingBlock) ent;
								if (s.getBlockData() != (byte) pteam.get(p.getName()).byteValue()) {
									if (MinigamesAPI.debug) getPlugin().getLogger().fine("Player " + p_ + " ran into block with color " + s.getBlockData());
									a.spectate(p.getName());
								}
							} else if (ent.getType() == EntityType.SLIME) {
								arenasize.put(arena, arenasize.get(arena) + 1);
								m.scoreboard.updateScoreboard(a);
								ent.remove();
								for (String p__ : a.getAllPlayers()) {
									final Player pp = Bukkit.getPlayer(p__);
									if (!MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p__)) {
										final Integer color = pteam.get(pp.getName());
										psheep.get(pp).add(0, spawn(pp.getLocation(), color));
									}
								}
							}
						}

//						if (!pvecs.containsKey(p)) {
//							pvecs.put(p, new ArrayList<Vector>(Arrays.asList(v.multiply(0.45D))));
//						} else {
//							ArrayList<Vector> temp = pvecs.get(p);
//							if (temp.size() > arenasize.get(arena)) {
//								temp.remove(0);
//							}
//							temp.add(v.multiply(0.45D));
//						}

						// pvecs.get(p));

					}
				}

				updateLocs(arena);
			}
		}, 2L, 2L);

		task = t;
	}

	public HashMap<Player, ArrayList<Location>> plocs = new HashMap<Player, ArrayList<Location>>();
	public HashMap<String, Integer> arenasize = new HashMap<String, Integer>();

	public HashMap<Player, ArrayList<FallingBlock>> psheep = new HashMap<>();

	//public HashMap<Player, ArrayList<Vector>> pvecs = new HashMap<Player, ArrayList<Vector>>();

	private void updateLocs(String arena) {
		IArena a = this;
		this.remainingTickCount--;
		boolean updatePLocs = false;
		if (this.remainingTickCount == 0)
		{
			this.remainingTickCount = this.tickCount;
			updatePLocs = true;
		}
		for (String p__ : a.getAllPlayers()) {
			Player p_ = Bukkit.getPlayer(p__);
			if (!MinigamesAPI.getAPI().pinstances.get(m).global_lost.containsKey(p_)) {
				if (!plocs.containsKey(p_)) {
					plocs.put(p_, new ArrayList<Location>(Arrays.asList(p_.getLocation())));
				} else if (updatePLocs) {
					ArrayList<Location> temp = plocs.get(p_);
					if (temp.size() > arenasize.get(arena)) {
						temp.remove(0);
					}
					temp.add(p_.getLocation());
				}

				int c = 0;
				final ArrayList<FallingBlock> plist = psheep.get(p_);
				for (FallingBlock ms : plist) {
					if (c < plocs.get(p_).size()) {
						final Location bl = plocs.get(p_).get(c);
						Vector direction = bl.toVector()
								.subtract(ms.toVector()).normalize();
						ms.setYaw(bl);
						ms.setVelocity(direction.multiply(0.5D));
						if (MinigamesAPI.debug) getPlugin().getLogger().fine("Sheep[" + p__ + "/" + ms.toVector() + "] yaws at " + bl);
						c++;
					}
				}
				debugPSheep(p__, plist);
			}
		}
	}

}
