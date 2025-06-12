package org.wayggstar.rPGCORE.mana

data class PlayerManaData(
    var currentMana: Double = 100.0,
    var maxMana: Double = 100.0,
    var regenRate: Double = 1.5
)
