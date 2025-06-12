package org.wayggstar.rPGCORE.stat.effects

import org.bukkit.entity.Player

object StatMetaKeys {
    const val DEFENSE = "defense"
    const val DODGE_CHANCE = "dodge-chance"
    const val CRIT_CHANCE = "crit-chance"
    const val CRIT_DAMAGE = "crit-damage"
    const val ATTACK_SPEED = "attack-speed"
    const val LIFE_STEAL = "life-steal"
    const val ARMOR_PENETRATION = "armor-penetration"
    const val WEAPON_LIFESTEAL_PERCENT = "lifesteal-percent"
    const val MAGIC_DAMAGE = "magic-damage"
}

fun Player.getStatMetaDouble(key: String, default: Double = 0.0): Double {
    val meta = this.getMetadata(key)
    if (meta.isEmpty()) return default
    return meta.firstOrNull()?.asDouble() ?: default
}

fun Player.getDefense(): Double = getStatMetaDouble(StatMetaKeys.DEFENSE)
fun Player.getDodgeChance(): Double = getStatMetaDouble(StatMetaKeys.DODGE_CHANCE)
fun Player.getCritChance(): Double = getStatMetaDouble(StatMetaKeys.CRIT_CHANCE)
fun Player.getCritDamageMultiplier(): Double = getStatMetaDouble(StatMetaKeys.CRIT_DAMAGE, 1.0)
fun Player.getAttackSpeed(): Double = getStatMetaDouble(StatMetaKeys.ATTACK_SPEED, 0.0)
fun Player.getLifeSteal(): Double = getStatMetaDouble(StatMetaKeys.LIFE_STEAL, 0.0)
fun Player.getArmorPenetration(): Double = getStatMetaDouble(StatMetaKeys.ARMOR_PENETRATION, 0.0)
fun Player.getWeaponLifeStealPercent(): Double = getStatMetaDouble(StatMetaKeys.WEAPON_LIFESTEAL_PERCENT, 0.0)
fun Player.getMagicDamage(): Double = getStatMetaDouble(StatMetaKeys.MAGIC_DAMAGE, 1.0)