package com.enshaedn.seismic.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enshaedn.seismic.database.SeismicDao
import kotlinx.coroutines.launch

class SeismicFinalizeViewModel(
    private val sessionKey: Long = 0L,
    val database: SeismicDao
    ) : ViewModel() {
    private val TAG = "SEISMIC_LOG"

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    fun doneNavigating() {
        _navigateToHome.value = null
    }

    fun onSaveSessionData(title: String, note: String) {
        viewModelScope.launch {
            val session = database.get(sessionKey) ?: return@launch
            session.title = title
            session.note = note
            database.updateSession(session)

            _navigateToHome.value = true
        }
    }
}