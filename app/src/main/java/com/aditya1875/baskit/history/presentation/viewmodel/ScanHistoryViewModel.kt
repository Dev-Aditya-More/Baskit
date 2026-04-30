package com.aditya1875.baskit.history.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.baskit.history.data.local.ScanEntity
import com.aditya1875.baskit.history.data.repository.HistoryFilter
import com.aditya1875.baskit.history.data.repository.ScanHistoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScanHistoryViewModel(
    private val repository: ScanHistoryRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(HistoryFilter.RECENT)
    val filter: StateFlow<HistoryFilter> = _filter.asStateFlow()

    /** Flat list filtered by selected tab */
    @OptIn(ExperimentalCoroutinesApi::class)
    val scans: StateFlow<List<ScanEntity>> = _filter
        .flatMapLatest { f ->
            when (f) {
                HistoryFilter.RECENT -> repository.getAll()
                HistoryFilter.HEALTHY -> repository.getHealthy()
                HistoryFilter.UNHEALTHY -> repository.getUnhealthy()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** 5 most recent for home screen widget */
    val recentScans: StateFlow<List<ScanEntity>> = repository
        .getRecent(5)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthCount: StateFlow<Int> = repository
        .getMonthCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setFilter(f: HistoryFilter) { _filter.value = f }

    fun save(scan: ScanEntity) = viewModelScope.launch { repository.save(scan) }

    fun delete(id: Int) = viewModelScope.launch { repository.delete(id) }
}