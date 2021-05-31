package com.siemens.j6_app_android_demo.activities.home_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.LocationRecyclerViewAdapter
import com.siemens.j6_app_android_demo.models.Location


class SelectLocationActivity : AppCompatActivity() {

    //private var locationListImported = ArrayList<Location>()
    private var locationList = ArrayList<Location>()
    lateinit var daRecyclerView: RecyclerView
    val dAdapter: LocationRecyclerViewAdapter = LocationRecyclerViewAdapter(locationList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_location)

        val locsImported = intent.getSerializableExtra("location_list") as ArrayList<Location>
        if (locsImported.isNotEmpty()) {
            locationList.clear()
            locationList.addAll(locsImported)
            dAdapter.notifyDataSetChanged()
        }

        daRecyclerView = findViewById(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter

        findViewById<ImageView>(R.id.add_new_loc).setOnClickListener {
            addNewLocation.launch(Intent(this, AddNewLocationActivity::class.java))
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val locsSelected = ArrayList<Location>()
            for (loc in locationList) {
                if (loc.isSelected) {
                    Log.d("TAG", "Selected: " + loc.getId())
                    locsSelected.add(loc)
                }
            }
            val resultIntent = Intent()
            resultIntent.putExtra("locs", locsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val locsSelected: ArrayList<Location>? = null
            val resultIntent = Intent()
            resultIntent.putExtra("locs", locsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    private val addNewLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val locToAdd = result.data!!.getSerializableExtra("loc") as Location?
            locToAdd?.let {
                var alreadyAdded = false
                for (loc in locationList)
                    if (loc.id == locToAdd.id)
                        alreadyAdded = true
                if (!alreadyAdded)
                    locationList.add(locToAdd)
                dAdapter.notifyDataSetChanged()
            }
        }
    }

}