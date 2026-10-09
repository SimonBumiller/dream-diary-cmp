package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupOrderRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackupOrder
import me.bumiller.ddcmp.domain.model.backup.copy
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.PlatformMapping
import me.bumiller.ddcomp.core.database.dao.BackupDestinationDao
import me.bumiller.ddcomp.core.database.dao.PeriodicBackupOrderDao
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity
import kotlin.jvm.JvmName

class PeriodicBackupOrderRepository(
    private val orderDao: PeriodicBackupOrderDao,
    private val backupDestinationDao: BackupDestinationDao
): PeriodicBackupOrderRepository {

    @JvmName("mapToModelList")
    fun Flow<List<PeriodicBackupOrderEntity.Aggregate>>.mapToModel() =
        map { entities -> entities.map(Mapping::backupOrder) }

    @JvmName("mapToModelSingle")
    fun Flow<PeriodicBackupOrderEntity.Aggregate?>.mapToModel() =
        map(::listOfNotNull)
            .mapToModel()
            .map { it.singleOrNull() }

    override val activeOrder: Flow<PeriodicBackupOrder?> =
        orderDao.getActiveOrder()
            .mapToModel()

    override val all: Flow<List<PeriodicBackupOrder>> =
        orderDao.getAllAggregate()
            .mapToModel()

    override fun id(id: Long): Flow<PeriodicBackupOrder?> =
        orderDao.getByIdAggregate(id)
            .mapToModel()

    override suspend fun save(t: PeriodicBackupOrder): Long {
        val destinationId = backupDestinationDao.getById(t.destination.id)
            .first()
            .let { destinationEntity ->
                // If the destination is new, we also save it to prevent foreign key constraint issues.
                if(destinationEntity == null) {
                    val mappedDestination = PlatformMapping.backupDestination(t.destination)
                    backupDestinationDao.upsert(mappedDestination)
                } else destinationEntity.id
            }

        val updated = t.copy(
            destination = t.destination.copy(id = destinationId)
        )

        return orderDao.upsert(Mapping.Reverse.backupOrder(updated))
    }

    override suspend fun delete(id: Long): Boolean =
        orderDao.getById(id)
            .first()
            ?.let { orderDao.delete(it) > 0 } ?: false

    override suspend fun deleteAll() = orderDao.deleteAll()
}