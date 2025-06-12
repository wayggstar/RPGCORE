package org.wayggstar.rPGCORE

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.wayggstar.rPGCORE.commands.RPGCoreCommand
import org.wayggstar.rPGCORE.commands.RPGCoreTabCompleter
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.gui.GuiListener
import org.wayggstar.rPGCORE.level.LevelManager
import org.wayggstar.rPGCORE.mana.ManaManager
import org.wayggstar.rPGCORE.mm.MythicMobExpListener
import org.wayggstar.rPGCORE.msg.MessageManager
import org.wayggstar.rPGCORE.papi.RPGPlaceholderExpansion
import org.wayggstar.rPGCORE.rpgItems.EquipmentListener
import org.wayggstar.rPGCORE.rpgItems.ItemDataLoader
import org.wayggstar.rPGCORE.rpgItems.RPGItemCommand
import org.wayggstar.rPGCORE.stat.PlayerDataListener
import org.wayggstar.rPGCORE.stat.StatDataManager
import org.wayggstar.rPGCORE.stat.StatRegistry
import org.wayggstar.rPGCORE.stat.effects.*
import java.io.File
import kotlin.coroutines.Continuation

class RPGCORE : JavaPlugin() {

    companion object{
        lateinit var instance: RPGCORE
    }

    override fun onEnable() {
        instance = this

        ConfigManager.loadAll(this)
        ItemDataLoader.loadAllItems()
        MythicMobExpListener.register(this)
        EventRegister(this)
        getCommand("rpgcore")?.apply {
            setExecutor(RPGCoreCommand())
            tabCompleter = RPGCoreTabCompleter()
        }
        getCommand("rpgitem")?.setExecutor(RPGItemCommand())
        HooksRegister()
        PAPIchecker()
        manaTask(this)
        logger.info("§aRPGCORE Plugin Enabled")
    }

    private fun PAPIchecker(){
        if (server.pluginManager.isPluginEnabled("PlaceholderAPI")){
            RPGPlaceholderExpansion().register()
        }
    }
    private fun EventRegister(plugin: JavaPlugin){
        server.pluginManager.registerEvents(GuiListener(), plugin)
        server.pluginManager.registerEvents(PlayerDataListener(), plugin)
        server.pluginManager.registerEvents(StatCombatListener(), plugin)
        server.pluginManager.registerEvents(EquipmentListener(), plugin)
    }

    private fun HooksRegister(){
        StatEffectHookRegistry.register(MoveSpeedAmountHook)
        StatEffectHookRegistry.register(MoveSpeedPercentHook)
        StatEffectHookRegistry.register(MaxHealthHook)
        StatEffectHookRegistry.register(AttackDamageHook)
        StatEffectHookRegistry.register(DefenseHook)
        StatEffectHookRegistry.register(DodgeChanceHook)
        StatEffectHookRegistry.register(CritChanceHook)
        StatEffectHookRegistry.register(CritDamageHook)
        StatEffectHookRegistry.register(MagicDamageHook)
        StatEffectHookRegistry.register(MaxManaHook)
        StatEffectHookRegistry.register(ManaRegenHook)
    }

    override fun onDisable() {
        logger.info("§cRPGCORE Plugin Disabled")

        for (player in Bukkit.getOnlinePlayers()){
            StatDataManager.save(player)
        }
    }

    fun manaTask(plugin: JavaPlugin){
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
             run {
                 ManaManager.regenAll()
             }
        }, 20L, 20L)
    }
}
