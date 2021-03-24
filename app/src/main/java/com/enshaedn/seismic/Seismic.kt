package com.enshaedn.seismic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import java.util.*

class Seismic : Fragment(), SensorEventListener {
    private val TAG = "SEISMIC_LOG"
    private lateinit var sensorManager: SensorManager
    private var mAccel: Sensor? = null
    val gravity = FloatArray(3)
    val accData = FloatArray(3)
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Log.d(TAG, "Testing")
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seismic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stopButton = view.findViewById(R.id.stopSession)
        stopButton.setOnClickListener {
            Log.d(TAG, "Stop Tracking")
            sensorManager.unregisterListener(this)
        }

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

        accData[0] = event.values[0] - gravity[0]
        accData[1] = event.values[1] - gravity[1]
        accData[2] = event.values[2] - gravity[2]
        val t = event.timestamp

        Log.d(TAG, event.sensor.name + " : " + accData[0].toString() + " : " + accData[1].toString() + " : " + accData[2].toString() + " : " + Date(t))
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