package org.wayggstar.rPGCORE.stat.effects

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.msg.MessageManager
import org.wayggstar.rPGCORE.rpgItems.ItemDataLoader
import org.wayggstar.rPGCORE.rpgItems.ItemUtils
import kotlin.math.max
import kotlin.random.Random

class StatCombatListener: Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val attacker = event.damager as? Player ?: return
        val target = event.entity as? LivingEntity ?: return

        val msg = MessageManager
        val weapon = attacker.inventory.itemInMainHand
        val itemId = ItemUtils.getItemId(weapon)
        val itemData = itemId?.let { ItemDataLoader.getItemById(it) }

        val baseDamage = event.damage
        val weaponDamage = itemData?.weaponStats?.get("weapon-damage") ?: 0.0
        var damage = baseDamage + weaponDamage


        // 크리티컬 계산 (플레이어, 몬스터 모두 적용)
        val critChance = attacker.getCritChance() + (itemData?.weaponStats?.get("weapon-crit-chance") ?: 0.0)
        val critDamage = attacker.getCritDamageMultiplier()

        if (Random.nextDouble() < critChance / 100.0) {
            damage *= critDamage
            attacker.sendMessage(msg.get("combat.crit", mapOf("multiplier" to "%.2f".format(critDamage))))
        }

        if (target is Player) {
            // 회피
            val dodgeChance = target.getDodgeChance()
            if (Random.nextDouble() < dodgeChance / 100.0) {
                event.isCancelled = true
                target.sendMessage(msg.get("combat.dodge"))
                attacker.sendMessage(msg.get("combat.dodge-notify"))
                return
            }

            // 방어력 계산
            val baseDefense = target.getDefense()
            val armorPenPercent = itemData?.weaponStats?.get("weapon-armor-penetration-percent") ?: 0.0
            val reducedDefense = baseDefense * (1.0 - armorPenPercent / 100.0)
            val effectiveDefense = max(0.0, reducedDefense)
            val finalDamage = damage * (100.0 / (100.0 + effectiveDefense))
            event.damage = finalDamage

            // 생명력 흡수
            val lifestealPercent = itemData?.weaponStats?.get("lifesteal-percent") ?: 0.0
            if (lifestealPercent > 0) {
                val heal = finalDamage * (lifestealPercent / 100.0)
                attacker.health = (attacker.health + heal).coerceAtMost(attacker.maxHealth)
            }

        } else {
            // 일반 몬스터 대상
            event.damage = damage

            // 생명력 흡수 (여기서도 적용)
            val lifestealPercent = itemData?.weaponStats?.get("lifesteal-percent") ?: 0.0
            if (lifestealPercent > 0) {
                val heal = damage * (lifestealPercent / 100.0)
                attacker.health = (attacker.health + heal).coerceAtMost(attacker.maxHealth)
            }
        }
    }
}