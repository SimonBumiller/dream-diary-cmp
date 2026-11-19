package me.bumiller.ddcmp.domain.model.stats

public sealed interface Stat<StatType> {

    /**
     * Stat could not be calculated due to not enough base data.
     */
    public class NotEnoughData<StatType> : Stat<StatType>

    /**
     * Stat could be calculated.
     */
    public data class Present<StatType>(val stat: StatType) : Stat<StatType>

    public fun <R> map(mapper: (StatType) -> R): Stat<R> = when (this) {
        is NotEnoughData -> NotEnoughData()
        is Present -> Present(mapper(stat))
    }

    public fun notEnoughDataIf(predicate: (StatType) -> Boolean): Stat<StatType> =
        when (this) {
            is NotEnoughData -> NotEnoughData()
            is Present -> if (predicate(stat)) NotEnoughData() else Present(stat)
        }

    public companion object {

        /**
         * Creates a stat object depending on the nullity.
         */
        public fun <StatType> ofNullable(value: StatType?): Stat<StatType> =
            value?.let { Present(value) }
                ?: NotEnoughData()

    }

}