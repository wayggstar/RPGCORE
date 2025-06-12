package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.stat.StatDataManager

object ItemRequirementChecker {

    fun canEquip(player: Player, itemData: ItemData) : Boolean{
        val playerStats = StatDataManager.get(player).stats
        return itemData.requiremnets.all { (stat, required) ->
            playerStats.getOrDefault(stat, 0) >= required
        }
    }

    fun getMissingRequirements(player: Player, itemData: ItemData): List<String>{
        val playerStats = StatDataManager.get(player).stats
        return itemData.requiremnets.filter { (stat, required) ->
            playerStats.getOrDefault(stat, 0) < required
        }.map { (stat, required) -> "$stat ≥ $required 필요" }
    }
}