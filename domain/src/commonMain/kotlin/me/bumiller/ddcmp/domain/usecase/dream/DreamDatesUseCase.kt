package me.bumiller.ddcmp.domain.usecase.dream

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.DreamRepository
import me.bumiller.ddcmp.domain.model.dream.Dream
import me.bumiller.ddcmp.domain.usecase.dream.DreamDatesUseCase.O
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DreamDatesUseCase(
    private val repo: DreamRepository
) : FlowUseCase<Unit, O?> {

    override fun invoke(i: Unit): Flow<O?> = repo.all
        .map { dreams ->
            val first = dreams.minByOrNull { it.dreamt }
            val last = dreams.maxByOrNull { it.dreamt }

            if(first != null && last != null) O(first, last)
            else null
        }

    /**
     * The output type.
     */
    data class O(

        /**
         * The dream that was dreamt the earliest.
         */
        val firstDream: Dream,

        /**
         * The dream that was dreamt the latest.
         */
        val lastDream: Dream

    )

}