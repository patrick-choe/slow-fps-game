/*
 * Copyright (C) 2020 PatrickKR
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact me on <mailpatrickkr@gmail.com>
 */

package com.github.patrick.slowfps.process

import com.github.patrick.slowfps.plugin.SlowFpsPlugin.Companion.instance
import com.github.patrick.slowfps.tasks.SlowFpsScheduler
import com.github.patrick.slowfps.utils.SlowFpsPlayer
import com.github.patrick.slowfps.utils.SlowFpsProjectile
import com.github.patrick.slowfps.utils.SlowFpsTeam
import org.bukkit.Bukkit.getPlayerExact
import org.bukkit.Bukkit.getScheduler
import org.bukkit.Bukkit.getScoreboardManager
import org.bukkit.ChatColor.DARK_BLUE
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList.unregisterAll
import org.bukkit.scoreboard.DisplaySlot.SIDEBAR
import org.bukkit.scoreboard.Team
import java.util.IdentityHashMap

/**
 * This class represents the game itself of this plugin.
 */
class SlowFpsGame(teams: HashSet<Team>) {
    /**
     * This stores the project-wide variables directly
     * related to the game.
     */
    companion object {
        var gameStatus = false
        val slowFpsTeams = HashSet<SlowFpsTeam>()
        val slowFpsPlayers = IdentityHashMap<Player, SlowFpsPlayer>()
        val slowFpsProjectiles = HashSet<SlowFpsProjectile>()
    }

    /**
     * This is called when the new game has initialized.
     */
    init {
        val scoreboard = getScoreboardManager().mainScoreboard
        scoreboard.getObjective("slowfps")?.unregister()
        val objective = scoreboard.registerNewObjective("slowfps", "dummy")
        objective.displayName = "   ${DARK_BLUE}Slow FPS   "
        objective.displaySlot = SIDEBAR
        slowFpsTeams.clear()
        slowFpsPlayers.clear()

        teams.forEach {
            val slowFpsTeam = SlowFpsTeam(it)

            it.entries.forEach { entry ->
                getPlayerExact(entry)?.let { player ->
                    slowFpsTeam.setPlayer(player)
                }
            }

            val slowFpsPlayer = slowFpsTeam.slowFpsPlayer
            slowFpsTeams.add(slowFpsTeam)
            slowFpsPlayers[slowFpsPlayer.player] = slowFpsPlayer
        }

        if (teams.isEmpty()) throw IllegalArgumentException("Teams cannot be empty")
        gameStatus = true
        getScheduler().runTaskTimer(instance, SlowFpsScheduler(), 0, 1)
    }

    /**
     * This method unregisters the game by destroying all projectiles
     * and canceling tasks and listeners.
     */
    fun unregister() {
        gameStatus = false
        slowFpsProjectiles.forEach { it.destroy() }
        unregisterAll(instance)
        getScheduler().cancelTasks(instance)
    }
}