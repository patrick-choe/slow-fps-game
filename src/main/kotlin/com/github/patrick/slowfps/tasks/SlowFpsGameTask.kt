package com.github.patrick.slowfps.tasks

import com.github.patrick.slowfps.plugin.SlowFpsPlugin.Companion.instance
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.gameStatus
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsProjectiles
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsTeams
import com.github.patrick.slowfps.process.SlowFpsListener
import org.bukkit.Bukkit.broadcastMessage
import org.bukkit.Bukkit.getPluginManager

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