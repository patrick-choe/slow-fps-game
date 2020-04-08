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

package com.github.patrick.slowfps.utils

import com.github.noonmaru.math.Vector
import com.github.noonmaru.tap.Tap
import com.github.noonmaru.tap.entity.TapArmorStand
import com.github.noonmaru.tap.entity.TapPlayer.wrapPlayer
import com.github.noonmaru.tap.firework.FireworkEffect.Type.STAR
import com.github.noonmaru.tap.firework.FireworkEffect.builder
import com.github.noonmaru.tap.item.TapItemStack
import com.github.noonmaru.tap.packet.Packet.EFFECT
import com.github.noonmaru.tap.packet.Packet.ENTITY
import com.github.patrick.slowfps.process.SlowFpsGame.Companion.slowFpsPlayers
import org.bukkit.Bukkit.broadcastMessage
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.ChatColor.WHITE
import org.bukkit.Color.fromRGB
import org.bukkit.GameMode.SPECTATOR
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import kotlin.random.Random.Default.nextInt
import org.bukkit.util.Vector as BukkitVector

/**
 * This class represents the projectile of this game.
 * When created, it moves slowly until it hits the block or entity,
 * or run out of time.
 */
class SlowFpsProjectile(private val owner: Player, private val move: BukkitVector, private val tapItemStack: TapItemStack) {
    private val tapArmorStand: TapArmorStand = Tap.ENTITY.createEntity(ArmorStand::class.java)
    private val armorStand: ArmorStand?
    private lateinit var position: Location
    private var ticks = 0
    private var removed = false

    /**
     * This boolean checks whether the projectile is dead,
     * and should be removed.
     */
    var dead = false
        private set

    /**
     * This is called when the new projectile is created.
     */
    init {
        tapArmorStand.apply {
            isInvisible = true
            setGravity(false)
            setBasePlate(false)
            owner.eyeLocation.let { pos ->
                setPositionAndRotation(pos.x, pos.y - 0.5, pos.z, pos.yaw + 90F, 0F)
                setHeadPose(0F, 0F, pos.pitch + 45F)
            }
        }
        armorStand = tapArmorStand.bukkitEntity.apply {
            ENTITY.spawnMob(this).sendAll()
            ENTITY.metadata(this).sendAll()
            ENTITY.equipment(tapArmorStand.id, EquipmentSlot.HEAD, tapItemStack).sendAll()
        }
    }

    /**
     * This runs every tick until it is removed.
     */
    fun onUpdate() {
        if (dead) return
        if (!removed) {
            if (++ticks > 600) {
                removed = true
                return
            }
            position = armorStand?.location?: return
            position.let { tapArmorStand.setPosition(it.x + (move.x / 12), it.y + (move.y / 12), it.z + (move.z / 12)) }

            val vector = Vector(position.x, position.y, position.z)
            val target = Vector(vector.x + (move.x / 8), vector.y + (move.y / 8), vector.z + (move.z / 8))

            ENTITY.metadata(armorStand).sendAll()
            ENTITY.equipment(tapArmorStand.id, EquipmentSlot.HEAD, tapItemStack).sendAll()
            tapArmorStand.let { ENTITY.teleport(armorStand, it.posX, it.posY, it.posZ, it.yaw, it.pitch, false).sendAll() }

            Tap.MATH.rayTraceBlock(position.world, vector, target, 0)?.let {
                removed = true
                return
            }

            var foundPlayer: Player? = null
            var distance = 0.0
            getOnlinePlayers()?.forEach { player ->
                if (player != owner && player.isValid && player.gameMode != SPECTATOR) {
                    wrapPlayer(player)?.boundingBox?.expand(1.0, 2.0, 1.0)?.calculateRayTrace(vector, target)?.let {
                        val location = player.location
                        val current = position.toVector().distance(BukkitVector(location.x, location.y, location.z))
                        if (current < distance || distance == 0.0) {
                            distance = current
                            foundPlayer = player
                        }
                    }
                }
            }
            foundPlayer?.let {
                val location = it.location
                EFFECT.firework(builder().color(fromRGB(nextInt(0xFFFFFF)).asRGB()).type(STAR).build(), location.x, location.y, location.z).sendAll()
                it.noDamageTicks = 0
                if (it.health > 3.3) it.health -= 3.3 else {
                    val team = (slowFpsPlayers[it]?: return).team
                    broadcastMessage("${team.displayName}${WHITE}는 FPS 게임에서 ${(slowFpsPlayers[owner]?: return).team.displayName}${WHITE}의 투사체에 맞아 사망했습니다.")
                    team.dead = true
                    it.gameMode = SPECTATOR
                }
                it.velocity = move.multiply(2)?.let { vector -> BukkitVector(vector.x, vector.y, vector.z) }
                removed = true
            }
        } else destroy()
    }

    /**
     * This destroys the projectile by sending packet to everyone.
     */
    fun destroy() {
        dead = true
        ENTITY.destroy(tapArmorStand.id).sendAll()
    }
}
