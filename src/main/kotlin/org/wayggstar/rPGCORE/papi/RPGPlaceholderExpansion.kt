package org.wayggstar.rPGCORE.papi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.stat.StatDataManager

class RPGPlaceholderExpansion: PlaceholderExpansion() {
    override fun getIdentifier() = "rpgcore"
    override fun getAuthor() = "wayggstar"
    override fun getVersion() = "1.0"

    override fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        if (player == null) return null

        val data = StatDataManager.get(player)
        val value = data.getStatWithModifiers(identifier)
        Bukkit.getLogger().info("[PAPI] Placeholder requested: $identifier = $value")
        return value.toString()
    }
}