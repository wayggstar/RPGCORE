package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.stat.StatApplier
import org.wayggstar.rPGCORE.stat.StatDataManager
import org.wayggstar.rPGCORE.stat.StatModifier

object EquipmentManager {

    fun updateEquipment(player: Player){
        val statData = StatDataManager.get(player)
        statData.clearModifiers()

        val equipped = listOfNotNull(
            player.inventory.helmet,
            player.inventory.chestplate,
            player.inventory.leggings,
            player.inventory.boots,
            player.inventory.itemInMainHand,
            player.inventory.itemInOffHand
        )

        for (item in equipped){
            val id = ItemUtils.getItemId(item) ?: continue
            val itemData = ItemDataLoader.getItemById(id) ?: continue

            itemData.stats.forEach { (stat, value) ->
                statData.addModifier(StatModifier(id, stat, value))
            }

            ItemEffectManager.handlePassive(player)
        }

        StatApplier.apply(player)
    }
}