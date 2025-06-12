package org.wayggstar.rPGCORE.stat.effects

object StatEffectHookRegistry {
    private val hooks = mutableMapOf<String, StatEffectHook>()

    fun register(hook: StatEffectHook){
        hooks[hook.key] = hook
    }
    fun get(key: String): StatEffectHook? = hooks[key]
    fun all(): Collection<StatEffectHook> = hooks.values
}