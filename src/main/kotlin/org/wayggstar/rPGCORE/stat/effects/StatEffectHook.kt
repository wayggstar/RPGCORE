package org.wayggstar.rPGCORE.stat.effects

import org.bukkit.entity.Player

interface StatEffectHook {
    val key: String
    fun apply(player: Player, value: Double)
}