package me.bumiller.ddcomp.core.database.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.bumiller.ddcmp.domain.common.combineSafe
import me.bumiller.ddcmp.domain.infrastructure.PeriodicBackupRepository
import me.bumiller.ddcmp.domain.model.backup.PeriodicBackup
import me.bumiller.ddcomp.core.database.Mapping
import me.bumiller.ddcomp.core.database.dao.PeriodicBackupDao
import me.bumiller.ddcomp.core.database.dao.PeriodicBackupOrderDao
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupEntity
import me.bumiller.ddcomp.core.database.entity.PeriodicBackupOrderEntity

class PeriodicBackupRepository(
    private val backupDao: PeriodicBackupDao,
    private val orderDao: PeriodicBackupOrderDao
) : PeriodicBackupRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun Flow<List<PeriodicBackupEntity.Aggregate>>.aggregateWithOrders() =
        flatMapLatest { backups ->
            val backupsWithOrderFlows = backups.map { backup ->
                orderDao.getByIdAggregate(backup.order.id).map { order ->
                    backup to order
                }
            }

            combineSafe(backupsWithOrderFlows) { it.toList() }
        }

    fun Flow<List<Pair<PeriodicBackupEntity.Aggregate, PeriodicBackupOrderEntity.Aggregate?>>>.mapToBackups() =
        map { backupsWithOrders ->
            backupsWithOrders.map { (backup, order) ->
                if (order == null) error("No order for backup with id $backup found.")

                Mapping.periodicBackup(backup, order)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun forOrder(orderId: Long): Flow<List<PeriodicBackup>> =
        backupDao.getAllAggregateForOrder(orderId)
            .aggregateWithOrders()
            .mapToBackups()

    override val all: Flow<List<PeriodicBackup>> = backupDao
        .getAllAggregate()
        .aggregateWithOrders()
        .mapToBackups()

    override fun id(id: Long): Flow<PeriodicBackup?> =
        backupDao.getByIdAggregate(id)
            .map(::listOfNotNull)
            .aggregateWithOrders()
            .mapToBackups()
            .map(List<PeriodicBackup>::single)

    override suspend fun save(t: PeriodicBackup): Long =
        backupDao.upsert(Mapping.Reverse.periodicBackup(t))

    override suspend fun delete(id: Long): Boolean =
        backupDao.getById(id)
            .first()
            ?.let { backupDao.delete(it) > 0 } ?: false

    override suspend fun deleteAll() = backupDao.deleteAll()
}