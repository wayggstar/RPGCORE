package org.wayggstar.rPGCORE.mm

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.level.LevelManager
import org.wayggstar.rPGCORE.level.PlayerLevelDataManager

object MythicMobExpListener: Listener {
    private val mobExpTable = mutableMapOf<String, Double>()

    fun load(config: FileConfiguration){
        mobExpTable.clear()
        val section = config.getConfigurationSection("mythicmobs.mobs") ?: return
        for (key in section.getKeys(false)){
            mobExpTable[key] = section.getDouble(key)
        }
    }

    fun register(plugin: Plugin){
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null){
            Bukkit.getPluginManager().registerEvents(this, plugin)
            RPGCORE.instance.logger.info("${ChatColor.GREEN}MythicMobs 연동 활성화됨.")
        }
    }

    @EventHandler
    fun onMythicMobDeath(e: MythicMobDeathEvent) {
        val mobType = e.mobType.internalName
        val exp = mobExpTable[mobType] ?: return

        val killer = e.killer as? Player ?: return
        val data = PlayerLevelDataManager.get(killer)
        LevelManager.addExp(killer, data, exp)
    }
}