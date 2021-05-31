package com.siemens.j6_app_android_demo.activities.home_page

import android.Manifest
import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mapxus.map.mapxusmap.api.map.MapxusMapContext
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.CalenderAdapter
import com.siemens.j6_app_android_demo.adapters.MaintenanceAdapter
import com.siemens.j6_app_android_demo.dialog.AttendanceDialog
import com.siemens.j6_app_android_demo.fragments.FavoriteFragment
import com.siemens.j6_app_android_demo.fragments.SearchFragment
import com.siemens.j6_app_android_demo.fragments.WorkOrderFragment
import com.siemens.j6_app_android_demo.models.MaintenanceDataModel
import com.siemens.j6_app_android_demo.models.WorkOrder
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.collections.ArrayList

class HomePageActivity : AppCompatActivity() {

    private lateinit var allFeatHoriz: HorizontalScrollView
    private lateinit var allFeatExp: LinearLayout
    private var allFeatHorizHeight: Int = 0
    private var allFeatExpHeight: Int = 0

    private val datesList: ArrayList<Calendar> = ArrayList()
    private var dAdapter: CalenderAdapter? = null

    lateinit var mListView: ListView
    var maintenanceList: ArrayList<MaintenanceDataModel> = ArrayList()
    var mAdapter: MaintenanceAdapter? = null

    lateinit var datesRecyclerView: RecyclerView

    var editedWorkOrderPosition = 0

    var tabSelected = -1
    val WORK_ORDER = 0
    val SEARCH = 1
    val FAVORITE = 2

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapxusMapContext.init(this)
        setContentView(R.layout.activity_home_page)
        methodRequiresPermission()

        allFeatHoriz = findViewById(R.id.all_features_horiz_scroll)
        allFeatExp = findViewById(R.id.all_features_expanded)
        allFeatHoriz.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        allFeatExp.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        val scale: Float = resources.displayMetrics.density
        allFeatHorizHeight = (110 * scale + 0.5f).toInt()
        allFeatExpHeight = (360 * scale + 0.5f).toInt()
        datesRecyclerView = findViewById(R.id.dates_recyclerview)
        dAdapter = CalenderAdapter(datesList)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        datesRecyclerView.layoutManager = layoutManager
        datesRecyclerView.itemAnimator = DefaultItemAnimator()
        datesRecyclerView.adapter = dAdapter
        prepareDatesData()
        datesRecyclerView.scrollToPosition(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1)

        mListView = findViewById(R.id.maintenance_listview)
        for (i in 1..5) {
            maintenanceList.add(
                MaintenanceDataModel(
                    "Chiller Plant Inspection",
                    Calendar.getInstance(),
                    "Chiller Plant Room, Building 1",
                    R.drawable.maintenance_1
                )
            )
        }
        mAdapter = MaintenanceAdapter(this, maintenanceList)
        mListView.adapter = mAdapter
        // adjust listview height to prevent it from collapsing
        val totalHeight = (120 * scale + 0.5f).toInt() * mAdapter!!.count  // 120 => each item's height in dp
        //setting listview item in adapter
        val params: ViewGroup.LayoutParams = mListView.layoutParams
        params.height = totalHeight + mListView.dividerHeight * mAdapter!!.count
        mListView.layoutParams = params

        // fragments
        loadFragment(WorkOrderFragment())
        findViewById<TextView>(R.id.work_order_butt).setOnClickListener {
            //updateFragButtonBackground(it)
//            tabSelected = if (tabSelected != WORK_ORDER) WORK_ORDER else -1
            if (tabSelected != WORK_ORDER) {
                tabSelected = WORK_ORDER
                expandWorkOrder()
                loadFragment(WorkOrderFragment())
                (it as TextView).setTextColor(getColor(R.color.j6_light_orange))
            } else {
                tabSelected = -1
                minimizeWorkOrder()
                (it as TextView).setTextColor(getColor(R.color.white))
            }
            updateFragButtonBackground()
//            loadFragment(WorkOrderFragment())
        }
        findViewById<LinearLayout>(R.id.search_butt).setOnClickListener {

            if (tabSelected != SEARCH) {
                tabSelected = SEARCH
                expandWorkOrder()
                loadFragment(SearchFragment())
            } else {
                tabSelected = -1
                minimizeWorkOrder()
            }
            updateFragButtonBackground()
        }
        findViewById<LinearLayout>(R.id.favorite_butt).setOnClickListener {
            if (tabSelected != FAVORITE) {
                tabSelected = FAVORITE
                expandWorkOrder()
                loadFragment(FavoriteFragment())
            } else {
                tabSelected = -1
                minimizeWorkOrder()
            }
            updateFragButtonBackground()
        }
        findViewById<LinearLayout>(R.id.add_new_work_order).setOnClickListener {
            //startActivity(Intent(this, AddNewWorkOrderActivity::class.java))
            addNewWorkOrder.launch(Intent(this, WorkOrderActivity::class.java))
        }

