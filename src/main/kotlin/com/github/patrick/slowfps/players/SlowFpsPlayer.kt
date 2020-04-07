package com.github.patrick.slowfps.players

import org.bukkit.entity.Player
import java.util.UUID

class SlowFpsPlayer(val player: Player) {
    val uniqueId: UUID = player.uniqueId
}
