package com.example.car

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarsFragment
import com.example.car.Cars.MyCarsFragment
import com.example.car.Models.UserResponse
import com.example.car.Shop.ProductsFragment
import com.example.car.Shop.ShopFragment
import com.example.car.UserActivities.LoginActivity
import com.example.car.UserActivities.ProfileFragment
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout
    lateinit var navview : NavigationView
    lateinit var textView_register: TextView
    lateinit var sp: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // textView_register = findViewById(R.id.Actibity)
        drawerLayout = findViewById(R.id.drawerLayout)
        navview = findViewById(R.id.navview)
        sp = getSharedPreferences("login",MODE_PRIVATE);


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navview.setNavigationItemSelectedListener {
            it.isChecked = true
            when(it.itemId){
                R.id.profile -> replaceFragment(ProfileFragment(), it.title.toString())
                R.id.shop -> replaceFragment(ProductsFragment(), it.title.toString())
                R.id.logoutbtn -> logout()
                R.id.myproducts -> replaceFragment(MyCarsFragment(), it.title.toString())
               // R.id.desactivatebtn -> DesactivateUser()
            }
            true
        }
        updateUserProfileInNavHeader()
    }

    private fun DesactivateUser() {
        val intentlogin = Intent(this, LoginActivity::class.java)
        val token = getSavedToken()
        println("token aze :")
        println(token)
        val call = RetrofitClient.instance.desactivateUser("Bearer $token")

        call.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    //startActivity(intentlogin)
                    Toast.makeText(applicationContext, "test" , Toast.LENGTH_LONG).show()

                }
                if (response.code() == 500) {
                    Toast.makeText(applicationContext, "error" , Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
            }
        })
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

    private fun logout() {
        sp.edit().putBoolean("logged",false).apply();
        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun getSavedToken(): String? {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    private fun updateUserProfileInNavHeader() {
        val token = getSavedToken() // implement this function to get the saved token
        val call = RetrofitClient.instance.getUser("Bearer $token")

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val user = response.body()?.user

                    // show user info in nav_header
                    val headerView = navview.getHeaderView(0)
                    val usernameTextView = headerView.findViewById<TextView>(R.id.username)
                    val emailTextView = headerView.findViewById<TextView>(R.id.email)
                  //  val userImageView = headerView.findViewById<ImageView>(R.id.userimage)

                    usernameTextView.text = user?.username
                    emailTextView.text = user?.email
                    println("userImageView")
                    //println(userImageView)
                  //  Glide.with(this@MainActivity).load(user?.userimage).placeholder(R.drawable.ic_google).into(userImageView)
                   // _status.value = "   First Mars image URL : ${_photos.value!!.imgSrcUrl}"
                    println(user?.userimage)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "Failed to get user info: ${t.message}")
            }
        })
    }

}