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
    ) : ViewModel()
{
    // 2-way-ish data binding for edit texts - need values to update Session info
    val sessionTitle = MutableLiveData<String>()
    val sessionNote = MutableLiveData<String>()

    // apparently a safer/better way of controlling if navigation is available/valid
    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    fun doneNavigating() {
        _navigateToHome.value = null
    }

    // function to trigger update of the session info and to navigate way from this screen
    fun onSaveSessionData() {
        viewModelScope.launch {
            val session = database.get(sessionKey) ?: return@launch
            if(sessionTitle.value == null) session.title = "Default Title" else session.title = sessionTitle.value!!
            if(sessionNote.value == null) session.note = "" else session.note = sessionNote.value!!
            database.update(session)

            _navigateToHome.value = true
        }
    }
}