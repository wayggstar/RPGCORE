package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.wayggstar.rPGCORE.RPGCORE
import java.util.*

object ItemBuilder {
    fun build(itemData: ItemData): ItemStack {
        val item = ItemStack(itemData.material)
        val meta = item.itemMeta ?: return item

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemData.displayName))

        val replacedLore = itemData.lore.map { line ->
            var replaced = ChatColor.translateAlternateColorCodes('&', line)
            itemData.weaponStats.forEach { (key, value) ->
                val formatted = if (value % 1.0 == 0.0) value.toInt().toString() else "%.2f".format(value)
                replaced = replaced.replace("{$key}", formatted)
            }
            replaced
        }

        meta.lore = replacedLore

        itemData.customModelData?.let {
            meta.setCustomModelData(it)
        }

        meta.persistentDataContainer.set(
            NamespacedKey(RPGCORE.instance, "item-id"),
            PersistentDataType.STRING,
            itemData.id
        )

        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_ENCHANTS
        )
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE)
        meta.addAttributeModifier(
            Attribute.GENERIC_ATTACK_DAMAGE,
            AttributeModifier(UUID.randomUUID(), "nullify", 0.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        )

        item.itemMeta = meta
        return item
    }
}