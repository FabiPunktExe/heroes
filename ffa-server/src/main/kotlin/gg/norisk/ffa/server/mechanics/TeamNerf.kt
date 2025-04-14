package gg.norisk.ffa.server.mechanics

import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.core.task.mcCoroutineTask
import kotlin.math.pow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TeamNerf {
    val hits: MutableMap<PlayerEntity, MutableMap<PlayerEntity, Long>> = mutableMapOf()

    fun init() {
        mcCoroutineTask(sync = true, client = false, period = 1.toDuration(DurationUnit.SECONDS)) {
            hits.forEach {
                val player = it.key
                if (!player.isAlive) {
                    hits.remove(player)
                    return@forEach
                }
                val hitMap = it.value
                hitMap.forEach { target, time ->
                    if (!target.isAlive || player.world != target.world || player.distanceTo(target) > 20) {
                        hitMap.remove(target)
                    }
                }
            }
        }
    }

    fun onAttack(player: PlayerEntity, target: PlayerEntity) {
        if (!hits.contains(player)) {
            hits[player] = mutableMapOf()
        }
        val hits = hits[player]!!
        hits[target] = System.currentTimeMillis()
    }

    fun getDamageAgainst(player: PlayerEntity, target: PlayerEntity, damage: Float): Float {
        val hits = hits[target] ?: mapOf()
        var teamSize = 0
        for (hit in hits) {
            val hitPlayer = hit.key
            if (hitPlayer != player) {
                teamSize++
            }
        }
        return if (teamSize > 1) damage * teamSize.toFloat().pow(-0.8f) else damage
    }
}