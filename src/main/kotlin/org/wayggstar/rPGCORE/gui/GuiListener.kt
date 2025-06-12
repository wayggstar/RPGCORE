package org.wayggstar.rPGCORE.gui

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.stat.StatApplier

class GuiListener: Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent){
        StatGui.handleClickEvent(e)
        val player = e.whoClicked as Player
        Bukkit.getScheduler().runTaskLater(RPGCORE.instance, Runnable {
            StatApplier.apply(player)
        }, 1L)
    }
}