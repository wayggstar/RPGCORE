package org.wayggstar.rPGCORE.level

import org.bukkit.entity.Player
import java.util.*

object PlayerLevelDataManager {
    private val playerDataMap = mutableMapOf<UUID, PlayerLevelData>()

    fun get(player: Player): PlayerLevelData {
        return playerDataMap.getOrPut(player.uniqueId) {
            PlayerLevelData()
        }
    }

    fun set(player: Player, data: PlayerLevelData) {
        playerDataMap[player.uniqueId] = data
    }

    fun getAll(): Map<UUID, PlayerLevelData> {
        return playerDataMap
    }

    fun remove(player: Player) {
        playerDataMap.remove(player.uniqueId)
    }

    fun clear() {
        playerDataMap.clear()
    }
}