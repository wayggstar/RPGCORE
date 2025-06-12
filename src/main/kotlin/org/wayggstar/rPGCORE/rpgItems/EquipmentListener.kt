package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.stat.StatDataManager

class EquipmentListener: Listener {

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val item = e.cursor ?: return
        if (item.type.isAir) return

        val itemId = ItemUtils.getItemId(item) ?: return
        val itemData = ItemDataLoader.getItemById(itemId) ?: return

        if (!ItemRequirementChecker.canEquip(player, itemData)) {
            e.isCancelled = true
            player.sendMessage("§c해당 아이템을 착용하기 위한 조건을 만족하지 않습니다.")
            val missing = ItemRequirementChecker.getMissingRequirements(player, itemData)
            missing.forEach { player.sendMessage("§7- $it") }
        }else{
            EquipmentManager.updateEquipment(player)
        }
    }

    @EventHandler
    fun onItemHeld(e: PlayerItemHeldEvent) {
        val player = e.player
        val newItem = player.inventory.getItem(e.newSlot) ?: return
        val itemId = ItemUtils.getItemId(newItem) ?: return
        val itemData = ItemDataLoader.getItemById(itemId) ?: return

        if (!ItemRequirementChecker.canEquip(player, itemData)) {
            e.isCancelled = true
            player.sendMessage("§c이 무기를 사용하기 위한 조건을 만족하지 않습니다.")
        }else{
            EquipmentManager.updateEquipment(player)
        }
    }
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        if (!ConfigManager.rpgItemsEnabled) return
        val player = event.player
        Bukkit.getScheduler().runTaskLater(RPGCORE.instance, Runnable {
            EquipmentManager.updateEquipment(player)
            ItemEffectManager.handlePassive(player)
        }, 5L)
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand
        if (item == null || item.type.isAir) return

        val itemId = ItemUtils.getItemId(item)
        if (itemId == null) return

        val itemData = ItemDataLoader.getItemById(itemId)

        if (itemData == null) return

        val clickType = when {
            e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK -> {
                if (player.isSneaking) "SHIFT_RIGHT_CLICK" else "RIGHT_CLICK"
            }
            e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK -> {
                if (player.isSneaking) "SHIFT_LEFT_CLICK" else "LEFT_CLICK"
            }
            else -> return
        }

        val effects = itemData.effects.filter { it.type == EffectType.ON_CLICK && it.clickType == clickType }

        effects.forEach { effect ->
            ItemEffectManager.applyEffect(player, player, effect)
        }
        EquipmentManager.updateEquipment(player)
    }
}