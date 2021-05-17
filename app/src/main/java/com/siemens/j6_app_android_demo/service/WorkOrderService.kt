package com.siemens.j6_app_android_demo.service

import android.util.Log
import com.siemens.j6_app_android_demo.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface EmployeesCallback {
    fun onEmployeesResult(result: List<Employee>)
}

interface MaterialsCallback {
    fun onMaterialsResult(result: List<Material>)
}

interface LocationsCallback {
    fun onLocationsResult(result: List<Location>)
}

interface TenantsCallback {
    fun onTenantsResult(result: List<Tenant>)
}

interface EquipmentsCallback {
    fun onEquipmentsResult(result: List<Equipment>)
}

interface WorkOrdersCallback {
    fun onWorkOrdersResult(result: List<WorkOrder>)
}

interface CategoriesCallback {
    fun onCategoriesResult(result: List<String>)
}

interface SystemZonesCallback {
    fun onSystemZonesResult(result: List<SystemZone>)
}

interface WorkOrderAPI {
    @GET("/employee")
    fun getEmployees(): Call<List<Employee>>
    @GET("/material")
    fun getMaterials(): Call<List<Material>>
    @GET("/location")
    fun getLocations(): Call<List<Location>>
    @GET("/tenant")
    fun getTenants(): Call<List<Tenant>>
    @GET("/equipment")
    fun getEquipments(): Call<List<Equipment>>
    @GET("/workorder")
    fun getWorkOrders(): Call<List<WorkOrder>>
    @GET("/category")
    fun getCategories(): Call<List<String>>
    @GET("/systemzone")
    fun getSystemZones(): Call<List<SystemZone>>
}

object WorkOrderService {

    private val BASE_URL = "https://j6-mis-dev.azurewebsites.net"
    private val retrofit: Retrofit
    private val service: WorkOrderAPI

    // listeners
    var employeesListener: EmployeesCallback? = null
    var materialsListener: MaterialsCallback? = null
    var locationsListener: LocationsCallback? = null
    var tenantsListener: TenantsCallback? = null
    var equipmentsListener: EquipmentsCallback? = null
    var workOrdersListener: WorkOrdersCallback? = null
    var categoriesListener: CategoriesCallback? = null
    var systemZonesListener: SystemZonesCallback? = null

    val TAG = "AsiaSiemens2021"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(WorkOrderAPI::class.java)
    }

    fun fetchEmployeesData() {
        val call = service.getEmployees()
        call.enqueue(object : Callback<List<Employee>> {
            override fun onResponse(call: Call<List<Employee>>, response: Response<List<Employee>>) {
                if (response.code() == 200) {
                    employeesListener?.onEmployeesResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: Failure")
            }
        })
    }

    fun fetchMaterialsData() {
        val call = service.getMaterials()
        call.enqueue(object : Callback<List<Material>> {
            override fun onResponse(call: Call<List<Material>>, response: Response<List<Material>>) {
                if (response.code() == 200) {
                    materialsListener?.onMaterialsResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Material>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: Failure")
            }
        })
    }

    fun fetchLocationsData() {
        val call = service.getLocations()
        call.enqueue(object : Callback<List<Location>> {
            override fun onResponse(call: Call<List<Location>>, response: Response<List<Location>>) {
                if (response.code() == 200) {
                    locationsListener?.onLocationsResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }

    fun fetchTenantsData() {
        val call = service.getTenants()
        call.enqueue(object : Callback<List<Tenant>> {
            override fun onResponse(call: Call<List<Tenant>>, response: Response<List<Tenant>>) {
                if (response.code() == 200) {
                    tenantsListener?.onTenantsResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Tenant>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }

    fun fetchEquipmentsData() {
        val call = service.getEquipments()
        call.enqueue(object : Callback<List<Equipment>> {
            override fun onResponse(call: Call<List<Equipment>>, response: Response<List<Equipment>>) {
                if (response.code() == 200) {
                    equipmentsListener?.onEquipmentsResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<Equipment>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }

    fun fetchWorkOrdersData() {
        val call = service.getWorkOrders()
        call.enqueue(object : Callback<List<WorkOrder>> {
            override fun onResponse(call: Call<List<WorkOrder>>, response: Response<List<WorkOrder>>) {
                if (response.code() == 200) {
                    workOrdersListener?.onWorkOrdersResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<WorkOrder>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }

    fun fetchCategoriesData() {
        val call = service.getCategories()
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.code() == 200) {
                    categoriesListener?.onCategoriesResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }

    fun fetchSystemZonesData() {
        val call = service.getSystemZones()
        call.enqueue(object : Callback<List<SystemZone>> {
            override fun onResponse(call: Call<List<SystemZone>>, response: Response<List<SystemZone>>) {
                if (response.code() == 200) {
                    systemZonesListener?.onSystemZonesResult(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<SystemZone>>, t: Throwable) {
                Log.d("AsiaSiemens2021", "onFailure: " + t.message)
            }
        })
    }
}