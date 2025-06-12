package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.wayggstar.rPGCORE.RPGCORE

object ItemUtils {

    private val key = NamespacedKey(RPGCORE.instance, "item-id")

    fun getItemId(item: ItemStack?) : String? {
        if (item == null || !item.hasItemMeta()) {
            return null
        }
        val meta = item.itemMeta ?: return null
        val container = meta.persistentDataContainer
        return container.get(key, PersistentDataType.STRING)
    }

    fun hasItemId(item: ItemStack?): Boolean{
        return getItemId(item) != null
    }
}