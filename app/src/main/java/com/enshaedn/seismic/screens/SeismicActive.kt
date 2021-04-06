package com.enshaedn.seismic.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enshaedn.seismic.R
import com.enshaedn.seismic.database.SeismicDB
import com.enshaedn.seismic.databinding.FragmentSeismicActiveBinding
import com.enshaedn.seismic.utils.convertLongToDateString
import com.enshaedn.seismic.viewModels.SeismicActiveViewModel
import com.enshaedn.seismic.viewModels.SeismicActiveViewModelFactory
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import java.util.*

class SeismicActive : Fragment(), SensorEventListener {
    private val TAG = "SEISMIC_LOG"
    private lateinit var sensorManager: SensorManager
    private var mAccel: Sensor? = null
    private val gravity = FloatArray(3)
    private val accelData = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "Active Screen")
        val binding: FragmentSeismicActiveBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_seismic_active, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SeismicActiveArgs.fromBundle(requireArguments())

        val dataSource = SeismicDB.getInstance(application).seismicDao

        val viewModelFactory = SeismicActiveViewModelFactory(arguments.sessionKey, dataSource)

        val seismicActiveViewModel = ViewModelProvider(this, viewModelFactory).get(SeismicActiveViewModel::class.java)

        // Navigate to Finalize Screen with active session primary key
        seismicActiveViewModel.navigateToFinalize.observe(viewLifecycleOwner, { activeKey ->
            activeKey?.let {
                sensorManager.unregisterListener(this)
                this.findNavController().navigate(SeismicActiveDirections.actionSeismicActiveToSeismicFinalize(activeKey))
                seismicActiveViewModel.doneNavigating()
            }
        })

        // Observe the active session's SessionMeasurement
        seismicActiveViewModel.getActiveSession().observe(viewLifecycleOwner, {
            it?.let {
                seismicActiveViewModel.gatherDataPoints()
                it.sessionMeasurements.forEach {
                    Log.d(TAG, "${it.measurementID} : ${it.measurement}")
                }
            }
        })

        // Update Graph View
        seismicActiveViewModel.getDataPoints().observe(viewLifecycleOwner, {
            if(it != null) {
                binding.activeGraph.apply {
                    addSeries(it)
                    gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
                    gridLabelRenderer.numHorizontalLabels = 3
                    viewport.setMinX(it.lowestValueX)
                    viewport.setMaxX(it.highestValueX)
                    viewport.isXAxisBoundsManual = true
                    gridLabelRenderer.setHumanRounding(false)
                }
            }
        })

        binding.setLifecycleOwner(this)

        binding.seismicActiveViewModel = seismicActiveViewModel

        return binding.root
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(TAG, sensor.name)
        Log.d(TAG, accuracy.toString())
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha: Float = 0.8f

        //isolate gravity with low pass filter
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

        accelData[0] = event.values[0] - gravity[0]
        accelData[1] = event.values[1] - gravity[1]
        accelData[2] = event.values[2] - gravity[2]
        // event.timestamp is a Long, but in nanoseconds
        val t = event.timestamp
        val tInMilli = System.currentTimeMillis() + (t - SystemClock.elapsedRealtimeNanos()) / 1000000

        Log.d(TAG, "${event.sensor.name}: ${accelData[0]}, ${accelData[1]}, ${accelData[2]} : ${t} vs ${SystemClock.elapsedRealtimeNanos()}")
        Log.d(TAG, "${tInMilli} : ${convertLongToDateString(tInMilli)}")
    }

    override fun onResume() {
        super.onResume()
        mAccel?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}