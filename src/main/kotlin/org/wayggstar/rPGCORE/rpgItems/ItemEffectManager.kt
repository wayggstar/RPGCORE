package org.wayggstar.rPGCORE.rpgItems


import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.adapters.AbstractLocation
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.wayggstar.rPGCORE.configs.ConfigManager
import org.wayggstar.rPGCORE.stat.StatDataManager
import org.wayggstar.rPGCORE.stat.StatModifier
import io.lumine.mythic.api.skills.SkillTrigger
import io.lumine.mythic.bukkit.BukkitAPIHelper
import io.lumine.mythic.core.skills.SkillTriggers
import org.wayggstar.rPGCORE.RPGCORE
import org.wayggstar.rPGCORE.stat.effects.getMagicDamage

object ItemEffectManager {

    fun handleOnHitEffects(attacker: Player, target: Entity, weapon: ItemStack) {
        if (!ConfigManager.rpgItemsEnabled) return
        val itemId = ItemUtils.getItemId(weapon) ?: return
        val itemData = ItemDataLoader.getItemById(itemId) ?: return

        itemData.effects.filter { it.type == EffectType.ON_HIT }.forEach { effect ->
            val chance = effect.chance ?: 1.0
            if (Math.random() <= chance) {
                applyEffect(attacker, target, effect)
            }
        }
    }

    fun handleOnEquip(player: Player, itemData: ItemData) {
        itemData.effects.filter { it.type == EffectType.ON_EQUIP }.forEach { effect ->
            applyStatEffect(player, effect, "equip-${itemData.id}")
        }
    }

    fun handlePassive(player: Player) {
        if (!ConfigManager.rpgItemsEnabled) return

        val equippedItems = listOfNotNull(
            player.inventory.helmet,
            player.inventory.chestplate,
            player.inventory.leggings,
            player.inventory.boots,
            player.inventory.itemInMainHand,
            player.inventory.itemInOffHand
        ).filter { it.type.isAir.not() }

        equippedItems.forEach { item ->
            val itemId = ItemUtils.getItemId(item) ?: return@forEach
            val data = ItemDataLoader.getItemById(itemId) ?: return@forEach
            data.effects.filter { it.type == EffectType.PASSIVE }.forEach { effect ->
                applyStatEffect(player, effect, "passive-$itemId")
            }
        }
    }

    fun applyEffect(attacker: Player, target: Entity, effect: ItemEffect) {

        val chance = effect.chance ?: 1.0
        val rand = Math.random()
        if (rand > chance) {
            RPGCORE.instance.logger.info("효과 발동 실패 (확률 미달)")
            return
        }

        when (effect.action) {
            "CAST_MYTHICMOB_SKILL" -> {
                if (target !is LivingEntity) {
                    return
                }

                val skillName = effect.skill ?: run {
                    RPGCORE.instance.logger.info("스킬 이름이 null임")
                    return
                }

                val origin = attacker.location
                val power = attacker.getMagicDamage().toFloat()
                try {
                    MythicBukkit.inst().apiHelper.castSkill(
                        attacker,
                        skillName,
                        origin,
                        listOf(target),
                        listOf(origin),
                        power
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            else -> {
                RPGCORE.instance.logger.info("알 수 없는 액션: ${effect.action}, 처리 생략")
            }
        }
    }

    private fun applyStatEffect(player: Player, effect: ItemEffect, source: String) {
        if (effect.stat != null && effect.value != null) {
            StatDataManager.get(player).addModifier(
                StatModifier(source, effect.stat, effect.value)
            )
        }
    }
}