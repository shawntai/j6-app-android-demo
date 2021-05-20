package com.siemens.j6_app_android_demo.activities.home_page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.aigestudio.wheelpicker.WheelPicker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.CategorySelectionAdapter
import com.siemens.j6_app_android_demo.models.Attachment
import com.siemens.j6_app_android_demo.adapters.SurveyAdapter
import com.siemens.j6_app_android_demo.adapters.SystemZoneSelectionAdapter
import com.siemens.j6_app_android_demo.models.*
import com.siemens.j6_app_android_demo.service.*
import java.util.*
import kotlin.collections.ArrayList

class AddNewWorkOrderActivity : AppCompatActivity(), TenantsCallback, WorkOrdersCallback, CategoriesCallback, SystemZonesCallback {

    private var workOrderCreated: WorkOrder? =  null
    private var workOrderImported: WorkOrder? = null

    private lateinit var tenant_et: EditText
    private var tenantSelected: Tenant? = null
    private var days: ArrayList<Int> = ArrayList()
    //private var dayViewSelected: FrameLayout? = null
    private var startTimeSelected = Calendar.getInstance()
    private var finishTimeSelected = Calendar.getInstance()
    private var issuedTimeSelected = Calendar.getInstance()
    private var requestTimeSelected = Calendar.getInstance()
    private var monthAndYearDisplayed = Calendar.getInstance()
    //private var monthShown = Calendar.getInstance().get(Calendar.MONTH)
    //private var yearShown = Calendar.getInstance().get(Calendar.YEAR)
    private val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

    private var dateSelectionMode = -1
    private val SELECT_START_TIME = 0
    private val SELECT_FINISH_TIME = 1
    private val SELECT_ISSUED_TIME = 2
    private val SELECT_REQUEST_TIME = 3

    private val locationsSelected = ArrayList<Location>()
    private val employeesSelected = ArrayList<Employee>()
    private val materialsSelected = ArrayList<Material>()
    private val equipmentSelected = ArrayList<Equipment>()
    private val attachmentList = ArrayList<Attachment>()

    private val categoryList = ArrayList<String>()
    private lateinit var categorySelectionRV: RecyclerView
    private val categoryAdapter = CategorySelectionAdapter(categoryList)

    private val systemZoneList = ArrayList<SystemZone>()
    private lateinit var systemZoneRV: RecyclerView
    val systemZoneAdapter = SystemZoneSelectionAdapter(systemZoneList)

