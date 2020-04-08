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

package com.github.patrick.slowfps.process

import com.github.noonmaru.tap.Tap.ITEM
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsPlayers
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsProjectiles
import com.github.patrick.slowfps.utils.SlowFpsProjectile
import org.bukkit.ChatColor.WHITE
import org.bukkit.GameMode.SPECTATOR
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class SlowFpsListener : Listener {
    @EventHandler fun onInteract(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player
        val item = event.item
        if (item != null) slowFpsProjectiles += SlowFpsProjectile(player, player.location.direction.normalize(), ITEM.fromItemStack(item))
    }

    @EventHandler fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity?: return
        slowFpsPlayers[player]?.let {
            event.deathMessage = "${it.team.displayName}${WHITE}는 FPS 게임에서 '누군가'의 투사체에 맞아 사망했습니다."
            it.team.dead = true
            player.gameMode = SPECTATOR
        }
    }

    @EventHandler fun onEvent(event: BlockBreakEvent) = event.cancel()
    @EventHandler fun onEvent(event: BlockDamageEvent) = event.cancel()
    @EventHandler fun onEvent(event: BlockPlaceEvent) = event.cancel()
    @EventHandler fun onEvent(event: EntityDamageByEntityEvent) = event.cancel()
    @EventHandler fun onEvent(event: EntityRegainHealthEvent) = event.cancel()
    @EventHandler fun onEvent(event: EntitySpawnEvent) = event.cancel()
    @EventHandler fun onEvent(event: FoodLevelChangeEvent) = event.cancel()
    @EventHandler fun onEvent(event: InventoryInteractEvent) = event.cancel()
    @EventHandler fun onEvent(event: InventoryOpenEvent) = event.cancel()
    @EventHandler fun onEvent(event: PlayerDropItemEvent) = event.cancel()

    private fun Cancellable.cancel() {
        isCancelled = true
    }
}