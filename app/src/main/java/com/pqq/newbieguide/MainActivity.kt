package com.pqq.newbieguide

import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.pqq.lib.GuideDialog
import com.pqq.lib.HellowData
import com.pqq.lib.widget.GuideView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        var tv: TextView = findViewById(R.id.tv)
//        var guideView: GuideView = findViewById(R.id.guide_view)

        var hellowData: HellowData = HellowData()
        hellowData.hellowView = tv

        var guideView: GuideView = GuideView(this)
        guideView.setHellowDatas(arrayOf(hellowData))

        var tipView: View =
            LayoutInflater.from(this).inflate(R.layout.layout_test_tip, null, false);

        var builder: GuideDialog.Builder = GuideDialog.Builder()
        builder.buildGuideView(guideView)
            .buildOperateView(tipView)
            .build()
            .show(supportFragmentManager, "GuideDialog")


    }

}