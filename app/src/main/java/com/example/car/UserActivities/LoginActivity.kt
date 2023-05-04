package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Cars.CarFragment
import com.example.car.MainActivity
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var forgotpassword : TextView
    lateinit var usernameinput : EditText
    lateinit var passwordinput : EditText
    lateinit var btn_login: Button
    lateinit var textView_register: TextView
    lateinit var sp: SharedPreferences
    lateinit var usertoken: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        forgotpassword = findViewById(R.id.forgotpassword)
        usernameinput = findViewById(R.id.username)
        passwordinput = findViewById(R.id.password)
        btn_login = findViewById(R.id.loginbutton)
        textView_register = findViewById(R.id.registerbtn)
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("logged",false)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            checkcredentials();
            sp.edit().putBoolean("logged",true).apply();
        }

        forgotpassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword1Activity::class.java)
            startActivity(intent)
        }

        textView_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    fun saveToken(token: String?) {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getSavedToken(): String? {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }


    private fun checkcredentials() {
        val intentProfile = Intent(this, MainActivity::class.java)
        val username = usernameinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()

        if(username.isEmpty()){
            usernameinput.error = "Username Required"
        }

        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }

        else {
            RetrofitClient.instance.Login(username,password)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                        {
                            val token = response.body()?.token
                            saveToken(token)
                            startActivity(intentProfile)
                            //Toast.makeText(applicationContext, "new user", Toast.LENGTH_LONG).show()
                        }
                        else if(response.code()==403)
                        {
                            Toast.makeText(applicationContext, "user not exists", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("User Does Not E  xists")
                                //.setContentText("Check Your Mail For The Verification Mail")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()

                        }
                        else if(response.code()==404)
                        {
                            //Toast.makeText(applicationContext, "user desactivated", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Wrong Desactivated")
                                //.setContentText("Check Your Mail For The Verification Mail")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()

                        }
                        else if(response.code()==402)
                        {
                            //Toast.makeText(applicationContext, "wrong password", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Wrong Password")
                                //.setContentText("Check Your Mail For The Verification Mail")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()

                        }
                        else if(response.code()==401)
                        {
                            //Toast.makeText(applicationContext, "not verified", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("User Not Verified")
                                .setContentText("Check Your Mail For The Verification Mail")
                                .setConfirmText("OK")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()

                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                    }

                })

        }

    }
}