package com.example.compassapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var rotationSensor: Sensor? = null

    private lateinit var ivDial: ImageView
    private lateinit var tvAzimuth: TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvInfo: TextView

    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var filteredAzimuth = 0f
    private val alpha = 0.2f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivDial = findViewById(R.id.ivDial)
        tvAzimuth = findViewById(R.id.tvAzimuth)
        tvDirection = findViewById(R.id.tvDirection)
        tvInfo = findViewById(R.id.tvInfo)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        tvInfo.text =
            if (rotationSensor != null) "Sensor: Rotation Vector"
            else "Sensor not available"
    }

    override fun onResume() {
        super.onResume()
        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ROTATION_VECTOR) return

        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
        SensorManager.getOrientation(rotationMatrix, orientation)

        var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
        if (azimuth < 0) azimuth += 360f

        filteredAzimuth += alpha * (azimuth - filteredAzimuth)

        ivDial.rotation = -filteredAzimuth

        val deg = filteredAzimuth.roundToInt()
        tvAzimuth.text = "Azimuth: $deg°"
        tvDirection.text = "Direction: ${directionFromDegrees(filteredAzimuth)}"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun directionFromDegrees(deg: Float): String {
        val dirs = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = ((deg + 22.5f) / 45f).toInt() % 8
        return dirs[index]
    }
}
