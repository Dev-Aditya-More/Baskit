package com.aditya1875.baskit.history.presentation.utils

import com.aditya1875.baskit.history.data.local.ScanEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun groupByDate(scans: List<ScanEntity>): List<Pair<String, List<ScanEntity>>> {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val yesterday = today - 86_400_000L
    val fullFmt = SimpleDateFormat("MMMM d", Locale.getDefault())

    return scans
        .groupBy { scan ->
            val t = scan.scannedAt
            when {
                t >= today -> "Today"
                t >= yesterday -> "Yesterday"
                else -> fullFmt.format(Date(t))
            }
        }
        .entries
        .map { it.key to it.value }
}