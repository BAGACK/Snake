package com.comze_instancelabs.mgsnake;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comze_instancelabs.mgsnake.nms.Register;
import com.comze_instancelabs.mgsnake.nms.register1_10;
import com.comze_instancelabs.mgsnake.nms.register1_11;
import com.comze_instancelabs.mgsnake.nms.register1_12;
import com.comze_instancelabs.mgsnake.nms.register1_7_10;
import com.comze_instancelabs.mgsnake.nms.register1_7_2;
import com.comze_instancelabs.mgsnake.nms.register1_7_5;
import com.comze_instancelabs.mgsnake.nms.register1_7_9;
import com.comze_instancelabs.mgsnake.nms.register1_8;
import com.comze_instancelabs.mgsnake.nms.register1_8_5;
import com.comze_instancelabs.mgsnake.nms.register1_8_8;
import com.comze_instancelabs.mgsnake.nms.register1_9;
import com.comze_instancelabs.mgsnake.nms.register1_9_4;
import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaConfigStrings;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.PlayerPickupItemHelper;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Main extends JavaPlugin implements Listener {

	MinigamesAPI api = null;
	PluginInstance pli = null;
	static Main m = null;
	static int global_arenas_size = 30;

	ICommandHandler cmdhandler = new ICommandHandler();
	IArenaScoreboard scoreboard;

	ArrayList<String> pspeed = new ArrayList<String>();
	ArrayList<String> pjump = new ArrayList<String>();
	
	private Register nmsregister;
	
	public Register getNmsRegister()
	{
		return this.nmsregister;
	}

	public void onEnable() {
		m = this;
		MinigamesAPI.getAPI();
		api = MinigamesAPI.setupAPI(this, "snake", IArena.class, new ArenasConfig(this), new MessagesConfig(this), new IClassesConfig(this), new StatsConfig(this, false), new DefaultConfig(this, false), true);
		PluginInstance pinstance = api.pinstances.get(this);
		pinstance.addLoadedArenas(loadArenas(this, pinstance.getArenasConfig()));
		Bukkit.getPluginManager().registerEvents(this, this);
		scoreboard = new IArenaScoreboard(this);
		pinstance.scoreboardManager = scoreboard;
		IArenaListener listener = new IArenaListener(this, pinstance, "snake");
		pinstance.setArenaListener(listener);
		MinigamesAPI.getAPI().registerArenaListenerLater(this, listener);
		pli = pinstance;

		// let's check which version we're on.
		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "MGSnake is running on " + MinigamesAPI.SERVER_VERSION + ".");
		switch (MinigamesAPI.SERVER_VERSION)
		{
		default:
		case Unknown:
			getLogger().info("Unsupported version");
			break;
		case V1_12:
		case V1_12_R1:
			this.nmsregister = new register1_12();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.12 mode.");
			break;
		case V1_11:
		case V1_11_R1:
			this.nmsregister = new register1_11();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.11 mode.");
			break;
		case V1_10:
		case V1_10_R1:
			this.nmsregister = new register1_10();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.10 mode.");
			break;
		case V1_7:
		case V1_7_R1:
			this.nmsregister = new register1_7_2();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.7.2 mode.");
			break;
		case V1_7_R2:
			this.nmsregister = new register1_7_5();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.7.5 mode.");
			break;
		case V1_7_R3:
			this.nmsregister = new register1_7_9();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.7.9 mode.");
			break;
		case V1_7_R4:
			this.nmsregister = new register1_7_10();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.7.10 mode.");
			break;
		case V1_8_R1:
		case V1_8:
			this.nmsregister = new register1_8();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.8 mode.");
			break;
		case V1_8_R2:
			this.nmsregister = new register1_8_5();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.8.5 mode.");
			break;
		case V1_8_R3:
			this.nmsregister = new register1_8_8();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.8.8 mode.");
			break;
		case V1_9:
		case V1_9_R1:
			this.nmsregister = new register1_9();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.9/1.9.2 mode.");
			break;
		case V1_9_R2:
			this.nmsregister = new register1_9_4();
			this.nmsregister.registerEntities();
			getLogger().info("Turned on 1.9_4 mode.");
			break;
		}

		this.getConfig().addDefault("config.powerup_spawn_percentage", 5);
		this.getConfig().addDefault("config.players_invisible", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		new PlayerPickupItemHelper(this, this::onPlayerPickup);
	}

	public static ArrayList<Arena> loadArenas(JavaPlugin plugin, ArenasConfig cf) {
		ArrayList<Arena> ret = new ArrayList<Arena>();
		FileConfiguration config = cf.getConfig();
		if (!config.isSet("arenas")) {
			return ret;
		}
		for (String arena : config.getConfigurationSection(ArenaConfigStrings.ARENAS_PREFIX).getKeys(false)) {
			if (Validator.isArenaValid(plugin, arena, cf.getConfig())) {
				boolean useSheeps = true;
				if (cf.getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".useSheeps"))
				{
					useSheeps = cf.getConfig().getBoolean(ArenaConfigStrings.ARENAS_PREFIX + arena + ".useSheeps");
				}
				long tickCount = 2L;
				if (cf.getConfig().isSet(ArenaConfigStrings.ARENAS_PREFIX + arena + ".tickCount"))
				{
					tickCount = cf.getConfig().getLong(ArenaConfigStrings.ARENAS_PREFIX + arena + ".tickCount");
				}
				ret.add(initArena(arena, useSheeps, tickCount));
			}
		}
		return ret;
	}

	public static IArena initArena(String arena, boolean useSheeps, long tickCount) {
		IArena a = new IArena(m, arena, useSheeps, tickCount);
		ArenaSetup s = MinigamesAPI.getAPI().pinstances.get(m).arenaSetup;
		a.init(Util.getSignLocationFromArena(m, arena), Util.getAllSpawns(m, arena), Util.getMainLobby(m), Util.getComponentForArena(m, arena, "lobby"), s.getPlayerCount(m, arena, true), s.getPlayerCount(m, arena, false), s.getArenaVIP(m, arena));
		return a;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return cmdhandler.handleArgs(this, MinigamesAPI.getAPI().getPermissionGamePrefix("snake"), "/" + cmd.getName(), sender, args);
	}

	public void onPlayerPickup(PlayerPickupItemHelper.CustomPickupEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			if (event.getItem().getItemStack().getType() != Material.IRON_BOOTS && event.getItem().getItemStack().getType() != Material.GOLD_BOOTS) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	public Vector v;

	@EventHandler
	public void onPlayerMove(VehicleMoveEvent event) {
		Entity passenger = event.getVehicle().getPassenger();
		if (passenger instanceof Player) {
			Player p = (Player) passenger;
			if (pli.global_players.containsKey(p.getName())) {
				v = p.getLocation().getDirection().multiply(10.0D);
				event.getVehicle().getLocation().setDirection(v);
				event.getVehicle().setVelocity(new Vector(v.getX(), 0.0001D, v.getZ()));
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (pli.global_players.containsKey(p.getName())) {
				IArena a = (IArena) pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.INGAME) {
					if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.PROJECTILE || event.getCause() == DamageCause.FALL) {
						p.setHealth(20D);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (pli.global_players.containsKey(event.getPlayer().getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryInteractEvent event) {
		final Player p = (Player) event.getWhoClicked();
		if (pli.global_players.containsKey(p.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			if (event.hasItem()) {
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					final ItemStack item = event.getItem();
					if (item.getType() == Material.IRON_BOOTS) {
						pspeed.add(p.getName());
						Bukkit.getScheduler().runTaskLater(m, new Runnable() {
							public void run() {
								if (pspeed.contains(p.getName())) {
									pspeed.remove(p.getName());
								}
							}
						}, 200L);
						Util.clearInv(p);
						event.setCancelled(true);
						return;
					} else if (item.getType() == Material.GOLD_BOOTS) {
						/*
						 * p.removePotionEffect(PotionEffectType.JUMP); p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 7));
						 * Bukkit.getScheduler().runTaskLater(m, new Runnable() { public void run() { p.removePotionEffect(PotionEffectType.JUMP);
						 * p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -5)); } }, 100L);
						 */
						pjump.add(p.getName());
						Util.clearInv(p);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

}
