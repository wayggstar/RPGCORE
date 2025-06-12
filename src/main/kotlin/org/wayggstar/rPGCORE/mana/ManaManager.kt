package org.wayggstar.rPGCORE.mana

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

object ManaManager {
    private val manaMap = mutableMapOf<UUID, PlayerManaData>()

    fun get(player: Player): PlayerManaData{
        return manaMap.getOrPut(player.uniqueId){PlayerManaData()}
    }

    fun setMana(player: Player, amount: Double){
        val data = get(player)
        data.currentMana = amount.coerceIn(0.0, data.maxMana)
    }

    fun regenAll(){
        Bukkit.getOnlinePlayers().forEach{
            val data = get(it)
            setMana(it, data.currentMana + data.regenRate)
        }
    }
}