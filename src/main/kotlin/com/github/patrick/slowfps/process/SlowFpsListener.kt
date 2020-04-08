package com.github.patrick.slowfps.process

import com.github.noonmaru.tap.Tap.ITEM
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.onlineSlowFpsPlayers
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsProjectiles
import com.github.patrick.slowfps.utils.SlowFpsProjectile
import org.bukkit.GameMode.SPECTATOR
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action.LEFT_CLICK_AIR
import org.bukkit.event.block.Action.LEFT_CLICK_BLOCK
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class SlowFpsListener : Listener {
    @EventHandler fun onInteract(event: PlayerInteractEvent) {
        if (setOf(LEFT_CLICK_AIR, LEFT_CLICK_BLOCK).contains(event.action)) {
            val player = event.player
            event.isCancelled = true
            slowFpsProjectiles += SlowFpsProjectile(player, player.location.direction.normalize().multiply(0.05), ITEM.fromItemStack(event.item))
        }
    }

    @EventHandler fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity?: return
        onlineSlowFpsPlayers[player]?.let {
            it.team.dead = true
            player.gameMode = SPECTATOR
        }
    }

    @EventHandler fun onEvent(event: BlockBreakEvent) = event.cancel()
    @EventHandler fun onEvent(event: BlockDamageEvent) = event.cancel()
    @EventHandler fun onEvent(event: BlockPlaceEvent) = event.cancel()
    @EventHandler fun onEvent(event: EntitySpawnEvent) = event.cancel()
    @EventHandler fun onEvent(event: FoodLevelChangeEvent) = event.cancel()
    @EventHandler fun onEvent(event: InventoryInteractEvent) = event.cancel()
    @EventHandler fun onEvent(event: InventoryOpenEvent) = event.cancel()
    @EventHandler fun onEvent(event: PlayerDropItemEvent) = event.cancel()

    private fun Cancellable.cancel() {
        isCancelled = true
    }
}