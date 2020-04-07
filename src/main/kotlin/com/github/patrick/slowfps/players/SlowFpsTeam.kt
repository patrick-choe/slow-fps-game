package com.github.patrick.slowfps.players

import org.bukkit.entity.Player

class SlowFpsTeam {
    lateinit var slowFpsPlayer: SlowFpsPlayer

    fun setPlayer(player: Player) {
        slowFpsPlayer = SlowFpsPlayer(player)
    }
}
