package com.siemens.j6_app_android_demo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.NewWorkOrderAppointmentAdapter
import com.siemens.j6_app_android_demo.adapters.WorkOrderAppointmentAdapter
import com.siemens.j6_app_android_demo.adapters.WorkOrderCompletionAdapter
import com.siemens.j6_app_android_demo.models.*
import com.siemens.j6_app_android_demo.service.CreateWorkOrderCallBack
import com.siemens.j6_app_android_demo.service.WorkOrderService
import com.siemens.j6_app_android_demo.service.FetchWorkOrdersCallback
import com.siemens.j6_app_android_demo.service.UpdateWorkOrderCallBack
import java.util.*

class WorkOrderFragment: Fragment(), FetchWorkOrdersCallback, CreateWorkOrderCallBack, UpdateWorkOrderCallBack {

    lateinit var aListView: ListView
    var appointmentList: ArrayList<WorkOrderAppointmentDataModel> = ArrayList()
    var aAdapter: WorkOrderAppointmentAdapter? = null

    lateinit var nwoaListView: ListView
    var nwoappointmentList: ArrayList<WorkOrder> = ArrayList()
    var nwoaAdapter: NewWorkOrderAppointmentAdapter? = null

    lateinit var cListView: ListView
    var completionList: ArrayList<WorkOrderCompletionDataModel> = ArrayList()
    var cAdapter: WorkOrderCompletionAdapter? = null

    var exampleWO = WorkOrder(
        638,
        "test",
        "test",
        "test",
        Tenant(ContactPerson("","","","",""), 0, "", ""),
        "2021-04-29T10:31:23.309Z",
        "test",
        "test",
        "test",
        "test",
        "2021-04-29T10:31:23.309Z",
        "2021-04-29T10:31:23.309Z",
        IssuedBy("","",""),
        "2021-04-29T10:31:23.309Z",
        "null",
        "2021-04-29T10:31:23.309Z",
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        listOf(),
        Interruption("", SystemZone(""),"","",""),
        Completion("",""),
        Feedback("", listOf(), listOf(),""),
        "",
        ""
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.work_order_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // prepare appointment and completion data
//        aListView = getView()!!.findViewById(R.id.appointment_listview)
//        for (i in 1..3) {
//            appointmentList.add(WorkOrderAppointmentDataModel("Clear Water Pump Replacement", Calendar.getInstance(), "Chiller Plant Room, Building 1", R.drawable.wo_appointment_thumbnail))
//        }
//        aAdapter = WorkOrderAppointmentAdapter(requireContext(), appointmentList)
//        aListView.adapter = aAdapter
        nwoaListView = requireView().findViewById(R.id.appointment_listview)
//        nwoaAdapter = NewWorkOrderAppointmentAdapter(requireContext(), nwoappointmentList)
//        nwoaListView.adapter = nwoaAdapter

        WorkOrderService.fetchWorkOrdersListener = this
        WorkOrderService.fetchWorkOrdersData()
        WorkOrderService.createWorkOrderCallBack = this
        WorkOrderService.updateWorkOrderCallBack = this

//        cListView = requireView().findViewById(R.id.completion_listview)
//        for (i in 1..2) {
//            completionList.add(WorkOrderCompletionDataModel("Clear Water Pump #12 Replacement", Calendar.getInstance(), "Chiller Plant Room, Building 1"))
//        }
//        cAdapter = WorkOrderCompletionAdapter(requireContext(), completionList)
//        cListView.adapter = cAdapter
        // adjust ll height
        /*var appointmentLL: LinearLayout = findViewById(R.id.appointment_ll)
        appointmentLL.layoutParams = LinearLayout.LayoutParams(appointmentLL.width, ((120+30)*scale+0.5f).toInt() * appointmentList.size)
        var completionLL: LinearLayout = findViewById(R.id.completion_ll)
        completionLL.layoutParams = LinearLayout.LayoutParams(completionLL.width, ((120+30)*scale+0.5f).toInt() * completionList.size)*/
        val scale: Float = resources.displayMetrics.density
        val appointmentLL: LinearLayout = requireView().findViewById(R.id.appointment_ll)
        val aParams = appointmentLL.layoutParams
        val supposedElementHeight = ((120+30)*scale+0.5f).toInt()
        Log.d("Siemens2021", "supposedElementHeight: $supposedElementHeight")
        aParams.height = ((120+15)*scale+0.5f).toInt() * nwoappointmentList.size
        val elementHeight = appointmentLL[0].layoutParams.height
        Log.d("Siemens2021", "elementHeight: $elementHeight")
        Log.d("Siemens2021", "height: ${aParams.height}")
        appointmentLL.layoutParams = aParams

//        val completionLL: LinearLayout = requireView().findViewById(R.id.completion_ll)
//        val cParams = completionLL.layoutParams
//        cParams.height = ((120+30)*scale+0.5f).toInt() * completionList.size
//        completionLL.layoutParams = cParams

        requireView().findViewById<TextView>(R.id.woa).setOnClickListener {
            //WorkOrderService.createWorkOrder(nwo)

//            exampleWO.approvalComment = (1..9999).random().toString()
//            WorkOrderService.updateWorkOrder(37, exampleWO)
        }
    }

