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

package com.github.patrick.slowfps.tasks

import com.github.patrick.slowfps.plugin.SlowFpsPlugin.Companion.instance
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.gameStatus
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsProjectiles
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsTeams
import com.github.patrick.slowfps.process.SlowFpsListener
import org.bukkit.Bukkit.broadcastMessage
import org.bukkit.Bukkit.getPluginManager

/**
 * This class runs while game is ongoing.
 * If the team size equals
 */
class SlowFpsGameTask : SlowFpsTask {
    private var ticks = -1

    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    override fun execute(): SlowFpsTask? {
        ++ticks
        slowFpsProjectiles.forEach { it.onUpdate() }
        if (ticks == 0) getPluginManager().registerEvents(SlowFpsListener(), instance)
        if (slowFpsTeams.size == 1) gameStatus = false
        if (slowFpsTeams.size == 0) {
            broadcastMessage("무승부라니 이럴수가 ;;")
            return null
        }

        return if (gameStatus) this else SlowFpsResultTask()
    }
}