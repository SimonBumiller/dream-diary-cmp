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

    companion object {

        /**
         * Creates a stat object depending on the nullity.
         */
        fun <StatType> ofNullable(value: StatType?) =
            value?.let { Present<StatType>(value) }
                ?: NotEnoughData<StatType>()

    }

}