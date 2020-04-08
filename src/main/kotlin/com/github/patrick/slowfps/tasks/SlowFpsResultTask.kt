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

import com.github.noonmaru.tap.packet.Packet.TITLE
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsTeams
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.ChatColor.RED
import org.bukkit.GameMode.ADVENTURE
import org.bukkit.GameMode.CREATIVE

class SlowFpsResultTask : SlowFpsTask {
    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    override fun execute(): SlowFpsTask? {
        TITLE.compound(RED.toString() + "게임종료!", "우승: ${slowFpsTeams.first().displayName}", 5, 60, 10).sendAll()
        getOnlinePlayers()?.forEach {
            it?: return@forEach
            it.gameMode = if (it.isOp) CREATIVE else ADVENTURE
        }
        return null
    }
}