package com.siemens.j6_app_android_demo.activities.home_page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siemens.j6_app_android_demo.R
import com.siemens.j6_app_android_demo.adapters.MaterialToBePickedAdapter
import com.siemens.j6_app_android_demo.models.Material

class SelectMaterialActivity : AppCompatActivity() {

    private val materialsToBePicked = ArrayList<Material>()
    lateinit var daRecyclerView: RecyclerView
    val dAdapter = MaterialToBePickedAdapter(materialsToBePicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_material)

        val matsImported = intent.getSerializableExtra("material_list") as ArrayList<Material>
        if (matsImported.isNotEmpty()) {
            materialsToBePicked.clear()
            materialsToBePicked.addAll(matsImported)
            dAdapter.notifyDataSetChanged()
        }

        daRecyclerView = findViewById(R.id.material_to_be_picked_recycler_view)
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        daRecyclerView.layoutManager = layoutManager
        daRecyclerView.itemAnimator = DefaultItemAnimator()
        daRecyclerView.adapter = dAdapter

        findViewById<ImageView>(R.id.add_new_material).setOnClickListener {
            addNewMaterial.launch(Intent(this, AddNewMaterialActivity::class.java))
        }
        findViewById<TextView>(R.id.save_btn).setOnClickListener {
            val materialsToBeReturned = ArrayList<Material>()
            if (isSavable()) {
                for (mat in materialsToBePicked) {
                    if (mat.isSelected) {
                        materialsToBeReturned.add(mat)
                    }
                }
                val resultIntent = Intent()
                resultIntent.putExtra("mat", materialsToBeReturned)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please select a material", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            val resultIntent = Intent()
            val nullMat: ArrayList<Material>? = null
            resultIntent.putExtra("mat", nullMat)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun isSavable(): Boolean {
        for (mat in materialsToBePicked) {
            if (mat.isSelected) return true
        }
        return false
    }

    fun updateButtonColor() {
        for (mat in materialsToBePicked) {
            if (mat.isSelected) {
                findViewById<TextView>(R.id.save_btn).setBackgroundResource(R.drawable.shape_orange)
                return
            }
        }
        findViewById<TextView>(R.id.save_btn).setBackgroundResource(if (isSavable()) R.drawable.shape_orange else R.drawable.shape_gray_less_round)
    }

    private val addNewMaterial = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mats = result.data!!.getSerializableExtra("available_mats_added") as ArrayList<Material>?
//            materialsToBePicked.clear()
//            materialsToBePicked.addAll(mats)
            mats?.let {
                for (matToBeAdded in mats) {
                    var alreadyAdded = false
                    for (mat in materialsToBePicked) {
                        if (mat.id == matToBeAdded.id) {
                            mat.qty = matToBeAdded.qty
                            alreadyAdded = true
                            break
                        }
                    }
                    if (!alreadyAdded)
                        materialsToBePicked.add(matToBeAdded)
                }
                dAdapter.notifyDataSetChanged()
            }
        }
    }
}