    private val surveyList = ArrayList<Survey>()
    lateinit var feedbackRecyclerView: RecyclerView
    val fAdapter = SurveyAdapter(surveyList)

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_work_order)

        // get existing work order and edit
        workOrderImported = Gson().fromJson(intent.getStringExtra("work_order"), WorkOrder::class.java)
        workOrderImported?.let {
            findViewById<TextView>(R.id.title).text = "Edit Work Order"
            findViewById<TextView>(R.id.tenant).text = workOrderImported!!.tenant.name
            findViewById<TextView>(R.id.unit).text = workOrderImported!!.tenant.unit
            findViewById<TextView>(R.id.contact_person).text = workOrderImported!!.tenant.contactPerson.name
            findViewById<TextView>(R.id.request_time_entered).text = workOrderImported!!.requestedAt
            findViewById<TextView>(R.id.category).text = workOrderImported!!.category
            findViewById<EditText>(R.id.work_order_details).setText(workOrderImported!!.desc)
            findViewById<TextView>(R.id.start_time_entered).text = workOrderImported!!.plannedAt
            findViewById<TextView>(R.id.finish_time_entered).text = workOrderImported!!.completedAt
            findViewById<EditText>(R.id.issued_by).setText(workOrderImported!!.issuedBy.name)
            findViewById<TextView>(R.id.issued_time_entered).text = workOrderImported!!.issuedAt

            tenantSelected = workOrderImported!!.tenant

            for (loc in workOrderImported!!.locations)
                loc.isSelected = true
            locationsSelected.addAll(workOrderImported!!.locations)
            var locsString = ""
            for (loc in locationsSelected) {
                locsString += (loc.building.name + " " + loc.level + " " + loc.room + ", ")
            }
            val locsSelected = findViewById<TextView>(R.id.locs_selected)
            if(locsString.isNotEmpty()) {
                locsSelected.text = locsString.substring(0, locsString.length - 2)
                locsSelected.setTypeface(null, Typeface.BOLD)
            } else {
                locsSelected.text = getString(R.string.building_level_room)
            }

            employeesSelected.addAll(workOrderImported!!.engineers)
            var empsString = ""
            for (emp in employeesSelected) {
                empsString += (emp.name + " " + emp.department + " " + emp.position + ", ")
            }
            val empsSelected = findViewById<TextView>(R.id.emps_selected)
            if(empsString.isNotEmpty()) {
                empsSelected.text = empsString.substring(0, empsString.length - 2)
                empsSelected.setTypeface(null, Typeface.BOLD)
            } else {
                empsSelected.text = getString(R.string.employee_department_position)
            }

            for (mat in workOrderImported!!.materials)
                mat.isSelected = true
            materialsSelected.addAll(workOrderImported!!.materials)

            equipmentSelected.addAll(workOrderImported!!.equipments)

            findViewById<TextView>(R.id.interruption_description).text = workOrderImported!!.interruption.remark
            findViewById<TextView>(R.id.system_zone).text = workOrderImported!!.interruption.systemZone.name
            findViewById<TextView>(R.id.sz_failure).text = workOrderImported!!.interruption.systemZoneFailureLevel
            findViewById<TextView>(R.id.ws_failure).text = workOrderImported!!.interruption.wholeSystemFailureLevel

            findViewById<TextView>(R.id.status).text = workOrderImported!!.status
            findViewById<EditText>(R.id.remark).setText(workOrderImported!!.completion.remark)

            surveyList.clear()
            surveyList.addAll(workOrderImported!!.feedback.surveys)
            fAdapter.notifyDataSetChanged()
            findViewById<EditText>(R.id.input).setText(workOrderImported!!.feedback.comment)

        }

        initView()

        WorkOrderService.workOrdersListener = this
        WorkOrderService.fetchWorkOrdersData()
        WorkOrderService.categoriesListener = this
        WorkOrderService.fetchCategoriesData()
        WorkOrderService.systemZonesListener = this
        WorkOrderService.fetchSystemZonesData()

        findViewById<TextView>(R.id.general_info_tab).setOnClickListener {
            val sv = findViewById<ScrollView>(R.id.nwo_scroll_wiew)
            sv.smoothScrollTo(0, 0)
            updateTabsColor(it as TextView)
        }
        findViewById<TextView>(R.id.detailed_situation_tab).setOnClickListener {
            val sv = findViewById<ScrollView>(R.id.nwo_scroll_wiew)
            val scale: Float = resources.displayMetrics.density
            val scrollPosition = IntArray(2)
            findViewById<LinearLayout>(R.id.select_employees).getLocationOnScreen(scrollPosition)
            val offset = /*(720 * scale + 0.5f).toInt() -*/ scrollPosition[1] - getNavigationBarHeight()
            sv.smoothScrollBy(0, offset)
            updateTabsColor(it as TextView)
        }
        findViewById<TextView>(R.id.customer_feed_tab).setOnClickListener {
            val sv = findViewById<ScrollView>(R.id.nwo_scroll_wiew)
            val scrollPosition = IntArray(2)
            findViewById<LinearLayout>(R.id.select_attachment).getLocationOnScreen(scrollPosition)
            val offset = scrollPosition[1] - getNavigationBarHeight()
            sv.smoothScrollBy(0, offset)
            updateTabsColor(it as TextView)
        }

        WorkOrderService.tenantsListener = this
        WorkOrderService.fetchTenantsData()

        val cat_ll = findViewById<LinearLayout>(R.id.category_selection)
        cat_ll.setOnClickListener {  }

        findViewById<View>(R.id.dark_shade_overlay).setOnClickListener {  }

        findViewById<LinearLayout>(R.id.select_category_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.category_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(cat_ll, transitionBottom)
            cat_ll.visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_category_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.category_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(cat_ll, transitionBottom)
            cat_ll.visibility = View.GONE
        }
