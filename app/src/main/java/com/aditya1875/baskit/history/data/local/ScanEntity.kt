package com.aditya1875.baskit.history.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val barcode: String,
    val productName: String,
    val brand: String?,
    val imageFrontUrl: String?,
    val score: Int,
    val calories: Int?,
    val scannedAt: Long = System.currentTimeMillis()
)