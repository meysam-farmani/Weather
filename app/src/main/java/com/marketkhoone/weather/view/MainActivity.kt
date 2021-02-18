package com.marketkhoone.weather.view

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


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeStatusBarColor(DayMode.Day)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when{
                menuItem.itemId == R.id.menu_home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.menu_forecast -> {
                    loadFragment(ForecastFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                menuItem.itemId == R.id.menu_explore -> {
                    loadFragment(ExploreFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        bottomNavigationView.setOnNavigationItemReselectedListener {

        }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().also { fragmentTransaction ->
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.commit()
        }

    }

    fun performExploreClick() {
        val view: View = bottomNavigationView.findViewById(R.id.menu_explore)
        view.performClick()
    }

    fun changeStatusBarColor(dayMode: DayMode) {

        var resourseColor = R.color.colorDay
        if(dayMode.equals(DayMode.Night)){
            resourseColor = R.color.colorNight
            window.decorView.systemUiVisibility = 0
        }else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, resourseColor)
        }
        val bar: ActionBar? = supportActionBar
        bar?.setBackgroundDrawable(ColorDrawable(resourseColor))
    }
}