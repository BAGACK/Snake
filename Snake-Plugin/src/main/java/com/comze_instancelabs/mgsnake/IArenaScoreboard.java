package com.comze_instancelabs.mgsnake;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;

public class IArenaScoreboard extends ArenaScoreboard {

	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();

	JavaPlugin plugin = null;
	PluginInstance pli;

	public IArenaScoreboard(JavaPlugin plugin) {
		this.plugin = plugin;
		pli = MinigamesAPI.getAPI().pinstances.get(plugin);
	}

	public void updateScoreboard(final IArena arena) {
		try {
			for (String p_ : arena.getAllPlayers()) {
				if (!ascore.containsKey(arena.getInternalName())) {
					ascore.put(arena.getInternalName(), Bukkit.getScoreboardManager().getNewScoreboard());
				}
				if (!aobjective.containsKey(arena.getInternalName())) {
					aobjective.put(arena.getInternalName(), ascore.get(arena.getInternalName()).registerNewObjective(arena.getInternalName(), "dummy"));
				}

				aobjective.get(arena.getInternalName()).setDisplaySlot(DisplaySlot.SIDEBAR);

				aobjective.get(arena.getInternalName()).setDisplayName(pli.getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getDisplayName()));

				for (String pl_ : arena.getAllPlayers()) {
					Player p = Bukkit.getPlayer(pl_);
					int score = 0;
					if (arena.arenasize.containsKey(arena.getInternalName())) {
						score = arena.arenasize.get(arena.getInternalName());
					}
					if (!pli.global_lost.containsKey(pl_)) {
						try {
							if (pl_.length() < 15) {
								get(aobjective.get(arena.getInternalName()), ChatColor.GREEN + pl_).setScore(score);
							} else {
								get(aobjective.get(arena.getInternalName()), ChatColor.GREEN + pl_.substring(0, pl_.length() - 3)).setScore(score);
							}
						} catch (Exception e) {
						}
					} else if (pli.global_lost.containsKey(pl_)) {
						try {
							if (pl_.length() < 15) {
								reset(ascore.get(arena.getInternalName()), ChatColor.GREEN + pl_);
								get(aobjective.get(arena.getInternalName()), ChatColor.RED + pl_).setScore(score);
							} else {
								reset(ascore.get(arena.getInternalName()), ChatColor.GREEN + pl_.substring(0, p_.length() - 3));
								get(aobjective.get(arena.getInternalName()), ChatColor.RED + pl_.substring(0, p_.length() - 3)).setScore(score);
							}
						} catch (Exception e) {
						}
					}
				}

				Bukkit.getPlayer(p_).setScoreboard(ascore.get(arena.getInternalName()));
			}
		} catch (Exception e) {
			plugin.getLogger().log(Level.SEVERE, "Failed setting Scoreboard:", e);
		}
	}

	@Override
	public void updateScoreboard(JavaPlugin plugin, final Arena arena) {
		IArena a = (IArena) MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(arena.getInternalName());
		this.updateScoreboard(a);
	}

	@Override
	public void removeScoreboard(String arena, Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sc = manager.getNewScoreboard();
		sc.clearSlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(sc);
		if (ascore.containsKey(arena)) {
			ascore.remove(arena);
		}
		if (aobjective.containsKey(arena)) {
			aobjective.remove(arena);
		}
	}
}
