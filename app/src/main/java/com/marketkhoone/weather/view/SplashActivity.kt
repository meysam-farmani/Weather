package com.marketkhoone.weather.view

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.marketkhoone.weather.R
import com.marketkhoone.weather.model.entity.DayMode
import kotlinx.android.synthetic.main.activity_main.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}