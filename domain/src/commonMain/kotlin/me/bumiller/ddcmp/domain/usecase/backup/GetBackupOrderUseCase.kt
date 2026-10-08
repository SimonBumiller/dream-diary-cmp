package me.bumiller.ddcmp.domain.usecase.backup

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.usecase.backup.GetBackupOrderUseCase.I
import me.bumiller.ddcmp.domain.usecase.meta.FlowUseCase

class GetBackupOrderUseCase(
    private val orderRepo: PeriodicBackupOrderRepository
) : FlowUseCase<I, PeriodicBackupOrder?> {

    override fun invoke(i: I): Flow<PeriodicBackupOrder?> =
        if (i.active)
            orderRepo.activeOrder.map { order ->
                if (i.id == null || order?.id == i.id) order
                else null
            }
        else orderRepo.all.map { orders ->
            orders.find { it.id == i.id }
        }

    /**
     * Query for the usecase.
     */
    data class I(

        /**
         * The id to search for.
         */
        val id: Long? = null,

        /**
         * Whether to search for the currently active order.
         */
        val active: Boolean = true

    )

}