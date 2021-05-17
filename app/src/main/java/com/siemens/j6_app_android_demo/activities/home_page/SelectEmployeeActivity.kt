package com.siemens.j6_app_android_demo.activities.home_page

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.EmployeeAdapter
import com.siemens.j6_app_android_demo.models.Employee
import com.siemens.j6_app_android_demo.service.EmployeesCallback
import com.siemens.j6_app_android_demo.service.WorkOrderService

class SelectEmployeeActivity : AppCompatActivity(), EmployeesCallback {

    private var employeeListImported: ArrayList<Employee>? = null
    private var employeeList = ArrayList<Employee>()
    private val dAdapter = EmployeeAdapter(employeeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_employee)

        employeeListImported = intent.getSerializableExtra("employee_list") as ArrayList<Employee>?

        /*var employeeList = arrayListOf(
            Employee("Jennie Lin", "Engineer", "Department A", "engineer@company.com"),
            Employee("Devin Lin", "Boss", "Department B", "boss@company.com"),
            Employee("Cathy Lin", "Engineer", "Department C", "engineer@company.com"),
            Employee("Jerome Lin", "Intern", "Department A", "intern@company.com"),
            Employee("Clifford Lin", "Janitor", "Department B", "janitor@company.com"),
        )*/
        WorkOrderService.employeesListener = this
        WorkOrderService.fetchEmployeesData()

        val daRecyclerView: RecyclerView = findViewById(R.id.employee_recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter
        dAdapter.notifyDataSetChanged()

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val empsSelected = ArrayList<Employee>()
            for (emp in employeeList) {
                if (emp.isSelected) {
                    empsSelected.add(emp)
                }
            }
            val resultIntent = Intent()
            resultIntent.putExtra("emps", empsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val empsSelected: ArrayList<Employee>? = null
            val resultIntent = Intent()
            resultIntent.putExtra("emps", empsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onEmployeesResult(result: List<Employee>) {
        employeeList.addAll(result)
        employeeListImported?.let {
            for (empImp in employeeListImported!!)
                for (emp in employeeList)
                    if (empImp.id == emp.id)
                        emp.isSelected = true
        }
        dAdapter.notifyDataSetChanged()
    }
}