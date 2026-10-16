package me.bumiller.ddcomp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import me.bumiller.ddcomp.core.database.dao.base.AggregateDao
import me.bumiller.ddcomp.core.database.entity.PersonEntity

@Dao
interface PersonDao: AggregateDao<PersonEntity, PersonEntity.Aggregate> {

    @Upsert
    override suspend fun upsert(entity: PersonEntity): Long

    @Delete
    override suspend fun delete(entity: PersonEntity): Int

    @Query("SELECT * FROM persons WHERE persons.id = :id")
    override fun getById(id: Long): Flow<PersonEntity?>

    @Query("SELECT * FROM persons")
    override fun getAll(): Flow<List<PersonEntity>>

    @Query("SELECT * FROM persons WHERE persons.id = :id")
    override fun getByIdAggregate(id: Long): Flow<PersonEntity.Aggregate?>

    @Query("SELECT * FROM persons")
    override fun getAllAggregate(): Flow<List<PersonEntity.Aggregate>>

    @Query("DELETE FROM persons")
    override suspend fun deleteAll()
}