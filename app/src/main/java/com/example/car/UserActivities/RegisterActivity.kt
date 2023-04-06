package com.example.car.UserActivities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    lateinit var logintextview : TextView
    lateinit var usernameinput : EditText
    lateinit var emailinput : EditText
    lateinit var passwordinput : EditText
    lateinit var roleinput : EditText
    lateinit var btnregister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        logintextview = findViewById(R.id.logintextview)
        usernameinput = findViewById(R.id.username)
        emailinput = findViewById(R.id.email)
        passwordinput = findViewById(R.id.password)
        roleinput = findViewById(R.id.role)
        btnregister = findViewById(R.id.registerbutton)

        btnregister.setOnClickListener{
            checkcredentials();
        }

        logintextview.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkcredentials() {
        val intentlogin = Intent(this, LoginActivity::class.java)
        val username = usernameinput.text.toString().trim()
        val email = emailinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()
        val role = roleinput.text.toString().trim()

        if(username.isEmpty()){
            usernameinput.error = "Username Required"
        }
        //if(email.matches(Patterns.EMAIL_ADDRESS.toRegex())) {
        if(email.isEmpty()) {
            emailinput.error = "Email Required"
        }
        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }
        if(role.isEmpty()) {
            roleinput.error = "Role Required"
        }
        else {
            RetrofitClient.instance.Register(username,password,email,role, image = "@drawable/ic_google")
                .enqueue(object: Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                        {
                            startActivity(intentlogin)
                            /*SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Welcome To CarNote")
                                .show();  */                      }
                        else if(response.code()==403)
                            //Toast.makeText(applicationContext, "user exists", Toast.LENGTH_LONG).show()

                            SweetAlertDialog(this@RegisterActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("User Exists Already !")
                                .setContentText("Another User Is Already Using This Username")
                                .setConfirmText("OK!")
                                .setConfirmClickListener { sDialog -> sDialog.dismissWithAnimation() }
                                .show()
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }

    }
}