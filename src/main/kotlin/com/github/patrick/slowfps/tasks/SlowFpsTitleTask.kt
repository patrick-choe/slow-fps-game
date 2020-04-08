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

class SlowFpsTitleTask : SlowFpsTask {
    private var ticks: Int = -1

    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    override fun execute(): SlowFpsTask {
        ticks++
        if (ticks == 0) TITLE.compound("§4S§6L§2O§bW §1F§9P§dS§5!", null, 5, 50, 5).sendAll()
        return if (ticks >= 60) SlowFpsCountDownTask() else this
    }
}
