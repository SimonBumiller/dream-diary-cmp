package me.bumiller.ddcmp.domain.model.stats

sealed interface Stat<StatType> {

    /**
     * Stat could not be calculated due to not enough base data.
     */
    class NotEnoughData<StatType> : Stat<StatType>

    /**
     * Stat could be calculated.
     */
    data class Present<StatType>(val stat: StatType) : Stat<StatType>

    fun <R> map(mapper: (StatType) -> R): Stat<R> = when (this) {
        is NotEnoughData -> NotEnoughData()
        is Present -> Present(mapper(stat))
    }

    fun notEnoughDataIf(predicate: (StatType) -> Boolean): Stat<StatType> =
        when (this) {
            is NotEnoughData -> NotEnoughData()
            is Present -> if (predicate(stat)) NotEnoughData() else Present(stat)
        }

    companion object {

        /**
         * Creates a stat object depending on the nullity.
         */
        fun <StatType> ofNullable(value: StatType?) =
            value?.let { Present<StatType>(value) }
                ?: NotEnoughData()

    }

}