    override fun onWorkOrdersResult(result: List<WorkOrder>) {
        val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        for (wo in result) {
            if(wo.requestedAt != "" && wo.requestedAt != null) {
                val hourOfDay = wo.requestedAt?.substring(11, 13)?.toInt()
                wo.requestedAt =
                    wo.requestedAt?.substring(0, 10) + " " + (hourOfDay!!%12).toString() + wo.requestedAt?.substring(13, 16) + (if (hourOfDay >= 12) " PM" else " AM")
                //wo.requestedAt = monthNames[wo.requestedAt?.substring(5, 7)!!.toInt()-1].substring(0, 3) + " " + wo.requestedAt?.substring(8, 10) + ", " + wo.requestedAt?.subSequence(0, 4) + " " + wo.requestedAt?.subSequence(11, 16)
            }
            if(wo.plannedAt != "" && wo.plannedAt != null) {
                val hourOfDay = wo.plannedAt?.substring(11, 13)?.toInt()
                wo.plannedAt =
                    wo.plannedAt?.substring(0, 10) + " " + (hourOfDay!!%12).toString() + wo.plannedAt?.substring(13, 16) + (if (hourOfDay >= 12) " PM" else " AM")
            }
            if(wo.completedAt != null && wo.completedAt != "") {
                val hourOfDay = wo.completedAt?.substring(11, 13)?.toInt()
                wo.completedAt =
                    wo.completedAt?.substring(0, 10) + " " + (hourOfDay!!%12).toString() + wo.completedAt?.substring(13, 16) + (if (hourOfDay >= 12) " PM" else " AM")
            }
            if(wo.issuedAt != "" && wo.issuedAt != null) {
                val hourOfDay = wo.issuedAt?.substring(11, 13)?.toInt()
                wo.issuedAt =
                    wo.issuedAt?.substring(0, 10) + " " + (hourOfDay!!%12).toString() + wo.issuedAt?.substring(13, 16) + (if (hourOfDay >= 12) " PM" else " AM")
            }
        }
        nwoappointmentList.clear()
        nwoappointmentList.addAll(result)
        //nwoaAdapter!!.notifyDataSetChanged()
        nwoaAdapter = NewWorkOrderAppointmentAdapter(requireContext(), nwoappointmentList)
        nwoaListView.adapter = nwoaAdapter
        val scale: Float = resources.displayMetrics.density
        val appointmentLL: LinearLayout = requireView().findViewById(R.id.appointment_ll)
        val aParams = appointmentLL.layoutParams
        aParams.height = ((120+15)*scale+0.5f).toInt() * nwoappointmentList.size
        appointmentLL.layoutParams = aParams
    }

    fun onNewWorkOrderAdded(newWorkOrder: WorkOrder) {
        //nwoappointmentList.add(0, newWorkOrder)
        nwoaAdapter = NewWorkOrderAppointmentAdapter(requireContext(), nwoappointmentList)
        nwoaListView.adapter = nwoaAdapter
        val scale: Float = resources.displayMetrics.density
        val appointmentLL: LinearLayout = requireView().findViewById(R.id.appointment_ll)
        val aParams = appointmentLL.layoutParams
        aParams.height = ((120+15)*scale+0.5f).toInt() * nwoappointmentList.size
        appointmentLL.layoutParams = aParams
        WorkOrderService.createWorkOrder(newWorkOrder)
    }

    fun onWorkOrderEdited(workOrder: WorkOrder, index: Int) {
        nwoappointmentList[index] = workOrder
        nwoaAdapter = NewWorkOrderAppointmentAdapter(requireContext(), nwoappointmentList)
        nwoaListView.adapter = nwoaAdapter
    }

    override fun onWorkOrderCreated(workOrder: WorkOrder) {
        Log.d("AsiaSiemens2021", "onWorkOrderCreated: ")
        WorkOrderService.fetchWorkOrdersData()
    }

    override fun onWorkOrderUpdated(workOrder: WorkOrder) {
        Log.d("AsiaSiemens2021", "onWorkOrderUpdated: ")
        WorkOrderService.fetchWorkOrdersData()
    }
}