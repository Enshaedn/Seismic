package com.enshaedn.seismic.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.enshaedn.seismic.database.SeismicDao
import kotlinx.coroutines.launch

class SessionsListViewModel(val database: SeismicDao) : ViewModel()
{
    private val TAG = "SEISMIC_LOG"
    val sessions = database.getAllSessions()

    private val _navigateToSessionDetail = MutableLiveData<Long?>()
    val navigateToSessionDetail
        get() = _navigateToSessionDetail

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

    fun onSessionClicked(id: Long) {
        _navigateToSessionDetail.value = id
    }

    fun onSessionDetailNavigated() {
        _navigateToSessionDetail.value = null
    }
}