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

import org.bukkit.Bukkit.broadcast
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.Bukkit.getScoreboardManager
import org.bukkit.scoreboard.Team
import java.lang.Integer.toHexString

/**
 * This class manages the game by starting and stopping the game.
 */
object SlowFpsProcess {
    private var game: SlowFpsGame? = null

    /**
     * This method starts the game by removing all the teams from scoreboard,
     * and creating a new game instance along with the information about teams.
     */
    fun startProcess() {
        if (game != null) broadcast("게임이 이미 진행중입니다.", "command.slowfps").also { return }
        val scoreboard = getScoreboardManager().mainScoreboard?: throw NullPointerException("Scoreboard cannot be null.")
        scoreboard.teams.forEach { it.unregister() }

        val teams = HashSet<Team>()
        getOnlinePlayers().forEach {
            if (it != null) {
                val team = scoreboard.registerNewTeam(it.name)?: return@forEach
                team.prefix = "§${toHexString(it.name.hashCode() and 0xF)}"
                team.addEntry(it.name)
                teams.add(team)
            }
        }

        if (teams.isEmpty()) broadcast("게임 참가자가 없습니다. 서버를 확인하세요", "command.slowfps").also { return }
        game = SlowFpsGame(teams)
    }

    /**
     * This method stops the ongoing game by unregistering the game process.
     */
    fun stopProcess() {
        if (game == null) broadcast("진행중인 게임이 없습니다.", "command.slowfps").also { return }
        game?.unregister()
        game = null
    }
}