        findViewById<ImageView>(R.id.pfp).setOnClickListener {

            val dialog = AttendanceDialog(this)
            dialog.activity = this
            dialog.show()
        }

        findViewById<View>(R.id.Chris).setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.not_in_building_layout)
            dialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, 5000)
        }
    }


    private val addNewWorkOrder = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val nwo = Gson().fromJson(result.data!!.getStringExtra("nwo"), WorkOrder::class.java)
            //WorkOrderFragment().onNewWorkOrderAdded(nwo)
            (supportFragmentManager.findFragmentById(R.id.fragment_placeholder) as WorkOrderFragment).onNewWorkOrderAdded(
                nwo
            )
            expandWorkOrder()
        }
    }

    val editWorkOrder = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val woe = Gson().fromJson(
                result.data!!.getStringExtra("wo_edited"),
                WorkOrder::class.java
            )
            (supportFragmentManager.findFragmentById(R.id.fragment_placeholder) as WorkOrderFragment).onWorkOrderEdited(
                woe,
                editedWorkOrderPosition
            )
            //expandWorkOrder(View(this))
        }
    }

    private fun prepareDatesData() {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        val month = cal.get(Calendar.MONTH)
        while (month == cal.get(Calendar.MONTH)) {
            val tempCal = Calendar.getInstance()
            tempCal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
            datesList.add(tempCal)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        dAdapter?.notifyDataSetChanged()
    }

    fun expandOrCollapseAllFeatures(view: View) {
        //Toast.makeText(this, "hh: $allFeatHorizHeight, eh: $allFeatExpHeight", Toast.LENGTH_SHORT).show()
        val textView = findViewById<TextView>(R.id.see_all_pack_up_text)
        if (textView.text == getString(R.string.see_all)) {
            allFeatHoriz.layoutParams = LinearLayout.LayoutParams(allFeatHoriz.width, 0)
            allFeatExp.layoutParams = LinearLayout.LayoutParams(allFeatExp.width, allFeatExpHeight)
            textView.text = getText(R.string.pack_up)
            findViewById<ImageView>(R.id.see_all_pack_up_img).setImageResource(R.drawable.orange_homepage_pack_up_upward_arrow)
        } else {
            allFeatHoriz.layoutParams = LinearLayout.LayoutParams(
                allFeatHoriz.width,
                allFeatHorizHeight
            )
            allFeatExp.layoutParams = LinearLayout.LayoutParams(allFeatExp.width, 0)
            textView.text = getString(R.string.see_all)
            findViewById<ImageView>(R.id.see_all_pack_up_img).setImageResource(R.drawable.orange_homepage_see_all_downward_arrow)
        }
    }

    private fun expandWorkOrder() {
        val workOrder: LinearLayout = findViewById(R.id.work_order)
        workOrder.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val params = workOrder.layoutParams
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels + getNavigationBarHeight()
        val scrollPosition = IntArray(2)
        findViewById<ScrollView>(R.id.scroll_view).getLocationOnScreen(scrollPosition)
        val expandedHeight = screenHeight - scrollPosition[1] - getNavigationBarHeight()
        params.height = expandedHeight
        workOrder.layoutParams = params
    }

    private fun minimizeWorkOrder() {
        val workOrder: LinearLayout = findViewById(R.id.work_order)
        val params = workOrder.layoutParams
        params.height = (80 * resources.displayMetrics.density + 0.5f).toInt()
        workOrder.layoutParams = params
        tabSelected = -1
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_placeholder, fragment).commit()
    }

    private fun updateFragButtonBackground() {
        findViewById<TextView>(R.id.work_order_butt).setTextColor(if (tabSelected == WORK_ORDER) getColor(R.color.j6_light_orange) else getColor(R.color.white))
        findViewById<ImageView>(R.id.search_butt_img).setImageResource(if (tabSelected == SEARCH) R.drawable.orange_mag_glass else R.drawable.white_mag_glass)
        findViewById<ImageView>(R.id.favorite_butt_img).setImageResource(if (tabSelected == FAVORITE) R.drawable.orange_heart else R.drawable.white_heart)
    }

    private fun getNavigationBarHeight(): Int {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight: Int = metrics.heightPixels
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight: Int = metrics.heightPixels
        return if (realHeight > usableHeight) realHeight - usableHeight else 0
    }

    private fun methodRequiresPermission() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
        for (i in perms.indices) {
            if (!EasyPermissions.hasPermissions(this, perms[i])) {
                EasyPermissions.requestPermissions(this, "request permission", 1010, perms[i])
            }
        }
    }

//    override fun onLocationProvided(location: IndoorLocation) {
//        canTakeAttendance = location.building == "143859d5c0fd4d76ba5c650f707bdfe7" // AMC
//    }

}