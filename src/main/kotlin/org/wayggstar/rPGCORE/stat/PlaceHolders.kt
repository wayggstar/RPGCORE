package org.wayggstar.rPGCORE.stat

fun replaceStatPlaceholders(
    lines: List<String>,
    statId: String,
    data: PlayerStatData,
    def: StatDefinition
): List<String> {
    val current = data.stats.getOrDefault(statId, 0)
    val availablePoints = data.availablePoints

    return lines.map { line ->
        var replaced = line
            .replace("{current_stat}", current.toString())
            .replace("{stat_name}", def.displayName)
            .replace("{stat_id}", statId)
            .replace("{statpoints}", availablePoints.toString())
            .replace("{max_stat}", def.max.toString())

        def.effectPerPoint.forEach { (varName, valuePerPoint) ->
            val value = valuePerPoint * current
            val displayValue = if (value % 1.0 == 0.0) {
                value.toInt().toString()
            } else {
                "%.2f".format(value)
            }
            replaced = replaced.replace("{$varName}", displayValue)
        }
        replaced
    }
}