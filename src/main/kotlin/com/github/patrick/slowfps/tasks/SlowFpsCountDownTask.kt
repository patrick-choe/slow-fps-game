package com.github.patrick.slowfps.tasks

import com.github.noonmaru.tap.packet.Packet.TITLE
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.onlineSlowFpsPlayers
import org.bukkit.ChatColor

class SlowFpsCountDownTask : SlowFpsTask {
    private var ticks = 100
    private val period = 20
    private var countdown = -1
    override fun execute(): SlowFpsTask {
        --ticks
        if (ticks > 0) {
            val count = (ticks + period) / period
            if (countdown != count) try { TITLE.compound(getColorByCount(count).toString() + count.toString(), null, 0, period + 20, 0).sendAll() } finally { countdown = count }
            return this
        }
        TITLE.compound("§6START", null, 0, 24, 6).sendAll()
        onlineSlowFpsPlayers.values.forEach { it.prepare() }
        return SlowFpsGameTask()
    }

    private fun getColorByCount(count: Int) = when (count) {
        1 -> ChatColor.RED
        2 -> ChatColor.YELLOW
        3 -> ChatColor.GREEN
        4 -> ChatColor.AQUA
        5 -> ChatColor.LIGHT_PURPLE
        else -> ChatColor.WHITE
    }
}