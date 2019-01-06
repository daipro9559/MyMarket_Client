package com.example.dainv.mymarket.ui.map

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


class Bearing
// compass arrow to rotate
@Inject constructor(context: Context) : SensorEventListener, LifecycleObserver {

    private val sensorManager: SensorManager = context
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gsensor: Sensor
    private val msensor: Sensor
    private val mGravity = FloatArray(3)
    private val mGeomagnetic = FloatArray(3)
    private var azimuth = 0f
    private var isStart: Boolean = false
    val bearObserve = PublishSubject.create<Float>()

    private val handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MESS_WHAT -> {
                    bearObserve.onNext(azimuth)
                    this.sendEmptyMessageDelayed(MESS_WHAT, TIME_SKIP.toLong())
                }
            }
        }
    }

    init {
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        sensorManager.registerListener(this, gsensor,
                SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        sensorManager.unregisterListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        startOnListenChange()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        stopOnListenChange()
    }

     private fun unRegisterListener() {
        isStart = false
        sensorManager.unregisterListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        unRegisterListener()
    }

    private fun startOnListenChange() {
        handler.sendEmptyMessageDelayed(MESS_WHAT, TIME_SKIP.toLong())
    }

    private fun stopOnListenChange() {
        handler.removeMessages(MESS_WHAT)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val alpha = 0.97f

        synchronized(this) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0]
                mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1]
                mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2]

                // mGravity = event.values;

                // Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0]
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1]
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2]
                // Log.e(TAG, Float.toString(event.values[0]));

            }

            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat() // orientation
                azimuth = (azimuth + 360) % 360
                // Log.d(TAG, "azimuth (deg): " + azimuth);
                //				adjustArrow();
                //				if (bearListener !=null) {
                //					bearListener.bearOnChange(azimuth);
                //				}
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    interface BearListener {
        fun bearOnChange(bear: Float)
    }

    companion object {
        private val TAG = "Bearing"
        val TIME_SKIP = 100
        val MESS_WHAT = 150
    }
}
