package com.siemens.j6_app_android_demo.activities.home_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.BuildingAdapter
import com.siemens.j6_app_android_demo.adapters.LevelAdapter
import com.siemens.j6_app_android_demo.adapters.RoomAdapter
import com.siemens.j6_app_android_demo.models.Building
import com.siemens.j6_app_android_demo.models.Location
import com.siemens.j6_app_android_demo.service.LocationsCallback
import com.siemens.j6_app_android_demo.service.WorkOrderService


class AddNewLocationActivity : AppCompatActivity(), LocationsCallback {

    lateinit var buildingChosen: TextView
    lateinit var levelChosen: TextView
    lateinit var roomChosen: TextView

    private val locationList = ArrayList<Location>()
    val locationMap = HashMap<String, HashMap<String, ArrayList<Location>>>()

    lateinit var buildingLV: ListView
    var buildingList: ArrayList<String> = ArrayList()
    var levelList: ArrayList<String> = ArrayList()
    var roomList: ArrayList<Location> = ArrayList()
    var buildingAdapter: BuildingAdapter? = null
    lateinit var levelLV: ListView
//    var levelList: ArrayList<String> = ArrayList()
//    var buildingAdapter: BuildingAdapter? = null
    lateinit var roomLV: ListView

    //val locationCreated = Location(Building("null", "null", "null"), "null", "null", "null")
    var locationCreated: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_location)

        buildingChosen = findViewById(R.id.building_chosen)
        levelChosen = findViewById(R.id.level_chosen)
        roomChosen = findViewById(R.id.room_chosen)

        WorkOrderService.locationsListener = this
        WorkOrderService.fetchLocationsData()

        buildingLV = findViewById(R.id.building_listview)
//        buildingList = arrayListOf("Building 1", "Building 2", "Building 3", "Building 4")
        buildingAdapter = BuildingAdapter(this, buildingList)
        buildingLV.adapter = buildingAdapter

        levelLV = findViewById(R.id.level_listview)
        //levelLV.adapter = LevelAdapter(this, arrayListOf("G/F", "1/F", "2/F", "3/F"))
        levelLV.adapter = LevelAdapter(this, levelList)

        roomLV = findViewById(R.id.room_listview)
        roomLV.adapter = RoomAdapter(this, roomList)

        val xbb = findViewById<ImageView>(R.id.expand_building_btn)
        val xlb = findViewById<ImageView>(R.id.expand_level_btn)
        val xrb = findViewById<ImageView>(R.id.expand_room_btn)

        val expandBtnToLV = hashMapOf(xbb to buildingLV, xlb to levelLV, xrb to roomLV)
        for ((expandBtn, _) in expandBtnToLV) {
            expandBtn.setOnClickListener {
                if (expandBtnToLV[expandBtn]!!.visibility == View.VISIBLE) {
                    expandBtnToLV[expandBtn]!!.visibility = View.GONE
                } else {
                    for ((_, listView) in expandBtnToLV) {
                        listView.visibility = View.GONE
                    }
                    expandBtnToLV[expandBtn]!!.visibility = View.VISIBLE
                }
            }
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            locationCreated?.let {
                val resultIntent = Intent()
                resultIntent.putExtra("loc", locationCreated)
                setResult(RESULT_OK, resultIntent)
                finish()
            } ?: run {
                Toast.makeText(this, "Please select all the required parameters before saving your changes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkIfReady() {
        //if (locationCreated.ready())
        locationCreated?.let{
            findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
        }
    }

    override fun onLocationsResult(result: List<Location>) {
        locationList.addAll(result)
        for (loc in locationList) {
            if (locationMap.containsKey(loc.building.id) && locationMap[loc.building.id]!!.containsKey(loc.level)) {
                locationMap[loc.building.id]?.get(loc.level)?.add(loc)
            } else if (locationMap.containsKey(loc.building.id)){
                locationMap[loc.building.id]?.set(loc.level, arrayListOf(loc))
            } else {
                locationMap.set(loc.building.id, hashMapOf(loc.level to arrayListOf<Location>(loc))) // HashMap<String, ArrayList<String>>()
            }
        }
        buildingList.addAll(ArrayList(locationMap.keys))
        buildingAdapter!!.notifyDataSetChanged()
    }
}