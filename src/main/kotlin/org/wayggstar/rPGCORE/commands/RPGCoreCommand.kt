package org.wayggstar.rPGCORE.commands

import io.lumine.mythic.bukkit.utils.redis.jedis.Protocol
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.gui.StatGui
import org.wayggstar.rPGCORE.stat.StatApplier
import org.wayggstar.rPGCORE.stat.StatDataManager

class RPGCoreCommand:CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            StatGui.open(sender as? Player ?: return false)
            return true
        }

        when (args[0].lowercase()) {
            "reload" -> {
                if (sender.isOp) {
                    ConfigManager.loadAll(RPGCORE.instance)
                    sender.sendMessage("§a설정이 재로드되었습니다.")
                }else {
                    sender.sendMessage("§c권한이 없습니다.")
                    return true
                }
            }

            "give" -> {
                if (sender.isOp) {
                    if (args.size < 3) {
                        sender.sendMessage("§c사용법: /rpgcore give <플레이어> <양>")
                        return true
                    }

                    val target = Bukkit.getPlayer(args[1])
                    val amount = args[2].toIntOrNull()
                    if (target == null || amount == null) {
                        sender.sendMessage("§c플레이어 또는 수치를 확인해주세요.")
                        return true
                    }

                    val data = StatDataManager.get(target)
                    data.availablePoints += amount
                    sender.sendMessage("§a${target.name}에게 스탯 포인트 ${amount}개를 부여했습니다.")
                    target.sendMessage("§a관리자에게 스탯 포인트 ${amount}개를 부여받았습니다")
                }else {
                    sender.sendMessage("§c권한이 없습니다.")
                    return true
                }
            }

            "reset" -> {
                if (sender.isOp) {
                    if (args.size < 2) {
                        sender.sendMessage("§c사용법: /rpgcore reset <플레이어>")
                        return true
                    }

                    val target = Bukkit.getPlayer(args[1])
                    if (target == null) {
                        sender.sendMessage("§c해당 플레이어를 찾을 수 없습니다.")
                        return true
                    }

                    val data = StatDataManager.get(target)
                    data.baseStats.clear()
                    data.availablePoints = 0
                    sender.sendMessage("§a${target.name}의 스탯이 초기화되었습니다.")
                    target.sendMessage("§c당신의 스텟이 관리자에 의해서 초기화 되었습니다.")
                    StatApplier.apply(target)
                }else {
                    sender.sendMessage("§c권한이 없습니다.")
                    return true
                }

            }
            else -> sender.sendMessage("§c알 수 없는 명령어입니다.")
        }
        return true
    }
}