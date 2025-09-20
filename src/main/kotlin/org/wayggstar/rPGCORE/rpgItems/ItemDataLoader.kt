package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.wayggstar.rPGCORE.RPGCORE
import java.io.File

object ItemDataLoader {
    val items = mutableMapOf<String, ItemData>()

    fun loadAllItems(){
        val itemFolder = File(RPGCORE.instance.dataFolder, "items")
        if (!itemFolder.exists()){itemFolder.mkdirs()}

        itemFolder.walkTopDown().filter { it.extension == "yml" }.forEach { file ->
            val config = YamlConfiguration.loadConfiguration(file)
            val item = parseItemData(config)
            items[item.id] = item
        }
    }

    fun getItemById(id: String): ItemData? = items[id]

    private fun parseItemData(config: YamlConfiguration): ItemData {
        return ItemData(
            id = config.getString("id") ?: "unknown",
            material = Material.valueOf(config.getString("material") ?: "STONE"),
            displayName = config.getString("display-name") ?: "&fUnknown Item",
            lore = config.getStringList("lore"),
            customModelData = config.getInt("custom-model-data").takeIf { it > 0 },
            requiremnets = config.getConfigurationSection("requirements")?.getValues(false)
                ?.mapValues { it.value.toString().toInt() } ?: emptyMap(),
            stats = config.getConfigurationSection("stats")?.getValues(false)
                ?.mapValues { it.value.toString().toDouble() } ?: emptyMap(),
            weaponStats = config.getConfigurationSection("weaponStats")?.getValues(false)
                ?.mapValues { it.value.toString().toDouble() } ?: emptyMap(),
            effects = parseEffects(config.getConfigurationSection("effects")),
            bind = config.getBoolean("bind") ?: false
        )
    }

    private fun parseEffects(section: org.bukkit.configuration.ConfigurationSection?): List<ItemEffect> {
        if (section == null) return emptyList()
        return section.getKeys(false).mapNotNull { key ->
            val s = section.getConfigurationSection(key) ?: return@mapNotNull null
            ItemEffect(
                type = EffectType.valueOf(s.getString("type") ?: "PASSIVE"),
                stat = s.getString("stat"),
                value = s.getDouble("value"),
                action = s.getString("action"),
                effect = s.getString("effect"),
                duration = s.getInt("duration"),
                amplifier = s.getInt("amplifier"),
                chance = s.getDouble("chance"),
                clickType = s.getString("click-type"),
                skill = s.getString("skill")
            )
        }
    }
}