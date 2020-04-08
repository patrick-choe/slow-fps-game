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
import org.bukkit.Bukkit.getOnlinePlayers
import org.bukkit.Color.fromRGB
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import kotlin.random.Random.Default.nextInt
import org.bukkit.util.Vector as BukkitVector

class SlowFpsProjectile(private val player: Player, private val move: BukkitVector, private val tapItemStack: TapItemStack) {
    private val tapArmorStand: TapArmorStand = Tap.ENTITY.createEntity(ArmorStand::class.java)
    private val armorStand: ArmorStand?
    private lateinit var position: Location
    private var ticks = 0
    private var removed = false
    var dead = false
        private set

    init {
        tapArmorStand.apply {
            isInvisible = true
            setGravity(false)
            setBasePlate(false)
            player.location.let { pos ->
                setPositionAndRotation(pos.x, pos.y - 0.5, pos.z, pos.yaw, 0F)
                setHeadPose(0F, 0F, pos.pitch + 45F)
            }
        }
        armorStand = tapArmorStand.bukkitEntity.apply {
            ENTITY.spawnMob(this).sendAll()
            ENTITY.metadata(this).sendAll()
            ENTITY.equipment(tapArmorStand.id, EquipmentSlot.HEAD, tapItemStack).sendAll()
        }
    }

    fun onUpdate() {
        if (dead) return
        if (!removed) {
            if (++ticks > 600) {
                removed = true
                return
            }
            position = armorStand?.location?: return
            position.let { tapArmorStand.setPosition(it.x + move.x, it.y + move.y, it.z + move.z) }

            val vector = Vector(position.x, position.y, position.z)
            val target = vector.add(move.x, move.y, move.z)

            ENTITY.metadata(armorStand).sendAll()
            ENTITY.equipment(tapArmorStand.id, EquipmentSlot.HEAD, tapItemStack).sendAll()
            tapArmorStand.let { ENTITY.teleport(armorStand, it.posX, it.posY, it.posZ, it.yaw, it.pitch, false).sendAll() }

            Tap.MATH.rayTraceBlock(position.world, vector, target, 0)?.let {
                removed = true
                return
            }

            var foundPlayer: Player? = null
            var distance = 0.0
            getOnlinePlayers()?.forEach {
                if (it != player && player.isValid) {
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
                val location = player.location
                EFFECT.firework(builder().color(fromRGB(nextInt(0xFFFFFF)).asRGB()).type(STAR).build(), location.x, location.y, location.z).sendAll()
                player.noDamageTicks = 0
                player.damage(0.5)
                player.velocity = move.normalize()?.multiply(2)?.let { vector -> BukkitVector(vector.x, vector.y, vector.z) }
                removed = true
            }
        } else destroy()
    }

    private fun destroy() {
        dead = true
        ENTITY.destroy(tapArmorStand.id)
    }
}
