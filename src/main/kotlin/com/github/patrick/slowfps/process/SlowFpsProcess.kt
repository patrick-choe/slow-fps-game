package com.github.patrick.slowfps.process

import org.bukkit.Bukkit.broadcast
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.Bukkit.getScoreboardManager
import org.bukkit.scoreboard.Team
import java.lang.Integer.toHexString

object SlowFpsProcess {
    private var game: SlowFpsGame? = null

    fun startProcess() {
        if (game != null) broadcast("게임이 이미 진행중입니다.", "command.rhythm").also { return }
        val scoreboard = getScoreboardManager().mainScoreboard
        scoreboard.teams.forEach { it.unregister() }

        val teams = HashSet<Team>()
        getOnlinePlayers().forEach {
            if (it != null) {
                val team = scoreboard.registerNewTeam(it.name)
                team.prefix = "§${toHexString(it.name.hashCode() and 0xF)}"
                team.addEntry(it.name)
            }
        }

        if (teams.isEmpty()) broadcast("게임 참가자가 없습니다. config.yml 을 확인하세요", "command.rhythm").also { return }
        game = SlowFpsGame(teams)
    }

    fun stopProcess() {
        if (game == null) broadcast("진행중인 게임이 없습니다.", "command.rhythm").also { return }
        game?.unregister()
        game = null
    }
}