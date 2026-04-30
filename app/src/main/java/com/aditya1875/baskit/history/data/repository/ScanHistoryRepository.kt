package com.aditya1875.baskit.history.data.repository

import com.aditya1875.baskit.history.data.local.ScanDao
import com.aditya1875.baskit.history.data.local.ScanEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class ScanHistoryRepository(private val dao: ScanDao) {

    fun getRecent(limit: Int = 5): Flow<List<ScanEntity>> = dao.getRecent(limit)

    fun getAll(): Flow<List<ScanEntity>> = dao.getAll()

    fun getHealthy(): Flow<List<ScanEntity>> = dao.getHealthy()

    fun getUnhealthy(): Flow<List<ScanEntity>> = dao.getUnhealthy()

    fun getMonthCount(): Flow<Int> {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getCountSince(cal.timeInMillis)
    }

    suspend fun save(scan: ScanEntity) = dao.insert(scan)

    suspend fun delete(id: Int) = dao.deleteById(id)

    suspend fun clearAll() = dao.deleteAll()
}

enum class HistoryFilter { RECENT, HEALTHY, UNHEALTHY }