package com.siemens.j6_app_android_demo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.security.AccessController.getContext

class HomePageActivity : AppCompatActivity() {

    private lateinit var allFeatHoriz: HorizontalScrollView
    private lateinit var allFeatExp: LinearLayout
    private var allFeatHorizHeight: Int = 0
    private var allFeatExpHeight: Int = 0

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
    }

    fun expandOrCollapseAllFeatures(view: View) {
        Toast.makeText(this, "hh: $allFeatHorizHeight, eh: $allFeatExpHeight", Toast.LENGTH_SHORT).show()
        val textView = findViewById<TextView>(R.id.see_all_pack_up_text)
        if (textView.text == getString(R.string.see_all)) {
            allFeatHoriz.layoutParams = LinearLayout.LayoutParams(allFeatHoriz.width, 0)
            allFeatExp.layoutParams = LinearLayout.LayoutParams(allFeatExp.width, allFeatExpHeight)
            textView.text = getText(R.string.pack_up)
            findViewById<ImageView>(R.id.see_all_pack_up_img).setImageResource(R.drawable.orange_homepage_pack_up_upward_arrow)
        } else {
            allFeatHoriz.layoutParams = LinearLayout.LayoutParams(allFeatHoriz.width, allFeatHorizHeight)
            allFeatExp.layoutParams = LinearLayout.LayoutParams(allFeatExp.width, 0)
            textView.text = getString(R.string.see_all)
            findViewById<ImageView>(R.id.see_all_pack_up_img).setImageResource(R.drawable.orange_homepage_see_all_downward_arrow)
        }
    }
}