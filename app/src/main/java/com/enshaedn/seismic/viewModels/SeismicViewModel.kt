package com.enshaedn.seismic.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session
import kotlinx.coroutines.launch

class SeismicViewModel(
    val database: SeismicDao, application: Application
    ) : AndroidViewModel(application) {
    private val TAG = "SEISMIC_LOG"
    private var currentSession = MutableLiveData<Session?>()
    private var activeKey = MutableLiveData<Long?>()
    private val sessions = database.getAllSessions()

    private val _navigateToActive = MutableLiveData<Long?>()
    val navigateToActive: LiveData<Long?>
        get() = _navigateToActive

    private val _navigateToSessionsList = MutableLiveData<Boolean?>()
    val navigateToSessionsList: LiveData<Boolean?>
        get() = _navigateToSessionsList

    fun onViewSessions() {
        _navigateToSessionsList.value = true
    }

    // Reset Navigation variables
    fun doneNavigating() {
        _navigateToActive.value = null
        _navigateToSessionsList.value = null
        Log.d(TAG, "Done Navigating to Active Screen - ${_navigateToActive.value}")
    }

    // Start Button Controller - may need to implement a continue button or hard stop any interruptions
    val startButtonVisible = Transformations.map(currentSession) {
        it == null
    }

    // Only make List Button active if there are sessions available to view
    val viewButtonVisible = Transformations.map(sessions) {
        it?.isNotEmpty()
    }

    init {
        initializeCurrentSession()
    }

    private fun initializeCurrentSession() {
        viewModelScope.launch { currentSession.value = getCurrentSessionFromDB() }
    }

    private suspend fun getCurrentSessionFromDB() : Session? {
        // Gets the last session in the DB, LIMIT 1
        var session = database.getCurrentSession()
        Log.d(TAG, "Start Time: ${session?.startTimeMilli} vs End Time: ${session?.endTimeMilli}")
        // If session is still active, return session else set to null so that a new session may be started
        if(session?.startTimeMilli != session?.endTimeMilli) {
            session = null
        }
        return session
    }

    fun onStartSession() {
        Log.d(TAG, "Start button clicked")
        viewModelScope.launch {
            // Instantiate a new Session to insert in DB
            val newSession = Session()
            insert(newSession)
            // Set current session value equal to latest session
            currentSession.value = getCurrentSessionFromDB()
            // Set navigateToActive value to the new PK for the new Session
            _navigateToActive.value = activeKey.value
        }
    }

    private suspend fun insert(session: Session) {
        // Set activeKey value to the new PK of the new Session
        activeKey.value = database.insert(session)
    }
}