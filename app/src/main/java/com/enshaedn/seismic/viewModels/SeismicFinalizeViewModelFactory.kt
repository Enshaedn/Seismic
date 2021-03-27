package com.enshaedn.seismic.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enshaedn.seismic.database.SeismicDao
import java.lang.IllegalArgumentException

class SeismicFinalizeViewModelFactory(
    private val sessionKey: Long,
    private val dataSource: SeismicDao
    ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SeismicFinalizeViewModel::class.java)) {
            return SeismicFinalizeViewModel(sessionKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}