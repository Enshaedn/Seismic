package com.enshaedn.seismic.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.*
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.Measurement
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session
import com.enshaedn.seismic.database.SessionMeasurements
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.concurrent.ThreadLocalRandom

class SeismicViewModel(
    val database: SeismicDao, application: Application
    ) : AndroidViewModel(application) {
    private val TAG = "SEISMIC_LOG"
    private var currentSession = MutableLiveData<Session?>()
    private val sessions = database.getAllSessions()
    private val measurements = database.getAllMeasurements()
    private var cM = MutableLiveData<List<SessionMeasurements>?>()

    private val _navigateToFinalize = MutableLiveData<Session?>()
    val navigateToFinalize: LiveData<Session?>
        get() = _navigateToFinalize

    fun doneNavigating() {
        _navigateToFinalize.value = null
    }

    val sessionsString = Transformations.map(sessions) { sessions ->
        formatSessions(sessions, application.resources)
    }

    val mString = Transformations.map(measurements) { measurements ->
        formatMeasurements(measurements, application.resources)
    }

    private fun formatMeasurements(measurements: List<Measurement>, resources: Resources): Spanned {
        val sb = StringBuilder()
        sb.apply {
            measurements.forEach {
                append("<br>")
                append(it.measurementID)
                append(" : ${it.sessionID}")
                append("<br>")
                append(convertLongToDateString(it.recorded))
                append("<br>")
                append(it.measurement)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    private fun formatSessions(sessions: List<Session>, resources: Resources): Spanned {
        val sb = StringBuilder()
        sb.apply {
            append(resources.getString(R.string.title))
            sessions.forEach {
                append("<br>")
                append(it.title)
                append("<br>")
                append(resources.getString(R.string.start_time))
                append("\t${convertLongToDateString(it.startTimeMilli)}<br>")
                if (it.endTimeMilli != it.startTimeMilli) {
                    append(resources.getString(R.string.end_time))
                    append("\t${convertLongToDateString(it.endTimeMilli)}<br>")
                    append(resources.getString(R.string.hours_slept))
                    // Hours
                    append("\t ${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60 / 60}:")
                    // Minutes
                    append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000 / 60}:")
                    // Seconds
                    append("${it.endTimeMilli.minus(it.startTimeMilli) / 1000}<br><br>")
                }
                append(it.note)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
        } else {
            return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertLongToDateString(systemTime: Long): String {
        return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
            .format(systemTime).toString()
    }

    val startButtonVisible = Transformations.map(currentSession) {
        it == null
    }

    val stopButtonVisible = Transformations.map(currentSession) {
        it != null
    }

    val clearButtonVisible = Transformations.map(sessions) {
        it?.isNotEmpty()
    }

    init {
        initializeCurrentSession()
    }

    private fun initializeCurrentSession() {
        viewModelScope.launch { currentSession.value = getCurrentSessionFromDB() }
    }

    private suspend fun getCurrentSessionFromDB() : Session? {
        var session = database.getCurrentSession()
        if(session != null) {
            cM.value = database.getSessionMeasurements(session.sessionID)
        }
        if(session?.startTimeMilli != session?.endTimeMilli) {
            session = null
            cM.value = null
        }
        return session
    }

    fun onStartSession() {
        Log.d(TAG, "Start button clicked")
        viewModelScope.launch {
            val newSession = Session()
            insert(newSession)
            currentSession.value = getCurrentSessionFromDB()
        }
    }

    private suspend fun insert(session: Session) {
        database.insert(session)
    }

    fun onGenerateRandom() {
        Log.d(TAG, "Generating a random number")
        viewModelScope.launch {
            val rn: Float = ThreadLocalRandom.current().nextFloat()
            val newMeasurement = Measurement(sessionID = currentSession.value!!.sessionID, measurement = rn)
            insertMeasurement(newMeasurement)
        }
    }

    private suspend fun insertMeasurement(m: Measurement) {
        database.insert(m)
    }

    fun onStopSession() {
        Log.d(TAG, "Stop session")
        measurements.value!!.forEach { Log.d(TAG, "${it.measurement} : ${it.sessionID}") }
        cM.value!!.forEach { Log.d(TAG, "${it.session.sessionID} : ${it.sessionMeasurements.forEach { it.measurement.toString() }} ") }
        viewModelScope.launch {
            val oldSession = currentSession.value ?: return@launch
            oldSession.endTimeMilli = System.currentTimeMillis()
            update(oldSession)
            _navigateToFinalize.value = oldSession
        }
    }

    private suspend fun update(session: Session) {
        database.update(session)
    }

    fun onClear() {
        Log.d(TAG, "Clear Table")
        viewModelScope.launch {
            clear()
            currentSession.value = null
        }
    }

    private suspend fun clear() {
        database.clearSessions()
    }
}