package com.siemens.j6_app_android_demo.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.mapxus.map.mapxusmap.positioning.IndoorLocation
import com.mapxus.map.mapxusmap.positioning.IndoorLocationProvider
import com.mapxus.positioning.positioning.api.MapxusLocation
import com.mapxus.positioning.positioning.api.MapxusPositioningClient
import com.mapxus.positioning.positioning.api.MapxusPositioningListener
import com.mapxus.positioning.positioning.api.PositioningState

interface OnLocationProvidedCallback {
    fun onLocationProvided(location: IndoorLocation)
}

class MapxusPositioningProvider(var lifecycleOwner: LifecycleOwner, context: Context) :
    IndoorLocationProvider() {
    private val context: Context
    private var positioningClient: MapxusPositioningClient? = null
    private var started = false

    private var listener: OnLocationProvidedCallback? = null

    override fun supportsFloor(): Boolean {
        return true
    }

    override fun start() {
        positioningClient =
            MapxusPositioningClient.getInstance(lifecycleOwner, context.getApplicationContext())
        positioningClient!!.addPositioningListener(mapxusPositioningListener)
        positioningClient!!.start()
        started = true
    }

    override fun stop() {
        if (positioningClient != null) {
            positioningClient!!.stop()
        }
        started = false
    }

    override fun isStarted(): Boolean {
        return started
    }

    private val mapxusPositioningListener: MapxusPositioningListener =
        object : MapxusPositioningListener {
            override fun onStateChange(positionerState: PositioningState) {
                when (positionerState) {
                    PositioningState.STOPPED -> {
                        dispatchOnProviderStopped()
                    }
                    PositioningState.RUNNING -> {
                        dispatchOnProviderStarted()
                    }
                    else -> {
                    }
                }
            }

            override fun onError(p0: com.mapxus.positioning.positioning.api.ErrorInfo?) {
                //TODO("Not yet implemented")
            }

            override fun onOrientationChange(orientation: Float, sensorAccuracy: Int) {
                dispatchCompassChange(orientation, sensorAccuracy)
            }

            override fun onLocationChange(mapxusLocation: MapxusLocation) {
                val location = android.location.Location("MapxusPositioning")
                location.setLatitude(mapxusLocation.latitude)
                location.setLongitude(mapxusLocation.longitude)
                location.setTime(System.currentTimeMillis())
                val floor =
                    if (mapxusLocation.mapxusFloor == null) null else mapxusLocation.mapxusFloor.code
                val building = mapxusLocation.buildingId
                val indoorLocation = IndoorLocation(location, building, floor)
                indoorLocation.setAccuracy(mapxusLocation.accuracy)
                dispatchIndoorLocationChange(indoorLocation)

                listener?.onLocationProvided(indoorLocation)
            }
        }

    fun setListener(listener: OnLocationProvidedCallback) {
        this.listener = listener
    }

    companion object {
        private const val TAG = "MapxusPositioningProvider"
    }

    init {
        this.context = context
    }
}