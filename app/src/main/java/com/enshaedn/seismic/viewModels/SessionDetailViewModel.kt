package com.enshaedn.seismic.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session

class SessionDetailViewModel(private val sessionKey: Long = 0L, dataSource: SeismicDao) : ViewModel()
{
    val database = dataSource
    private val session: LiveData<Session>

    fun getSession() = session

    init {
        session = database.getSessionByID(sessionKey)
    }

    private val _navigateToSessionsList = MutableLiveData<Boolean?>()

    val navigateToSessionsList: LiveData<Boolean?>
        get() = _navigateToSessionsList

    fun doneNavigating() {
        _navigateToSessionsList.value = null
    }

    fun onClose() {
        _navigateToSessionsList.value = true
    }
}