package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword3Activity : AppCompatActivity() {

    lateinit var btn_next : Button
    lateinit var btn_cancel : Button
    lateinit var passwordinput : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password3)

        passwordinput = findViewById(R.id.password)
        btn_next = findViewById(R.id.nextbutton)
        btn_cancel = findViewById(R.id.cancelbutton)


        btn_next.setOnClickListener{
            checkcredentials()
        }

        btn_cancel.setOnClickListener{
            val intent = Intent(this,ForgotPassword2Activity::class.java)
            startActivity(intent)
        }
    }


    fun getUsernameforogotpassword(): String? {
        val sharedPreferences = getSharedPreferences("usernameforogotpassword", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun checkcredentials() {
        val intentlogin = Intent(this, LoginActivity::class.java)
        val password = passwordinput.text.toString().trim()


        if(password.isEmpty()){
            passwordinput.error = "New Password"
        }
        else {
            val username = getUsernameforogotpassword() // implement this function to get the saved token
            startActivity(intentlogin)
            if (username != null) {
                RetrofitClient.instance.resetPassword(username, password)
                    .enqueue(object: Callback<UserResponse> {
                        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                            if(response.code() == 200)
                                startActivity(intentlogin)
                            if(response.code() == 500)
                                //Toast.makeText(applicationContext, "error" , Toast.LENGTH_LONG).show()
                                SweetAlertDialog(this@ForgotPassword3Activity, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("An Error Has Occured")
                                    //.setContentText("You Submitted The Wrong Code!")
                                    .show()
                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                        }

                    })
            }
        }
    }
}