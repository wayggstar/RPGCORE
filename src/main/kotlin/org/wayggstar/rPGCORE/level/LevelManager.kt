package org.wayggstar.rPGCORE.level

import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.msg.MessageManager
import org.wayggstar.rPGCORE.stat.StatDataManager

object LevelManager {
    private val expTable = mutableMapOf<Int, Double>()
    private val pointsTable = mutableMapOf<Int, Int>()

    private var baseExp = 1000.0
    private var multiplier = 1.2
    private var defaultStatPoints = 1

    fun load(config: FileConfiguration){
        baseExp = config.getDouble("base-exp", 1000.0)
        multiplier = config.getDouble("default-multiplier", 1.2)

        val levelSection = config.getConfigurationSection("levels")
        levelSection?.getKeys(false)?.forEach {
            val level = it.toIntOrNull() ?: return@forEach
            expTable[level] = levelSection.getDouble(it)
        }

        val pointSection = config.getConfigurationSection("points-per-level")
        defaultStatPoints = pointSection?.getInt("default", 1) ?: 1
        pointSection?.getKeys(false)?.forEach {
            if (it == "default") return@forEach
            val level = it.toIntOrNull() ?: return@forEach
            pointsTable[level] = pointSection.getInt(it)
        }
    }

    fun getRequiredExp(level: Int): Double {
        return expTable[level] ?: (baseExp * Math.pow(multiplier, (level - 1).toDouble()))
    }

    private fun getStatPointsForLevel(level: Int): Int {
        return pointsTable[level] ?: defaultStatPoints
    }

    fun addExp(player: Player, data: PlayerLevelData, exp: Double) {
        data.currentExp += exp
        val expMsg = MessageManager.get(
            "exp-gain",
            mapOf("exp" to String.format("%.1f", exp))
        )
        player.sendMessage(expMsg)

        while (data.currentExp >= getRequiredExp(data.level + 1)) {
            data.currentExp -= getRequiredExp(data.level + 1)
            data.level++
            val statPoints = getStatPointsForLevel(data.level)
            val statdata = StatDataManager.get(player)
            statdata.availablePoints += statPoints

            val levelUpMsg = MessageManager.get(
                "level-up",
                mapOf("level" to data.level.toString(), "points" to statPoints.toString())
            )
            player.sendMessage(levelUpMsg)
        }

        val nextLevExp = getRequiredExp(data.level + 1)
        player.exp = (data.currentExp / nextLevExp).coerceIn(0.0, 1.0).toFloat()
        player.level = data.level
    }
}