/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.comze_instancelabs.mgsnake;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.ArenaListener;
import com.comze_instancelabs.minigamesapi.PluginInstance;

public class IArenaListener extends ArenaListener {

	private Main main;

	public IArenaListener(JavaPlugin plugin, PluginInstance pinstance, String minigame) {
		super(plugin, pinstance, minigame);
		this.main = (Main) plugin; 
	}

	@EventHandler
	@Override
	public void onMobSpawn(CreatureSpawnEvent evt)
	{
		// allow spawn of our own sheeps
		final LivingEntity entity = evt.getEntity();
		if (entity instanceof Slime || (entity instanceof Sheep && this.main.getNmsRegister().isSheep(entity)))
		{
			return;
		}
		super.onMobSpawn(evt);
	}
	
	

}
