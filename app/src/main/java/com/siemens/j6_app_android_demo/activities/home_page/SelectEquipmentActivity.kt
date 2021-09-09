package com.siemens.j6_app_android_demo.activities.home_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.EquipmentAdapter
import com.siemens.j6_app_android_demo.models.Equipment

class SelectEquipmentActivity : AppCompatActivity() {

    private val equipmentList = ArrayList<Equipment>()
    lateinit var daRecyclerView: RecyclerView
    val dAdapter: EquipmentAdapter = EquipmentAdapter(equipmentList)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_equipment)

        val equipmentsImported = intent.getSerializableExtra("equipment_list") as ArrayList<Equipment>
        if (equipmentsImported.isNotEmpty()) {
            equipmentList.clear()
            equipmentList.addAll(equipmentsImported)
            dAdapter.notifyDataSetChanged()
        }

        daRecyclerView = findViewById(R.id.equipment_recyclerview)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter

        findViewById<ImageView>(R.id.add_new_equipment).setOnClickListener {
            addEquipment.launch(Intent(this, AddEquipmentActivity::class.java))
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("equip_list", equipmentList)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val resultIntent = Intent()
            val nullEqLst: ArrayList<Equipment>? = null
            resultIntent.putExtra("equip_list", nullEqLst)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
    private val addEquipment = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val equipToAdd = result.data!!.getSerializableExtra("equip") as Equipment?
            equipToAdd?.let {
                var alreadyAdded = false
                for (equip in equipmentList)
                    if (equip.eid == equipToAdd.eid)
                        alreadyAdded = true
                if (!alreadyAdded) {
                    equipmentList.add(equipToAdd)
                    dAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}