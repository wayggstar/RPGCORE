package org.wayggstar.rPGCORE.stat.effects

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.mana.ManaManager

object MoveSpeedPercentHook : StatEffectHook {
    override val key: String
        get() = "move-speed-per"

    override fun apply(player: Player, value: Double) {
        val attr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) ?: return
        val base = 0.1
        attr.baseValue = base * (1.0 + value / 100.0)
    }
}

object MoveSpeedAmountHook : StatEffectHook {
    override val key = "move-speed-amount"

    override fun apply(player: Player, value: Double) {
        val attr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) ?: return
        attr.baseValue = 0.1 + value
    }
}

object MaxHealthHook : StatEffectHook {
    override val key = "max-health"
    override fun apply(player: Player, value: Double) {
        val attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        attr.baseValue = 20.0 + value
    }
}

object AttackDamageHook : StatEffectHook {
    override val key = "attack-damage"
    override fun apply(player: Player, value: Double) {
        player.setMetadata("attack-damage", FixedMetadataValue(RPGCORE.instance, 1.0 + value))
        val attr = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) ?: return
        attr.baseValue = 1.0 + value
    }
}

object DefenseHook : StatEffectHook {
    override val key = "defense"
    override fun apply(player: Player, value: Double) {
        player.setMetadata("defense", FixedMetadataValue(RPGCORE.instance, value))
    }
}

object DodgeChanceHook : StatEffectHook {
    override val key = "dodge-chance"
    override fun apply(player: Player, value: Double) {
        player.setMetadata("dodge-chance", FixedMetadataValue(RPGCORE.instance, value))
    }
}

object CritChanceHook : StatEffectHook {
    override val key = "crit-chance"
    override fun apply(player: Player, value: Double) {
        player.setMetadata("crit-chance", FixedMetadataValue(RPGCORE.instance, value))
    }
}

object CritDamageHook : StatEffectHook {
    override val key = "crit-damage"
    override fun apply(player: Player, value: Double) {
        player.setMetadata("crit-damage", FixedMetadataValue(RPGCORE.instance, 1.0 + value / 100.0))
    }
}

object AttackSpeedHook : StatEffectHook {
    override val key = "attack-speed"

    override fun apply(player: Player, value: Double) {
        val attr = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED) ?: return
        attr.baseValue = 4.0 + value
    }
}

object LifeStealHook : StatEffectHook {
    override val key = "life-steal"

    override fun apply(player: Player, value: Double) {
        player.setMetadata(key, FixedMetadataValue(RPGCORE.instance, value))
    }
}

object ArmorPenetrationHook : StatEffectHook {
    override val key = "armor-penetration"

    override fun apply(player: Player, value: Double) {
        player.setMetadata(key, FixedMetadataValue(RPGCORE.instance, value))
    }
}

object LifeStealPercentHook : StatEffectHook {
    override val key = "lifesteal-percent"

    override fun apply(player: Player, value: Double) {
        player.setMetadata(key, FixedMetadataValue(RPGCORE.instance, value))
    }
}

object MagicDamageHook : StatEffectHook {
    override val key: String = "magic-damage"

    override fun apply(player: Player, value: Double) {
        val multiplier = 1.0 + value / 100.0
        player.setMetadata("magic-damage", FixedMetadataValue(RPGCORE.instance, multiplier))
    }
}

object MaxManaHook : StatEffectHook {
    override val key = "max-mana"

    override fun apply(player: Player, value: Double) {
        val data = ManaManager.get(player)
        data.maxMana = value
        data.currentMana = data.currentMana.coerceAtMost(data.maxMana)
    }
}

object ManaRegenHook : StatEffectHook {
    override val key = "mana-regen"

    override fun apply(player: Player, value: Double) {
        val data = ManaManager.get(player)
        data.regenRate = value
    }
}
