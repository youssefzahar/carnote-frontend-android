package com.example.car

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.car.Cars.CarsFragment
import com.example.car.Shop.ShopFragment
import com.example.car.UserActivities.ProfileFragment
import com.example.car.UserActivities.RegisterActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var navview : NavigationView
    lateinit var textView_register: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // textView_register = findViewById(R.id.Actibity)
        drawerLayout = findViewById(R.id.drawerLayout)
        navview = findViewById(R.id.navview)


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navview.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.profile -> replaceFragment(ProfileFragment(), it.title.toString())
                R.id.cars -> replaceFragment(CarsFragment(), it.title.toString())
                R.id.shop -> replaceFragment(ShopFragment(), it.title.toString())
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment, title: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}