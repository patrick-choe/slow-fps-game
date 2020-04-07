package com.github.patrick.slowfps.process

import com.github.patrick.slowfps.players.SlowFpsPlayer
import com.github.patrick.slowfps.players.SlowFpsTeam
import com.github.patrick.slowfps.plugin.SlowFpsPlugin.Companion.instance
import org.bukkit.Bukkit.getPlayerExact
import org.bukkit.Bukkit.getScheduler
import org.bukkit.Bukkit.getScoreboardManager
import org.bukkit.ChatColor.DARK_BLUE
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList.unregisterAll
import org.bukkit.scoreboard.DisplaySlot.SIDEBAR
import org.bukkit.scoreboard.Team
import java.util.IdentityHashMap
import java.util.UUID

class SlowFpsGame(teams: HashSet<Team>) {
    companion object {
        var gameStatus = false
        val slowFpsTeams = HashSet<SlowFpsTeam>()
        val slowFpsPlayers = HashMap<UUID, SlowFpsPlayer>()
        val onlineSlowFpsPlayers = IdentityHashMap<Player, SlowFpsPlayer>()
    }

    init {
        val scoreboard = getScoreboardManager().mainScoreboard
        scoreboard.getObjective("slowfps")?.apply { this.unregister() }
        val objective = scoreboard.registerNewObjective("slowfps", "dummy")
        objective.displayName = "   ${DARK_BLUE}RhythmCraft   "
        objective.displaySlot = SIDEBAR

        teams.forEach {
            val slowFpsTeam = SlowFpsTeam()

            it.entries.forEach { entry ->
                getPlayerExact(entry)?.let { player ->
                    slowFpsTeam.setPlayer(player)
                }
            }

            val slowFpsPlayer = slowFpsTeam.slowFpsPlayer
            slowFpsTeams.add(slowFpsTeam)
            slowFpsPlayers[slowFpsPlayer.uniqueId] = slowFpsPlayer
            onlineSlowFpsPlayers[slowFpsPlayer.player] = slowFpsPlayer
        }

        if (teams.isEmpty()) throw IllegalArgumentException("Teams cannot be empty")
        gameStatus = true
    }

    fun unregister() {
        gameStatus = false
        unregisterAll(instance)
        getScheduler().cancelTasks(instance)
    }
}