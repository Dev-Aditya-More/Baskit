package com.aditya1875.baskit.history.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scan: ScanEntity)

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC LIMIT :limit")
    fun getRecent(limit: Int = 5): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun getAll(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scan_history WHERE score > 60 ORDER BY scannedAt DESC")
    fun getHealthy(): Flow<List<ScanEntity>>

    @Query("SELECT * FROM scan_history WHERE score <= 60 ORDER BY scannedAt DESC")
    fun getUnhealthy(): Flow<List<ScanEntity>>

    @Query("SELECT COUNT(*) FROM scan_history WHERE scannedAt >= :startOfMonth")
    fun getCountSince(startOfMonth: Long): Flow<Int>

    @Query("DELETE FROM scan_history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM scan_history")
    suspend fun deleteAll()
}