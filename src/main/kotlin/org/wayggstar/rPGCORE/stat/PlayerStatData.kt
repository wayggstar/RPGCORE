package org.wayggstar.rPGCORE.stat

import java.util.UUID

class PlayerStatData(
    val uuid: UUID,
    var availablePoints: Int = 0,
    val baseStats: MutableMap<String, Int> = mutableMapOf()
) {
    private val modifiers = mutableListOf<StatModifier>()

    val stats: Map<String, Int>
        get() {
            val result = baseStats.toMutableMap()
            for (mod in modifiers) {
                val origin = result.getOrDefault(mod.stat, 0)
                result[mod.stat] = (origin + mod.value).toInt()
            }
            return result
        }

    fun addModifier(mod: StatModifier) {
        modifiers.add(mod)
    }

    fun removeModifiersBySource(source: String) {
        modifiers.removeIf { it.source == source }
    }

    fun clearModifiers() {
        modifiers.clear()
    }

    fun getBaseStat(stat: String): Int {
        return baseStats.getOrDefault(stat, 0)
    }

    fun getStatWithModifiers(stat: String): Int {
        val base = getBaseStat(stat)
        val bonus = modifiers.filter { it.stat == stat }.sumOf { it.value }
        return (base + bonus).toInt()
    }

    fun hasStatAtLeast(stat: String, required: Int): Boolean {
        return getStatWithModifiers(stat) >= required
    }
}