//        for (cat in categorySelectionRV) {
//            (cat as LinearLayout).setOnClickListener {
//                findViewById<TextView>(R.id.category).text = (cat[1] as TextView).text
//            }
//        }

        val pst_ll = findViewById<LinearLayout>(R.id.time_selection)
        pst_ll.setOnClickListener {  }
        findViewById<LinearLayout>(R.id.select_start_time_btn).setOnClickListener {

            dateSelectionMode = SELECT_START_TIME

            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.VISIBLE

            monthAndYearDisplayed.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }
        findViewById<LinearLayout>(R.id.select_finish_time_btn).setOnClickListener {

            dateSelectionMode = SELECT_FINISH_TIME

            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.VISIBLE

            monthAndYearDisplayed.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }
        findViewById<LinearLayout>(R.id.select_issued_time_btn).setOnClickListener {

            dateSelectionMode = SELECT_ISSUED_TIME

            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.VISIBLE

            monthAndYearDisplayed.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }
        findViewById<LinearLayout>(R.id.select_request_time_btn).setOnClickListener {

            dateSelectionMode = SELECT_REQUEST_TIME

            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.VISIBLE

            monthAndYearDisplayed.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }
        findViewById<ImageView>(R.id.close_time_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.GONE
        }

        findViewById<ImageView>(R.id.prev_month).setOnClickListener {
            //setDaysByMonth((--monthShown))
            monthAndYearDisplayed.add(Calendar.MONTH, -1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }

        findViewById<ImageView>(R.id.next_month).setOnClickListener {
            //setDaysByMonth((++monthShown))
            monthAndYearDisplayed.add(Calendar.MONTH, 1)
            setDaysByMonthAndYear(monthAndYearDisplayed)
            updateCalender()
        }

        //val timeSelected = findViewById<TextView>(R.id.time_selected)

        val hours = ArrayList<String>()
        var ampm = 0
        for (i in 0..11)
            hours.add((if(i<10) "0" else "") + i.toString())
        findViewById<WheelPicker>(R.id.hour_wheelpicker).data = hours
        findViewById<WheelPicker>(R.id.hour_wheelpicker).setOnItemSelectedListener { picker, data, position ->
            if (dateSelectionMode == SELECT_START_TIME) {
                startTimeSelected.set(Calendar.HOUR_OF_DAY, position + ampm)
                /*timeSelected.text = startTimeSelected.get(Calendar.HOUR_OF_DAY)
                    .toString() + ":" + (if (startTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + startTimeSelected.get(
                    Calendar.MINUTE
                ).toString()*/
            } else if (dateSelectionMode == SELECT_FINISH_TIME) {
                finishTimeSelected.set(Calendar.HOUR_OF_DAY, position + ampm)
                /*timeSelected.text = finishTimeSelected.get(Calendar.HOUR_OF_DAY)
                    .toString() + ":" + (if (finishTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + finishTimeSelected.get(
                    Calendar.MINUTE
                ).toString()*/
            } else if (dateSelectionMode == SELECT_ISSUED_TIME) {
                issuedTimeSelected.set(Calendar.HOUR_OF_DAY, position + ampm)
                /*timeSelected.text = issuedTimeSelected.get(Calendar.HOUR_OF_DAY)
                    .toString() + ":" + (if (issuedTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + issuedTimeSelected.get(
                    Calendar.MINUTE
                ).toString()*/
            } else if (dateSelectionMode == SELECT_REQUEST_TIME) {
                requestTimeSelected.set(Calendar.HOUR_OF_DAY, position + ampm)
            }
            /*if (!findViewById<TextView>(R.id.date_selected).text.isBlank()) {
                findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
            }*/
        }
        findViewById<WheelPicker>(R.id.ampm_wheelpicker).data = listOf("AM", "PM")
        findViewById<WheelPicker>(R.id.ampm_wheelpicker).setOnItemSelectedListener { picker, data, position ->
            ampm = if (position == 0) 0 else 12
        }

        val minutes = ArrayList<String>()
        for (i in 0..59)
            minutes.add((if(i<10) "0" else "") + i.toString())
        findViewById<WheelPicker>(R.id.minute_wheelpicker).data = minutes
        findViewById<WheelPicker>(R.id.minute_wheelpicker).setOnItemSelectedListener { picker, data, position ->
            if (dateSelectionMode == SELECT_START_TIME) {
                startTimeSelected.set(Calendar.MINUTE, position)
                /*timeSelected.text =
                    (if (startTimeSelected.get(Calendar.HOUR_OF_DAY) < 10) "0" else "") + startTimeSelected.get(
                        Calendar.HOUR_OF_DAY
                    ).toString() + ":" + (if (startTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + startTimeSelected.get(
                        Calendar.MINUTE
                    ).toString()*/
            } else if (dateSelectionMode == SELECT_FINISH_TIME) {
                finishTimeSelected.set(Calendar.MINUTE, position)
                /*timeSelected.text =
                    (if (finishTimeSelected.get(Calendar.HOUR_OF_DAY) < 10) "0" else "") + finishTimeSelected.get(
                        Calendar.HOUR_OF_DAY
                    ).toString() + ":" + (if (finishTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + finishTimeSelected.get(
                        Calendar.MINUTE
                    ).toString()*/
            } else if (dateSelectionMode == SELECT_ISSUED_TIME) {
                issuedTimeSelected.set(Calendar.MINUTE, position)
                /*timeSelected.text =
                    (if (issuedTimeSelected.get(Calendar.HOUR_OF_DAY) < 10) "0" else "") + issuedTimeSelected.get(
                        Calendar.HOUR_OF_DAY
                    ).toString() + ":" + (if (issuedTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + issuedTimeSelected.get(
                        Calendar.MINUTE
                    ).toString()*/
            } else if (dateSelectionMode == SELECT_REQUEST_TIME) {
                requestTimeSelected.set(Calendar.MINUTE, position)
            }
            /*if (!findViewById<TextView>(R.id.date_selected).text.isBlank()) {
                findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
            }*/
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            // close
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.time_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(pst_ll, transitionBottom)
            pst_ll.visibility = View.GONE

            // update start time
            if (dateSelectionMode == SELECT_START_TIME) {
                findViewById<TextView>(R.id.start_time_entered).text =
                    startTimeSelected.get(Calendar.YEAR)
                        .toString() + "-" + (if (startTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (startTimeSelected.get(
                        Calendar.MONTH
                    ) + 1).toString() + "-" + (if (startTimeSelected.get(Calendar.DAY_OF_MONTH) < 10) "0" else "") + startTimeSelected.get(Calendar.DAY_OF_MONTH)
                        .toString() + " " + (if (startTimeSelected.get(Calendar.HOUR) < 10) "0" else "") + startTimeSelected.get(
                        Calendar.HOUR
                    ).toString() + ":" + (if (startTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + startTimeSelected.get(
                        Calendar.MINUTE
                    ).toString() + if (ampm == 0) "AM" else "PM"
            } else if (dateSelectionMode == SELECT_FINISH_TIME) {
                findViewById<TextView>(R.id.finish_time_entered).text =
                    finishTimeSelected.get(Calendar.YEAR)
                        .toString() + "-" + (if (finishTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (finishTimeSelected.get(
                        Calendar.MONTH
                    ) + 1).toString() + "-"  + (if (finishTimeSelected.get(Calendar.DAY_OF_MONTH) < 10) "0" else "")+ finishTimeSelected.get(Calendar.DAY_OF_MONTH)
                        .toString() + " " + (if (finishTimeSelected.get(Calendar.HOUR) < 10) "0" else "") + finishTimeSelected.get(
                        Calendar.HOUR
                    ).toString() + ":" + (if (finishTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + finishTimeSelected.get(
                        Calendar.MINUTE
                    ).toString() + if (ampm == 0) "AM" else "PM"
            } else if (dateSelectionMode == SELECT_ISSUED_TIME) {
                findViewById<TextView>(R.id.issued_time_entered).text =
                    issuedTimeSelected.get(Calendar.YEAR)
                        .toString() + "-" + (if (issuedTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (issuedTimeSelected.get(
                        Calendar.MONTH
                    ) + 1).toString() + "-"  + (if (issuedTimeSelected.get(Calendar.DAY_OF_MONTH) < 10) "0" else "") + issuedTimeSelected.get(Calendar.DAY_OF_MONTH)
                        .toString() + " " + (if (issuedTimeSelected.get(Calendar.HOUR) < 10) "0" else "") + issuedTimeSelected.get(
                        Calendar.HOUR
                    ).toString() + ":" + (if (issuedTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + issuedTimeSelected.get(
                        Calendar.MINUTE
                    ).toString() + if (ampm == 0) "AM" else "PM"
            } else if (dateSelectionMode == SELECT_REQUEST_TIME) {
                findViewById<TextView>(R.id.request_time_entered).text =
                    requestTimeSelected.get(Calendar.YEAR)
                        .toString() + "-" + (if (requestTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (requestTimeSelected.get(
                        Calendar.MONTH
                    ) + 1).toString() + "-"  + (if (requestTimeSelected.get(Calendar.DAY_OF_MONTH) < 10) "0" else "") + requestTimeSelected.get(Calendar.DAY_OF_MONTH)
                        .toString() + " " + (if (requestTimeSelected.get(Calendar.HOUR) < 10) "0" else "") + requestTimeSelected.get(
                        Calendar.HOUR
                    ).toString() + ":" + (if (requestTimeSelected.get(Calendar.MINUTE) < 10) "0" else "") + requestTimeSelected.get(
                        Calendar.MINUTE
                    ).toString() + if (ampm == 0) "AM" else "PM"
            }
        }

        findViewById<LinearLayout>(R.id.select_location).setOnClickListener {
            val i = Intent(this, SelectLocationActivity::class.java)
            i.putExtra("location_list", locationsSelected)
            selectLocation.launch(i)
        }
        findViewById<LinearLayout>(R.id.select_employees).setOnClickListener {
            val i = Intent(this, SelectEmployeeActivity::class.java)
            i.putExtra("employee_list", employeesSelected)
            selectEmployees.launch(i)
        }
        findViewById<LinearLayout>(R.id.select_material).setOnClickListener {
            val i = Intent(this, SelectMaterialActivity::class.java)
            i.putExtra("material_list", materialsSelected)
            selectMaterial.launch(i)
        }
        findViewById<LinearLayout>(R.id.select_equipment).setOnClickListener {
            val i = Intent(this, SelectEquipmentActivity::class.java)
            i.putExtra("equipment_list", equipmentSelected)
            selectEquipment.launch(i)
        }
        findViewById<LinearLayout>(R.id.select_attachment).setOnClickListener {
            selectAttachment.launch(Intent(this, AddAttachmentActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.enter_interruption_description).setOnClickListener {
            enterInterruptionDescription.launch(Intent(this, InterruptDescriptionActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.select_system_zone_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.system_zone_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.system_zone_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.system_zone_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_system_zone_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.system_zone_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.system_zone_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.system_zone_selection).visibility = View.GONE
        }
        for (sysZ in findViewById<LinearLayout>(R.id.system_zone_selection)) {
            sysZ.setOnClickListener {
                if(sysZ is TextView) {
                    findViewById<TextView>(R.id.system_zone).text = sysZ.text
                    sysZ.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }

        findViewById<LinearLayout>(R.id.select_tenant_interrupt_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.tenant_interrupt_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.tenant_interrupt_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.tenant_interrupt_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_tenant_interrupt_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.tenant_interrupt_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.tenant_interrupt_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.tenant_interrupt_selection).visibility = View.GONE
        }
        for (interrupt in findViewById<LinearLayout>(R.id.tenant_interrupt_selection)) {
            interrupt.setOnClickListener {
                if(interrupt is TextView) {
                    findViewById<TextView>(R.id.tenant_interrupt).text = interrupt.text
                    interrupt.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }
        
        findViewById<LinearLayout>(R.id.select_sz_failure_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.sz_failure_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.sz_failure_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.sz_failure_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_sz_failure_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.sz_failure_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.sz_failure_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.sz_failure_selection).visibility = View.GONE
        }
        for (szf in findViewById<LinearLayout>(R.id.sz_failure_selection)) {
            szf.setOnClickListener {
                if(szf is TextView) {
                    findViewById<TextView>(R.id.sz_failure).text = szf.text
                    szf.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }

        findViewById<LinearLayout>(R.id.select_ws_failure_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.ws_failure_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.ws_failure_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.ws_failure_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_ws_failure_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.ws_failure_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.ws_failure_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.ws_failure_selection).visibility = View.GONE
        }
        for (wsf in findViewById<LinearLayout>(R.id.ws_failure_selection)) {
            wsf.setOnClickListener {
                if(wsf is TextView) {
                    findViewById<TextView>(R.id.ws_failure).text = wsf.text
                    wsf.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }

        findViewById<LinearLayout>(R.id.select_status_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.status_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.status_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.status_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_status_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.status_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.status_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.status_selection).visibility = View.GONE
        }
        for (status in findViewById<LinearLayout>(R.id.status_selection)) {
            status.setOnClickListener {
                if(status is TextView) {
                    findViewById<TextView>(R.id.status).text = status.text
                    for (j in findViewById<LinearLayout>(R.id.status_selection))
                        if (j is TextView)
                            j.setTextColor(getColor(R.color.white))
                    status.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }

        // feedback section
        feedbackRecyclerView = findViewById(R.id.feedback_recyclerview)
        var layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        feedbackRecyclerView.layoutManager = layoutManager
        feedbackRecyclerView.itemAnimator = DefaultItemAnimator()
        feedbackRecyclerView.adapter = fAdapter
        val surveyTitles = arrayOf(
            "1. Provision of security and safety to assigned area",
            "2. Time and attendance",
            "3. Relationship with fellow employees",
            "4. Knowledge and application of laws and regulations",
            "5. Maintenance of equipment",
            "6. Anticipation and action in emergency situations"
        )
        if (surveyList.isEmpty()) {
            for (i in 0..5) {
                surveyList.add(Survey(surveyTitles[i], i))
                fAdapter.notifyDataSetChanged()
            }
        }

        // save work order
        findViewById<ImageView>(R.id.done).setOnClickListener {
            val resultIntent = Intent()
            workOrderCreated = WorkOrder(
                workOrderImported?.id ?: WorkOrder.ID,
                workOrderImported?.workorderId ?: WorkOrder.ID.toString(),
                workOrderImported?.recordName ?: "Work Order " + WorkOrder.ID++.toString(),
                findViewById<TextView>(R.id.status).text.toString(),
                tenantSelected!!,
                findViewById<TextView>(R.id.request_time_entered).text.toString(),
                findViewById<TextView>(R.id.category).text.toString(),
                findViewById<TextView>(R.id.category).text.toString(),
                findViewById<TextView>(R.id.category).text.toString(),
                workOrderImported?.comment ?: "No comment",
                findViewById<TextView>(R.id.start_time_entered).text.toString(),
                findViewById<TextView>(R.id.finish_time_entered).text.toString(),
                IssuedBy("", "", findViewById<TextView>(R.id.issued_by).text.toString()),
                findViewById<TextView>(R.id.issued_time_entered).text.toString(),
                workOrderImported?.approvalComment ?: "Approval comment",
                null,
                //null,
                locationsSelected,
                employeesSelected,
                materialsSelected,
                equipmentSelected,
                attachmentList,
                Interruption(
                    findViewById<TextView>(R.id.interruption_description).text.toString(),
                    SystemZone(findViewById<TextView>(R.id.system_zone).text.toString()),
                    "",
                    findViewById<TextView>(R.id.sz_failure).text.toString(),
                    findViewById<TextView>(R.id.ws_failure).text.toString(),
                ),
                Completion(null, null),
                Feedback(
                    findViewById<EditText>(R.id.input).text.toString(),
                    surveyList,
                    null,
                    null
                ),
                findViewById<TextView>(R.id.issued_time_entered).text.toString(),
                findViewById<TextView>(R.id.issued_time_entered).text.toString(),
                "https://tickikids.ams3.cdn.digitaloceanspaces.com/z3.cache/gallery/organizations/32/image_5aa51cea20e6e7.73556469.jpg",
                findViewById<TextView>(R.id.category).text.toString(),
                "",
                "",
                null
            )
            if (workOrderImported == null) { // add new work order instead of edit old one
                resultIntent.putExtra("nwo", Gson().toJson(workOrderCreated))
            } else {
                resultIntent.putExtra("wo_edited", Gson().toJson(workOrderCreated))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun initView() {
        tenant_et = findViewById(R.id.tenant)
        categorySelectionRV = findViewById(R.id.category_selection_recycler_view)
        systemZoneRV = findViewById(R.id.system_zone_selection_recycler_view)
    }

    private fun setDaysByMonthAndYear(monthAndYear: Calendar) {
        days = ArrayList()
        var cal = Calendar.getInstance()
        cal.set(Calendar.MONTH, monthAndYear.get(Calendar.MONTH))  // n -> (n+1)th month
        val thisMonth = cal.get(Calendar.MONTH)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        var firstDay = cal.get(Calendar.DAY_OF_WEEK)
        while (cal.get(Calendar.MONTH) == thisMonth) {
            days.add(cal.get(Calendar.DAY_OF_MONTH))
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        cal.set(Calendar.MONTH, thisMonth)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        while (firstDay > 1) {
            cal.add(Calendar.DATE, -1)
            days.add(0, cal.get(Calendar.DAY_OF_MONTH))
            firstDay--
        }
        var dayInNextMonth = 1
        while (days.size % 7 != 0) {
            days.add(dayInNextMonth++)
        }
        Log.d("Days in Month", days.toString())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun updateCalender() {
        val calender = findViewById<LinearLayout>(R.id.calender)
        for (week in calender) week.visibility = View.VISIBLE
        var iWeek = 2
        var iDay = 0
        while (iDay < days.size) {
            val week = calender[iWeek++] as LinearLayout
            for (day in week) {
                val dayNum = days[iDay++]
                ((day as FrameLayout)[1] as TextView).text = dayNum.toString()
                if ((iWeek == 3 && dayNum > 20) || (iWeek == days.size/7 + 2 && dayNum < 7)) {
                    (day[1] as TextView).setTextColor(getColor(R.color.date_from_other_months))
                } else {
                    (day[1] as TextView).setTextColor(getColor(R.color.white))
                }
                day.setOnClickListener {
                    if (dateSelectionMode == SELECT_START_TIME) {
                        if ((day[1] as TextView).currentTextColor != getColor(R.color.date_from_other_months)) {
                            day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                            startTimeSelected.set(
                                monthAndYearDisplayed.get(Calendar.YEAR),
                                monthAndYearDisplayed.get(Calendar.MONTH),
                                dayNum
                            )
                            /*findViewById<TextView>(R.id.date_selected).text =
                                startTimeSelected.get(Calendar.YEAR)
                                    .toString() + "-" + (if (startTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (startTimeSelected.get(
                                    Calendar.MONTH
                                ) + 1).toString() + (if (dayNum < 10) "0" else "") + "-" + dayNum.toString()*/
                            refreshSelectedDate()
                        }
                        /*if (!findViewById<TextView>(R.id.time_selected).text.isBlank()) {
                            findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
                        }*/
                    } else if (dateSelectionMode == SELECT_FINISH_TIME) {
                        if ((day[1] as TextView).currentTextColor != getColor(R.color.date_from_other_months)) {
                            day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                            finishTimeSelected.set(
                                monthAndYearDisplayed.get(Calendar.YEAR),
                                monthAndYearDisplayed.get(Calendar.MONTH),
                                dayNum
                            )
                            /*findViewById<TextView>(R.id.date_selected).text =
                                finishTimeSelected.get(Calendar.YEAR)
                                    .toString() + "-" + (if (finishTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (finishTimeSelected.get(
                                    Calendar.MONTH
                                ) + 1).toString() + (if (dayNum < 10) "0" else "") + "-" + dayNum.toString()*/
                            refreshSelectedDate()
                        }
                        /*if (!findViewById<TextView>(R.id.time_selected).text.isBlank()) {
                            findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
                        }*/
                    } else if (dateSelectionMode == SELECT_ISSUED_TIME) {
                        if ((day[1] as TextView).currentTextColor != getColor(R.color.date_from_other_months)) {
                            day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                            issuedTimeSelected.set(
                                monthAndYearDisplayed.get(Calendar.YEAR),
                                monthAndYearDisplayed.get(Calendar.MONTH),
                                dayNum
                            )
                            /*findViewById<TextView>(R.id.date_selected).text =
                                issuedTimeSelected.get(Calendar.YEAR)
                                    .toString() + "-" + (if (issuedTimeSelected.get(Calendar.MONTH) < 9) "0" else "") + (issuedTimeSelected.get(
                                    Calendar.MONTH
                                ) + 1).toString() + (if (dayNum < 10) "0" else "") + "-" + dayNum.toString()*/
                            refreshSelectedDate()
                        }
                        /*if (!findViewById<TextView>(R.id.time_selected).text.isBlank()) {
                            findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
                        }*/
                    } else if (dateSelectionMode == SELECT_REQUEST_TIME) {
                        if ((day[1] as TextView).currentTextColor != getColor(R.color.date_from_other_months)) {
                            day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                            requestTimeSelected.set(
                                monthAndYearDisplayed.get(Calendar.YEAR),
                                monthAndYearDisplayed.get(Calendar.MONTH),
                                dayNum
                            )
                            refreshSelectedDate()
                        }
                        /*if (!findViewById<TextView>(R.id.time_selected).text.isBlank()) {
                            findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
                        }*/
                    }
                }
            }
        }
        while (iWeek < 8) {
            calender[iWeek++].visibility = View.GONE
        }
        refreshSelectedDate()

        findViewById<TextView>(R.id.month_year).text = monthNames[monthAndYearDisplayed.get(Calendar.MONTH)] + " " + monthAndYearDisplayed.get(Calendar.YEAR).toString()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun refreshSelectedDate() {
        val calender = findViewById<LinearLayout>(R.id.calender)
        if (dateSelectionMode == SELECT_START_TIME) {
            for (i in 2 until calender.size) {
                val week = calender[i]
                for (day in week as LinearLayout) {
                    day as FrameLayout
                    val daYear = startTimeSelected.get(Calendar.YEAR)
                    if (monthAndYearDisplayed.get(Calendar.YEAR) == daYear
                        && monthAndYearDisplayed.get(Calendar.MONTH) == startTimeSelected.get(
                            Calendar.MONTH
                        )
                        && (day[1] as TextView).text == startTimeSelected.get(Calendar.DAY_OF_MONTH)
                            .toString()
                        && (day[1] as TextView).currentTextColor == getColor(R.color.white)
                    ) {
                        day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                    } else {
                        day[0].setBackgroundResource(R.drawable.shape_darker_light_gray_calender_day)
                    }
                }
            }
        } else if (dateSelectionMode == SELECT_FINISH_TIME) {
            for (i in 2 until calender.size) {
                val week = calender[i]
                for (day in week as LinearLayout) {
                    day as FrameLayout
                    val daYear = finishTimeSelected.get(Calendar.YEAR)
                    if (monthAndYearDisplayed.get(Calendar.YEAR) == daYear
                        && monthAndYearDisplayed.get(Calendar.MONTH) == finishTimeSelected.get(
                            Calendar.MONTH
                        )
                        && (day[1] as TextView).text == finishTimeSelected.get(Calendar.DAY_OF_MONTH)
                            .toString()
                        && (day[1] as TextView).currentTextColor == getColor(R.color.white)
                    ) {
                        day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                    } else {
                        day[0].setBackgroundResource(R.drawable.shape_darker_light_gray_calender_day)
                    }
                }
            }
        } else if (dateSelectionMode == SELECT_ISSUED_TIME) {
            for (i in 2 until calender.size) {
                val week = calender[i]
                for (day in week as LinearLayout) {
                    day as FrameLayout
                    val daYear = issuedTimeSelected.get(Calendar.YEAR)
                    if (monthAndYearDisplayed.get(Calendar.YEAR) == daYear
                        && monthAndYearDisplayed.get(Calendar.MONTH) == issuedTimeSelected.get(
                            Calendar.MONTH
                        )
                        && (day[1] as TextView).text == issuedTimeSelected.get(Calendar.DAY_OF_MONTH)
                            .toString()
                        && (day[1] as TextView).currentTextColor == getColor(R.color.white)
                    ) {
                        day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                    } else {
                        day[0].setBackgroundResource(R.drawable.shape_darker_light_gray_calender_day)
                    }
                }
            }
        } else if (dateSelectionMode == SELECT_REQUEST_TIME) {
            for (i in 2 until calender.size) {
                val week = calender[i]
                for (day in week as LinearLayout) {
                    day as FrameLayout
                    val daYear = requestTimeSelected.get(Calendar.YEAR)
                    if (monthAndYearDisplayed.get(Calendar.YEAR) == daYear
                        && monthAndYearDisplayed.get(Calendar.MONTH) == requestTimeSelected.get(
                            Calendar.MONTH
                        )
                        && (day[1] as TextView).text == requestTimeSelected.get(Calendar.DAY_OF_MONTH)
                            .toString()
                        && (day[1] as TextView).currentTextColor == getColor(R.color.white)
                    ) {
                        day[0].setBackgroundResource(R.drawable.shape_orange_calender_day)
                    } else {
                        day[0].setBackgroundResource(R.drawable.shape_darker_light_gray_calender_day)
                    }
                }
            }
        }
    }

    private val selectLocation = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //val data: Intent? = result.data
            //data!!.getSerializableExtra("locs")
            val locs = result.data!!.getSerializableExtra("locs") as ArrayList<Location>?
            if (locs != null) {
                locationsSelected.clear()
                locationsSelected.addAll(locs)
                var locsString = ""
                for (loc in locationsSelected) {
                    locsString += (loc.building.name + " " + loc.level + " " + loc.room + ", ")
                }
                val locsSelected = findViewById<TextView>(R.id.locs_selected)
                if(locsString.isNotEmpty()) {
                    locsSelected.text = locsString.substring(0, locsString.length - 2)
                    locsSelected.setTypeface(null, Typeface.BOLD)
                } else {
                    locsSelected.text = getString(R.string.building_level_room)
                }
            }
        }
    }

    private val selectEmployees = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val emps = result.data!!.getSerializableExtra("emps") as ArrayList<Employee>?
            emps?.let {
                employeesSelected.clear()
                employeesSelected.addAll(emps)
                var empsString = ""
                for (emp in employeesSelected) {
                    empsString += (emp.name + " " + emp.department + " " + emp.position + ", ")
                }
                val empsSelected = findViewById<TextView>(R.id.emps_selected)
                if(empsString.isNotEmpty()) {
                    empsSelected.text = empsString.substring(0, empsString.length - 2)
                    empsSelected.setTypeface(null, Typeface.BOLD)
                } else {
                    empsSelected.text = getString(R.string.employee_department_position)
                }
            }
        }
    }

    private val selectMaterial = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mats = result.data!!.getSerializableExtra("mat") as ArrayList<Material>
            if (mats.size > 0) {
                materialsSelected.clear()
                materialsSelected.addAll(mats)
            }
        }
    }

    private val selectEquipment = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val eq = result.data!!.getSerializableExtra("equip_list") as ArrayList<Equipment>
            equipmentSelected.clear()
            equipmentSelected.addAll(eq)
        }
    }

    private val selectAttachment = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //val att = result.data!!.getSerializableExtra("attach_list") as ArrayList<AttachmentAdapter.Attachment>?
            val myType = object : TypeToken<ArrayList<Attachment>>() {}.type
            val att = Gson().fromJson<ArrayList<Attachment>>(result.data!!.getStringExtra("attach_list"), myType)
            attachmentList.clear()
            if(att != null) attachmentList.addAll(att)
        }
    }

    private val enterInterruptionDescription = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data!!.getStringExtra("interruption_description") != null) {
            findViewById<TextView>(R.id.interruption_description).text = result.data!!.getStringExtra("interruption_description")
        }
    }

    private fun updateTabsColor(view: TextView) {
        findViewById<TextView>(R.id.general_info_tab).setTextColor(getColor(R.color.white))
        findViewById<TextView>(R.id.customer_feed_tab).setTextColor(getColor(R.color.white))
        findViewById<TextView>(R.id.detailed_situation_tab).setTextColor(getColor(R.color.white))
        view.setTextColor(getColor(R.color.j6_light_orange))
    }

    private fun getNavigationBarHeight(): Int {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight: Int = metrics.heightPixels
        windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight: Int = metrics.heightPixels
        return if (realHeight > usableHeight) realHeight - usableHeight else 0
    }

    private fun mod12(num: Int): Int {
        return (if (num<0) num + ((-num/12)+1)*12 else num) % 12
    }

    override fun onTenantsResult(result: List<Tenant>) {

        val tenantNameList = ArrayList<String>()
        for (tenant in result)
            tenantNameList.add(tenant.name)

        val tenant_actv = findViewById<AutoCompleteTextView>(R.id.tenant)
        tenant_actv.threshold = 1
        tenant_actv.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, tenantNameList))
        tenant_actv.doOnTextChanged { text, start, before, count ->
            for (tenant in result) {
                if (tenant.name == text.toString()) {
                    tenantSelected = tenant
                    findViewById<EditText>(R.id.unit).setText(tenant.unit)
                    findViewById<EditText>(R.id.contact_person).setText(tenant.contactPerson.name)
                }
            }
        }
    }

    override fun onWorkOrdersResult(result: List<WorkOrder>) {
        Log.d("TAG", "onWorkOrdersResult: ")
    }

    override fun onCategoriesResult(result: List<String>) {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        categorySelectionRV.layoutManager = layoutManager
        categorySelectionRV.itemAnimator = DefaultItemAnimator()
        categorySelectionRV.adapter = categoryAdapter
        categoryList.addAll(result)
        categoryAdapter.notifyDataSetChanged()
    }

    override fun onSystemZonesResult(result: List<SystemZone>) {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        systemZoneRV.layoutManager = layoutManager
        systemZoneRV.itemAnimator = DefaultItemAnimator()
        systemZoneRV.adapter = systemZoneAdapter
        systemZoneList.addAll(result)
        systemZoneAdapter.notifyDataSetChanged()
    }

    fun finishThisActivity(view: View) {
        finish()
    }
}