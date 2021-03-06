package com.enshaedn.seismic.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enshaedn.seismic.database.SeismicDao
import java.lang.IllegalArgumentException

class SessionsListViewModelFactory(
    private val dataSource: SeismicDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionsListViewModel::class.java)) {
            return SessionsListViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}