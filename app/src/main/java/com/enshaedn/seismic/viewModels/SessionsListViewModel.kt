package com.enshaedn.seismic.viewModels

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enshaedn.seismic.database.SeismicDao
import kotlinx.coroutines.launch

class SessionsListViewModel(val database: SeismicDao) : ViewModel()
{
    private val TAG = "SEISMIC_LOG"
    val sessions = database.getAllSessions()

    val clearButtonVisible = Transformations.map(sessions) {
        it?.isNotEmpty()
    }

    fun onClear() {
        Log.d(TAG, "Clear Table")
        viewModelScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        database.clearSessions()
    }
}