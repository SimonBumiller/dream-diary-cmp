package me.bumiller.ddcmp.domain.model.stats

/**
 * Models different sections of histogram ranks.
 */
enum class HistogramRank(
    val descriptor: String
) {

    Lowest("lowest"),

    Low("low"),

    Average("average"),

    High("high"),

    Highest("highest")

}