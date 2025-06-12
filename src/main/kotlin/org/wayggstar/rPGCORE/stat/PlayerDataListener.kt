package org.wayggstar.rPGCORE.stat

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.wayggstar.rPGCORE.RPGCORE

class PlayerDataListener: Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent){
        StatDataManager.load(e.player)
        Bukkit.getScheduler().runTaskLater(RPGCORE.instance, Runnable {
            StatApplier.apply(e.player)
        }, 1L)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent){
        StatDataManager.save(e.player)
    }
}