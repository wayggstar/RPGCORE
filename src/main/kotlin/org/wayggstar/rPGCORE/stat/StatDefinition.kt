package org.wayggstar.rPGCORE.stat

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class StatDefinition(
    val id: String,
    val displayName: String,
    val icon: Material,
    val description: List<String>,
    val effectPerPoint: Map<String, Double>,
    val max: Int = Int.MAX_VALUE
)
