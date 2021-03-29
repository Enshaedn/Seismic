package com.enshaedn.seismic.viewModels

import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.*
import com.enshaedn.seismic.database.Measurement
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session
import com.enshaedn.seismic.database.SessionMeasurements
import com.enshaedn.seismic.utils.convertLongToDateString
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom

class SeismicViewModel(
    val database: SeismicDao, application: Application
    ) : AndroidViewModel(application) {
    private val TAG = "SEISMIC_LOG"
    private var currentSession = MutableLiveData<Session?>()
    private val sessions = database.getAllSessions()
    private val measurements = database.getAllMeasurements()
    private var cM = MutableLiveData<List<SessionMeasurements>?>()
//    lateinit var graphData: LineGraphSeries<DataPoint>

    private val _navigateToFinalize = MutableLiveData<Session?>()
    val navigateToFinalize: LiveData<Session?>
        get() = _navigateToFinalize

    private val _navigateToSessionsList = MutableLiveData<Boolean?>()
    val navigateToSessionsList: LiveData<Boolean?>
        get() = _navigateToSessionsList

    fun onViewSessions() {
        _navigateToSessionsList.value = true
    }

    fun doneNavigating() {
        _navigateToFinalize.value = null
        _navigateToSessionsList.value = null
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

    val startButtonVisible = Transformations.map(currentSession) {
        it == null
    }

    val stopButtonVisible = Transformations.map(currentSession) {
        it != null
    }

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
//            val dataPoint: DataPoint = DataPoint(0.0, newMeasurement.measurement.toDouble())
//            graphData.appendData(dataPoint,true,10)
            insertMeasurement(newMeasurement)
        }
    }

    private suspend fun insertMeasurement(m: Measurement) {
        database.insert(m)
    }

    fun onStopSession() {
        Log.d(TAG, "Stop session")
//        measurements.value!!.forEach { Log.d(TAG, "${it.measurement} : ${it.sessionID}") }
//        cM.value!!.forEach { Log.d(TAG, "${it.session.sessionID} : ${it.sessionMeasurements.forEach { it.measurement.toString() }} ") }
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
}