package org.wayggstar.rPGCORE.gui

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.msg.MessageManager
import org.wayggstar.rPGCORE.stat.*

object StatGui {
    private val guiConfig get() = ConfigManager.guiConfig

    fun open(player: Player) {
        val rows = guiConfig.getInt("gui.rows")
        val title = guiConfig.getString("gui.title") ?: "Stat GUI"
        val size = rows * 9
        val bgSection = guiConfig.getConfigurationSection("gui.background-item") ?: return
        val holder = StatGUIHolder()
        val inventory = Bukkit.createInventory(holder, size, title)
        holder.setInventory(inventory)

        val data = StatDataManager.get(player)

        // 배경 아이템
        val bgMaterial = Material.matchMaterial(bgSection.getString("material")?.uppercase() ?: "GRAY_STAINED_GLASS_PANE")
            ?: Material.GRAY_STAINED_GLASS_PANE
        val bgDisplayName = bgSection.getString("display-name") ?: " "
        val bgLore = bgSection.getStringList("lore")
        val bgItem = ItemStack(bgMaterial).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName(bgDisplayName)
                lore = bgLore
            }
        }

        // 배경 채우기
        for (i in 0 until size) {
            inventory.setItem(i, bgItem)
        }

        // 스텟 아이템
        val statList = guiConfig.getMapList("gui.stats")
        for (entry in statList) {
            val id = entry["id"] as? String ?: continue
            val slot = entry["slot"] as? Int ?: continue
            val def = StatRegistry.get(id) ?: continue

            val lore = replaceStatPlaceholders(def.description, id, data, def)

            val statItem = ItemStack(def.icon).apply {
                itemMeta = itemMeta?.apply {
                    setDisplayName(def.displayName)
                    this.lore = lore
                }
            }

            inventory.setItem(slot, statItem)
        }

        player.openInventory(inventory)
    }

    fun handleClickEvent(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val holder = e.inventory.holder as? StatGUIHolder ?: return

        e.isCancelled = true
        val clickedSlot = e.slot
        val data = StatDataManager.get(player)

        val statList = guiConfig.getMapList("gui.stats")
        for (entry in statList) {
            val id = entry["id"] as? String ?: continue
            val slot = entry["slot"] as? Int ?: continue

            if (clickedSlot == slot) {
                if (data.availablePoints > 0) {
                    data.baseStats[id] = data.stats.getOrDefault(id, 0) + 1
                    data.availablePoints -= 1
                    open(player)
                } else {
                    player.sendMessage(
                        MessageManager.get(
                            "no-points-left"
                        )
                    )
                }
                return
            }
        }
    }
}

class StatGUIHolder : InventoryHolder {
    private lateinit var inventory: Inventory

    override fun getInventory(): Inventory = inventory
    fun setInventory(inventory: Inventory) {
        this.inventory = inventory
    }
}