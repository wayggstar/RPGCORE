package org.wayggstar.rPGCORE.stat

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.stat.effects.StatEffectHookRegistry

object StatApplier {
    fun apply(player: Player) {
        val data = StatDataManager.get(player)

        for ((statId, level) in data.baseStats) {
            val def = StatRegistry.get(statId) ?: continue

            for ((effectId, perPoint) in def.effectPerPoint) {
                val hook = StatEffectHookRegistry.get(effectId) ?: continue
                val total = perPoint * level
                hook.apply(player, total)
            }
        }

        for (hook in StatEffectHookRegistry.all()) {
            if (!data.stats.any { StatRegistry.get(it.key)?.effectPerPoint?.containsKey(hook.key) == true }) {
                hook.apply(player, 0.0)
            }
        }
    }
}