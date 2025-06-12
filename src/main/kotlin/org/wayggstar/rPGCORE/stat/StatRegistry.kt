package org.wayggstar.rPGCORE.stat

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.ItemStack

object StatRegistry {
    val stats = mutableMapOf<String, StatDefinition>()

    fun loadFromConfig(config: FileConfiguration){
        val section = config.getConfigurationSection("stats") ?: return

        for (key in section.getKeys(false)){
            val statSec = section.getConfigurationSection(key) ?: continue

            val display = ChatColor.translateAlternateColorCodes('&',statSec.getString("display") ?: key)
            val icon = Material.matchMaterial(statSec.getString("icon") ?: "BARRIER") ?: Material.BARRIER
            val description = statSec.getStringList("description").map { ChatColor.translateAlternateColorCodes('&', it) }

            val effects = statSec.getConfigurationSection("per-point")?.getKeys(false)?.associateWith {
                statSec.getDouble("per-point.$it")
            } ?: emptyMap()

            stats[key] = StatDefinition(key, display, icon, description, effects)
        }
    }

    fun get(id: String): StatDefinition? = stats[id]
}