package me.bumiller.ddcomp.core.database.dao.base

interface M2MDao<M2M> {

    suspend fun insert(m2m: M2M)

    suspend fun delete(first: Long, second: Long): Int

}