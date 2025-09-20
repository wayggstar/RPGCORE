package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.Material

data class ItemData(
    val id: String,
    val material: Material,
    val displayName: String,
    val lore: List<String>,
    val customModelData: Int?,
    val requiremnets: Map<String, Int>,
    val stats: Map<String, Double>,
    val weaponStats: Map<String, Double>,
    val effects: List<ItemEffect>,
    val bind: Boolean?
)

data class ItemEffect(
    val type: EffectType,
    val clickType: String? = null,
    val action: String? = null,
    val stat: String? = null,
    val value: Double? = null,
    val effect: String? = null,
    val duration: Int? = null,
    val amplifier: Int? = null,
    val chance: Double? = null,
    val spell: String? = null,
    val skill: String? = null
)

enum class EffectType {
    PASSIVE, ON_HIT, ON_EQUIP, ON_CLICK
}