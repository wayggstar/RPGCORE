package org.wayggstar.rPGCORE.configs

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import org.wayggstar.rPGCORE.level.LevelManager
import org.wayggstar.rPGCORE.mm.MythicMobExpListener
import org.wayggstar.rPGCORE.msg.MessageManager
import org.wayggstar.rPGCORE.stat.StatRegistry

object ConfigManager {
    lateinit var statsConfig: YamlConfiguration
        private set
    lateinit var guiConfig: YamlConfiguration
        private set
    lateinit var levelConfig: YamlConfiguration
        private set
    lateinit var messageConfig: YamlConfiguration
        private set
    var rpgItemsEnabled: Boolean = true

    fun loadAll(plugin: JavaPlugin) {
        plugin.saveDefaultConfig()
        val config = plugin.config
        plugin.saveResource("stat_definition.yml", false)
        plugin.saveResource("stat_gui.yml", false)
        plugin.saveResource("level.yml", false)
        plugin.saveResource("messages.yml", false)

        statsConfig = loadYaml(plugin, "stat_definition.yml")
        guiConfig = loadYaml(plugin, "stat_gui.yml")
        levelConfig = loadYaml(plugin, "level.yml")
        messageConfig = loadYaml(plugin, "messages.yml")
        rpgItemsEnabled = config.getBoolean("rpg-items.enabled", true)
        StatRegistry.loadFromConfig(statsConfig)
        LevelManager.load(levelConfig)
        MessageManager.load(messageConfig)

        MythicMobExpListener.load(levelConfig)
    }

    private fun loadYaml(plugin: JavaPlugin, name: String): YamlConfiguration {
        val file = plugin.dataFolder.resolve(name)
        if (!file.exists()) plugin.saveResource(name, false)
        return YamlConfiguration.loadConfiguration(file)
    }
}