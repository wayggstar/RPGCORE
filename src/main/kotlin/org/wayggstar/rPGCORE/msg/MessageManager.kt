package org.wayggstar.rPGCORE.msg

import net.md_5.bungee.api.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object MessageManager {
    private lateinit var messages: YamlConfiguration

    fun load(file: YamlConfiguration) {
        messages = file
    }

    fun get(key: String, placeholders: Map<String, Any> = emptyMap()): String {
        var msg = messages.getString(key) ?: "&c[$key] 메시지를 찾을 수 없습니다."
        placeholders.forEach { (k, v) ->
            msg = msg.replace("{$k}", v.toString())
        }
        return ChatColor.translateAlternateColorCodes('&', msg)
    }
}