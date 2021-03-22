package com.enshaedn.seismic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity(), SensorEventListener {
    private val TAG = "SEISMIC_LOG"
    private lateinit var sensorManager: SensorManager
    private var mAccel: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Log.d(TAG, "Testing")

//        val sensorList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        Log.d(TAG, sensor.name)
        Log.d(TAG, accuracy.toString())
    }

    override fun onSensorChanged(event: SensorEvent?) {
//        Log.d(TAG, event.toString())
//        val v = event?.values
        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val t = event.timestamp

        Log.d(TAG, event.sensor.name + " : " + x.toString() + " : " + y.toString() + " : " + z.toString() + " : " + t)
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