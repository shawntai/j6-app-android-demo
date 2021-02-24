package com.siemens.j6_app_android_demo.activities.home_page

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.CalenderAdapter
import com.siemens.j6_app_android_demo.adapters.MaintenanceAdapter
import com.siemens.j6_app_android_demo.models.MaintenanceDataModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        allFeatHoriz = findViewById(R.id.all_features_horiz_scroll)
        allFeatExp = findViewById(R.id.all_features_expanded)
        val scale: Float = resources.displayMetrics.density
        allFeatHorizHeight = (110 * scale + 0.5f).toInt()
        allFeatExpHeight = (360 * scale + 0.5f).toInt()
        /*allFeatHorizHeight = allFeatHoriz.height
        allFeatExpHeight = allFeatExp.height
        print(">>>> hrz height = $allFeatHorizHeight")
        print(">>>> exp height = $allFeatExpHeight")
        Toast.makeText(this, "hh: " + allFeatHoriz.height + ", eh: " + allFeatExp.height, Toast.LENGTH_SHORT).show()
        // hide allFeatExp in the beginning
        allFeatExp.layoutParams = LinearLayout.LayoutParams(allFeatExp.width, 0)*/
        val datesRecyclerView = findViewById<RecyclerView>(R.id.dates_recyclerview)
        dAdapter = CalenderAdapter(datesList)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        datesRecyclerView.layoutManager = layoutManager
        datesRecyclerView.itemAnimator = DefaultItemAnimator()
        datesRecyclerView.adapter = dAdapter
        prepareDatesHotData()
        /*dAdapter!!.setOnItemClickListener(object : DateAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                //Toast.makeText(baseContext, position.toString(), Toast.LENGTH_SHORT).show()
                Log.d("TAG", "#############$position")
                //if (position+1 == datesList[position].get(Calendar.DAY_OF_MONTH)) {
                //    Log.d("TAG", "#############Position+1: " + (position+1) + ", DoM: " + datesList[position].get(Calendar.DAY_OF_MONTH))
                v?.setBackgroundResource(R.drawable.shape_orange)
                Log.d("V", ((v as LinearLayout)[0] as TextView).text as String)
                //dAdapter!!.notifyDataSetChanged()
                //}
            }

        })*/

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
        /*for (i in 0 until mAdapter!!.count) {
            Log.d("mAdapter!!.count", mAdapter!!.count.toString())
            val listItem: View = mAdapter!!.getView(i, null, mListView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }*/
        //setting listview item in adapter
        val params: ViewGroup.LayoutParams = mListView.layoutParams
        params.height = totalHeight + mListView.dividerHeight * (mAdapter!!.count - 1) + 50  // arbitrary margin
        mListView.layoutParams = params
    }

    private fun prepareDatesHotData() {
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
}