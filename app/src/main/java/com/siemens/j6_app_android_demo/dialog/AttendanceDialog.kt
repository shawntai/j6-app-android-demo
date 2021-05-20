package com.siemens.j6_app_android_demo.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.mapbox.mapboxsdk.maps.MapView
import com.mapxus.map.mapxusmap.api.map.MapxusMap
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider
import com.mapxus.map.mapxusmap.positioning.IndoorLocation
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.activities.home_page.HomePageActivity
import com.siemens.j6_app_android_demo.service.OnLocationProvidedCallback
import com.siemens.j6_app_android_demo.service.MapxusPositioningProvider


class AttendanceDialog(context: Context) : Dialog(context), OnMapxusMapReadyCallback, OnLocationProvidedCallback {

    var activity: HomePageActivity? = null
    var mapxusMap: MapxusMap? = null
    private lateinit var mapView: MapView
    private var canTakeAttendance = false
    private var mapxusPositioningProvider: MapxusPositioningProvider? = null
    private var mapviewProvider: MapboxMapViewProvider? = null

    init {
        setCancelable(true)
    }

    @SuppressLint("ClickableViewAccessibility", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.attendance_pop_up)
        mapView = findViewById(R.id.mapView)
        mapView.setOnTouchListener { v, event -> true }
        mapView.onCreate(savedInstanceState)

        this.mapviewProvider = MapboxMapViewProvider(context, mapView)
        this.mapviewProvider!!.getMapxusMapAsync(this)

        findViewById<ImageView>(R.id.close_attendance).setOnClickListener {
            this.mapxusMap!!.onPause()
            this.mapviewProvider!!.onDestroy()
            this.mapxusPositioningProvider!!.stop()
            dismiss()
        }
        val notInBuildingDialog = Dialog(context)
        notInBuildingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        notInBuildingDialog.setCancelable(false)
        notInBuildingDialog.setContentView(R.layout.not_in_building_layout)
        notInBuildingDialog.findViewById<TextView>(R.id.close_warning).setOnClickListener {
            notInBuildingDialog.dismiss()
        }
        findViewById<TextView>(R.id.take_attendance).setOnClickListener {
            if (!canTakeAttendance) {
                notInBuildingDialog.show()
            }
        }
    }

    override fun onMapxusMapReady(p0: MapxusMap?) {
        this.mapxusMap = p0
        mapxusPositioningProvider = activity?.let {
            MapxusPositioningProvider(
                it,
                context.applicationContext
            )
        }
        mapxusPositioningProvider!!.setListener(this)
        p0!!.setLocationProvider(mapxusPositioningProvider)
    }

    override fun onLocationProvided(location: IndoorLocation) {
        canTakeAttendance = location.building == "143859d5c0fd4d76ba5c650f707bdfe7" // AMC
    }

}