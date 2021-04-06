package com.enshaedn.seismic.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.enshaedn.seismic.database.Measurement
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.Session
import com.enshaedn.seismic.database.SessionMeasurements
import com.enshaedn.seismic.utils.convertStringDateToDate
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.pow
import kotlin.math.sqrt

class SeismicActiveViewModel(
    private val activeKey: Long = 0L,
    val dataSource: SeismicDao
) : ViewModel()
{
    private val TAG = "SEISMIC_LOG"
    private val database = dataSource
    private val activeSession: LiveData<SessionMeasurements> = database.getMeasurementsByID(activeKey)
    private val dataPoints = MutableLiveData<LineGraphSeries<DataPoint>>()

    fun getActiveSession() = activeSession

    fun getDataPoints() = dataPoints

    fun gatherDataPoints() {
        val dp = LineGraphSeries<DataPoint>()
        activeSession.value!!.sessionMeasurements.forEach {
            val magnitude = sqrt(it.xValue.pow(2) + it.yValue.pow(2) + it.zValue.pow(2))
            Log.d(TAG, "${it.sessionID} : ${it.measurementID} : ${magnitude} : ${convertStringDateToDate(it.recorded)}")
            dp.appendData(DataPoint(convertStringDateToDate(it.recorded), magnitude.toDouble()), true, 20)
        }
        dataPoints.value = dp
    }

    private val _navigateToFinalize = MutableLiveData<Long?>()
    val navigateToFinalize: LiveData<Long?>
        get() = _navigateToFinalize

    // Reset Navigation variables
    fun doneNavigating() {
        _navigateToFinalize.value = null
    }

    // Placeholder for accelerometer data generation
    fun onGenerateRandom() {
        Log.d(TAG, "Generating a random number")
        viewModelScope.launch {
            val rn: Float = ThreadLocalRandom.current().nextFloat()
            val newMeasurement = Measurement(sessionID = activeKey, recorded = System.currentTimeMillis(), xValue = rn, yValue = 0f, zValue = 0f)
            insertMeasurement(newMeasurement)
        }
    }

    fun onAccelerometerDataGenerated(data: FloatArray, t: Long) {
        Log.d(TAG, "Saving accelerometer data")
        viewModelScope.launch {
            val newMeasurement = Measurement(sessionID = activeKey, recorded = t, xValue = data[0], yValue = data[1], zValue = data[2])
            insertMeasurement(newMeasurement)
        }
    }

    private suspend fun insertMeasurement(m: Measurement) {
        database.insert(m)
    }

    // End session recording
    fun onStopSession() {
        Log.d(TAG, "Stop session: ${activeKey}")
        viewModelScope.launch {
            // Update active session's end time to current system time
//            val oldSession = database.get(activeKey) ?: return@launch
            val oldSession = activeSession.value?.session ?: return@launch
            oldSession.endTimeMilli = System.currentTimeMillis()
            // Update session's data in DB
            update(oldSession)
            // Propagate active session key via navigateToFinalize
            _navigateToFinalize.value = activeKey
        }
    }

    private suspend fun update(session: Session) {
        database.update(session)
    }
}