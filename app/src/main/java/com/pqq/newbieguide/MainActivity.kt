package com.pqq.newbieguide

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.pqq.lib.HellowData
import com.pqq.lib.widget.GuideView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)
        var guideView: GuideView = findViewById(R.id.guide_view)
        var tv: TextView = findViewById(R.id.tv)

        var hellowData: HellowData = HellowData()
        hellowData.hellowView = tv
        guideView.setHellowDatas(arrayOf(hellowData))
    }

}