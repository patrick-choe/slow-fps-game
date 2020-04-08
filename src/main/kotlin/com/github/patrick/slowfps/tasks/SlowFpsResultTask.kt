package com.github.patrick.slowfps.tasks

import com.github.noonmaru.tap.packet.Packet.TITLE
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsTeams
import org.bukkit.ChatColor.RED

class SlowFpsResultTask : SlowFpsTask {
    /**
     * This 'execute' method works like a 'run'
     * method in 'Runnable'
     */
    override fun execute(): SlowFpsTask? {
        TITLE.compound(RED.toString() + "게임종료!", "우승: ${slowFpsTeams.first().team.prefix + slowFpsTeams.first().team.name}", 5, 60, 10).sendAll()
        return null
    }
}