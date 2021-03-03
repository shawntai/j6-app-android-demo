package com.siemens.j6_app_android_demo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.WorkOrderAppointmentAdapter
import com.siemens.j6_app_android_demo.adapters.WorkOrderCompletionAdapter
import com.siemens.j6_app_android_demo.models.WorkOrderAppointmentDataModel
import com.siemens.j6_app_android_demo.models.WorkOrderCompletionDataModel
import java.util.*

class WorkOrderFragment: Fragment() {

    lateinit var aListView: ListView
    var appointmentList: ArrayList<WorkOrderAppointmentDataModel> = ArrayList()
    var aAdapter: WorkOrderAppointmentAdapter? = null

    lateinit var cListView: ListView
    var completionList: ArrayList<WorkOrderCompletionDataModel> = ArrayList()
    var cAdapter: WorkOrderCompletionAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.work_order_frag_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // prepare appointment and completion data
        aListView = getView()!!.findViewById(R.id.appointment_listview)
        for (i in 1..3) {
            appointmentList.add(WorkOrderAppointmentDataModel("Clear Water Pump Replacement", Calendar.getInstance(), "Chiller Plant Room, Building 1", R.drawable.wo_appointment_thumbnail))
        }
        aAdapter = WorkOrderAppointmentAdapter(requireContext(), appointmentList)
        aListView.adapter = aAdapter

        cListView = getView()!!.findViewById(R.id.completion_listview)
        for (i in 1..2) {
            completionList.add(WorkOrderCompletionDataModel("Clear Water Pump #12 Replacement", Calendar.getInstance(), "Chiller Plant Room, Building 1"))
        }
        cAdapter = WorkOrderCompletionAdapter(requireContext(), completionList)
        cListView.adapter = cAdapter
        // adjust ll height
        /*var appointmentLL: LinearLayout = findViewById(R.id.appointment_ll)
        appointmentLL.layoutParams = LinearLayout.LayoutParams(appointmentLL.width, ((120+30)*scale+0.5f).toInt() * appointmentList.size)
        var completionLL: LinearLayout = findViewById(R.id.completion_ll)
        completionLL.layoutParams = LinearLayout.LayoutParams(completionLL.width, ((120+30)*scale+0.5f).toInt() * completionList.size)*/
        val scale: Float = resources.displayMetrics.density
        val appointmentLL: LinearLayout = getView()!!.findViewById(R.id.appointment_ll)
        val aParams = appointmentLL.layoutParams
        aParams.height = ((120+30)*scale+0.5f).toInt() * appointmentList.size
        appointmentLL.layoutParams = aParams
        val completionLL: LinearLayout = getView()!!.findViewById(R.id.completion_ll)
        val cParams = completionLL.layoutParams
        cParams.height = ((120+30)*scale+0.5f).toInt() * completionList.size
        completionLL.layoutParams = cParams
    }
}