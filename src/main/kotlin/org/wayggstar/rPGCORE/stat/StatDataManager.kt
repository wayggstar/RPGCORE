package org.wayggstar.rPGCORE.stat

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.level.LevelManager
import org.wayggstar.rPGCORE.level.PlayerLevelData
import org.wayggstar.rPGCORE.level.PlayerLevelDataManager
import java.io.File
import java.util.*

object StatDataManager {
    private val playerData = mutableMapOf<UUID, PlayerStatData>()
    private val dataFolder = File(RPGCORE.instance.dataFolder, "data")

    init {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
    }

    fun get(player: Player): PlayerStatData {
        return playerData.getOrPut(player.uniqueId) {
            PlayerStatData(player.uniqueId)
        }
    }

    fun get(uuid: UUID): PlayerStatData {
        return playerData.getOrPut(uuid) {
            PlayerStatData(uuid)
        }
    }

    fun set(uuid: UUID, data: PlayerStatData) {
        playerData[uuid] = data
    }

    fun save(player: Player){
        val data = get(player)
        val file = File(dataFolder, "${player.name}.yml")
        val config = YamlConfiguration()

        config.set("uuid", player.uniqueId.toString())
        config.set("level", PlayerLevelDataManager.get(player).level)
        config.set("exp", PlayerLevelDataManager.get(player).currentExp)
        config.set("available-points", data.availablePoints)

        config.createSection("stats", data.baseStats)
        config.save(file)
    }

    fun load(player: Player) {
        val file = File(dataFolder, "${player.name}.yml")
        if (!file.exists()) return

        val config = YamlConfiguration.loadConfiguration(file)
        val uuid = UUID.fromString(config.getString("uuid") ?: player.uniqueId.toString())
        val exp = config.getDouble("exp")
        val level = config.getInt("level", 1)
        val availablePoints = config.getInt("available-points", 0)
        val stats = mutableMapOf<String, Int>()

        val statsSection = config.getConfigurationSection("stats")
        statsSection?.getKeys(false)?.forEach {
            stats[it] = statsSection.getInt(it)
        }

        val data = PlayerStatData(
            uuid = uuid,
            availablePoints = availablePoints,
            baseStats = stats
        )

        val leveldata = PlayerLevelData(
            level = level,
            currentExp = exp
        )
        set(uuid, data)
        PlayerLevelDataManager.set(player, leveldata)
    }
}