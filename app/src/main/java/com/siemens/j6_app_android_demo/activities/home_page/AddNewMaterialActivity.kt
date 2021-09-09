package com.siemens.j6_app_android_demo.activities.home_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.AddMaterialAdapter
import com.siemens.j6_app_android_demo.models.Material
import com.siemens.j6_app_android_demo.service.MaterialsCallback
import com.siemens.j6_app_android_demo.service.WorkOrderService

class AddNewMaterialActivity : AppCompatActivity(), MaterialsCallback {

    private var materialList = ArrayList<Material>()
    val dAdapter = AddMaterialAdapter(materialList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_material)

        /*val materialList = arrayListOf(
            Material("Material 1", "Code 1", "20", "Loc 1"),
            Material("Material 2", "Code 2", "20", "Loc 2"),
            Material("Material 3", "Code 3", "20", "Loc 3"),
            Material("Material 4", "Code 4", "20", "Loc 4"),
            Material("Material 5", "Code 5", "20", "Loc 5"),
        )*/
        WorkOrderService.materialsListener = this
        WorkOrderService.fetchMaterialsData()

        val daRecyclerView: RecyclerView = findViewById(R.id.material_recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter
        dAdapter.notifyDataSetChanged()

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val matsSelected = ArrayList<Material>()
            for (mat in materialList) {
                if (mat.qty > 0) {
                    matsSelected.add(mat)
                }
            }
            val resultIntent = Intent()
            resultIntent.putExtra("available_mats_added", matsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val matsSelected: ArrayList<Material>? = null
            val resultIntent = Intent()
            resultIntent.putExtra("available_mats_added", matsSelected)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onMaterialsResult(result: List<Material>) {
        materialList.addAll(result)
        dAdapter.notifyDataSetChanged()
    }
}