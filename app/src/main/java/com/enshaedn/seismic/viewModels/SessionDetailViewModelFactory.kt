package com.enshaedn.seismic.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enshaedn.seismic.database.SeismicDao

class SessionDetailViewModelFactory(
    private val sessionKey: Long,
    private val dataSource: SeismicDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionDetailViewModel::class.java)) {
            return SessionDetailViewModel(sessionKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}