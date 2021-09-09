package com.siemens.j6_app_android_demo.activities.home_page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.View
import android.widget.*
import androidx.core.view.iterator
import androidx.core.widget.doOnTextChanged
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.models.Equipment
import com.siemens.j6_app_android_demo.service.EquipmentsCallback
import com.siemens.j6_app_android_demo.service.WorkOrderService

class AddEquipmentActivity : AppCompatActivity(), EquipmentsCallback {

    var equipmentToAdd: Equipment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_equipment)

        WorkOrderService.equipmentsListener = this
        WorkOrderService.fetchEquipmentsData()

        findViewById<ImageView>(R.id.select_condition_btn).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.condition_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.VISIBLE
            TransitionManager.beginDelayedTransition(findViewById(R.id.condition_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.condition_selection).visibility = View.VISIBLE
        }
        findViewById<ImageView>(R.id.close_condition_selection).setOnClickListener {
            val transitionBottom = Slide()
            transitionBottom.duration = 300
            transitionBottom.addTarget(R.id.condition_selection)

            findViewById<View>(R.id.dark_shade_overlay).visibility = View.GONE
            TransitionManager.beginDelayedTransition(findViewById(R.id.condition_selection), transitionBottom)
            findViewById<LinearLayout>(R.id.condition_selection).visibility = View.GONE
        }
        for (con in findViewById<LinearLayout>(R.id.condition_selection)) {
            con.setOnClickListener {
                if(con is TextView) {
                    findViewById<TextView>(R.id.condition).text = con.text
                    con.setTextColor(getColor(R.color.j6_light_orange))
                }
            }
        }

        findViewById<ImageView>(R.id.back).setOnClickListener {
            val nullEquip: Equipment? = null
            val resultIntent = Intent()
            //resultIntent.putExtra("equip", Equipment(eid, description, downtime, condition, (0..9999).random(), ""))
            resultIntent.putExtra("equip", nullEquip)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        findViewById<TextView>(R.id.save_btn).setOnClickListener {

            val eid = findViewById<AutoCompleteTextView>(R.id.equip_id).text.toString()
            val description = findViewById<EditText>(R.id.description).text.toString()
            val downtime = findViewById<EditText>(R.id.downtime).text.toString()
            val condition = findViewById<TextView>(R.id.condition).text.toString()

            val resultIntent = Intent()
            //resultIntent.putExtra("equip", Equipment(eid, description, downtime, condition, (0..9999).random(), ""))
            resultIntent.putExtra("equip", equipmentToAdd)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onEquipmentsResult(result: List<Equipment>) {
        val equipEidList = ArrayList<String>()
        for (equipment in result)
            equipEidList.add(equipment.eid)

        val equip_actv = findViewById<AutoCompleteTextView>(R.id.equip_id)
        equip_actv.threshold = 0
        equip_actv.setDropDownBackgroundResource(R.color.color_basic_900)
        equip_actv.setAdapter(ArrayAdapter<String>(this, android.R.layout.select_dialog_item, equipEidList))
        equip_actv.doOnTextChanged { text, start, before, count ->
            for (equipment in result) {
                if (equipment.eid == text.toString()) {
                    equipmentToAdd = equipment
                    findViewById<EditText>(R.id.description).setText(equipment.description)
                    findViewById<EditText>(R.id.downtime).setText(equipment.downtime)
                    findViewById<TextView>(R.id.condition).text = equipment.condition
                }
            }
        }
    }
}