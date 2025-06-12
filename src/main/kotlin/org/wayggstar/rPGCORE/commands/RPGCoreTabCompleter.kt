package org.wayggstar.rPGCORE.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class RPGCoreTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {

        if (!sender.isOp){
            return null
        }

        if (command.name.equals("rpgcore", ignoreCase = true)) {
            return when (args.size) {
                1 -> listOf("reload", "give", "reset").filter {
                    it.startsWith(args[0], ignoreCase = true)
                }

                2 -> when (args[0].lowercase()) {
                    "give", "reset" -> Bukkit.getOnlinePlayers()
                        .map { it.name }
                        .filter { it.startsWith(args[1], ignoreCase = true) }

                    else -> emptyList()
                }
                else -> emptyList()
            }
        }
        return null
    }
}