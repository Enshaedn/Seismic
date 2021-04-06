package com.enshaedn.seismic.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enshaedn.seismic.database.SeismicDao
import com.enshaedn.seismic.database.SessionMeasurements
import com.enshaedn.seismic.utils.convertStringDateToDate
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.pow
import kotlin.math.sqrt

class SessionDetailViewModel(private val sessionKey: Long = 0L, dataSource: SeismicDao) : ViewModel()
{
    private val TAG = "SEISMIC_LOG"
    private val database = dataSource
    private val session: LiveData<SessionMeasurements> = database.getMeasurementsByID(sessionKey)
    private val dataPoints = MutableLiveData<LineGraphSeries<DataPoint>>()

    fun getSession() = session

    fun getDataPoints() = dataPoints

    fun gatherDataPoints() {
        val dp = LineGraphSeries<DataPoint>()
        session.value!!.sessionMeasurements.forEach {
            val magnitude = sqrt(it.xValue.pow(2) + it.yValue.pow(2) + it.zValue.pow(2))
            Log.d(TAG, "${it.sessionID} : ${it.measurementID} : ${magnitude} : ${convertStringDateToDate(it.recorded)}")
            dp.appendData(DataPoint(convertStringDateToDate(it.recorded), magnitude.toDouble()), true, 10)
        }
        dataPoints.value = dp
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