package org.wayggstar.rPGCORE.rpgItems

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RPGItemCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("§c권한이 없습니다.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§c사용법: /rpgitem give <플레이어> <아이템ID> [수량] 또는 /rpgitem reload")
            return true
        }

        when (args[0].lowercase()) {
            "give" -> {
                if (args.size < 3) {
                    sender.sendMessage("§c사용법: /rpgitem give <플레이어> <아이템ID> [수량]")
                    return true
                }

                val target = Bukkit.getPlayer(args[1])
                if (target == null) {
                    sender.sendMessage("§c플레이어를 찾을 수 없습니다.")
                    return true
                }

                val itemId = args[2]
                val itemData = ItemDataLoader.getItemById(itemId)
                if (itemData == null) {
                    sender.sendMessage("§c존재하지 않는 아이템 ID입니다.")
                    return true
                }

                val amount = args.getOrNull(3)?.toIntOrNull() ?: 1
                val itemStack = ItemBuilder.build(itemData).apply {
                    this.amount = amount.coerceAtLeast(1)
                }

                target.inventory.addItem(itemStack)
                sender.sendMessage("§a${target.name}에게 아이템 ${itemData.displayName}을(를) ${amount}개 지급했습니다.")
                target.sendMessage("§a관리자로부터 아이템 ${itemData.displayName}을(를) ${amount}개 받았습니다.")
            }

            "reload" -> {
                ItemDataLoader.loadAllItems()
                sender.sendMessage("§aRPG 아이템 데이터를 리로드했습니다.")
            }

            else -> {
                sender.sendMessage("§c알 수 없는 명령어입니다. /rpgitem give 또는 /rpgitem reload 사용")
            }
        }

        return true
    }
}