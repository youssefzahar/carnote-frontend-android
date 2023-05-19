package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPassword1Activity : AppCompatActivity() {

  /*  lateinit var usernameinput : EditText
    lateinit var btn_next: Button
    lateinit var btn_cancel: Button
*/
    lateinit var btn_next : Button
    lateinit var btn_cancel: Button
    lateinit var usernameinput: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password1)
        usernameinput = findViewById(R.id.username)
        btn_cancel = findViewById(R.id.cancelbutton)
        btn_next = findViewById(R.id.nextbutton)

        btn_next.setOnClickListener{
            checkcredentials();

        }

        btn_cancel.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun saveUsernameforogotpassword(token: String?) {
        val sharedPreferences = getSharedPreferences("usernameforogotpassword", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", token)
        editor.apply()
    }

    private fun checkcredentials() {
        val intentforgotpasswor2 = Intent(this, ForgotPassword2Activity::class.java)
        val username = usernameinput.text.toString().trim()

        if(username.isEmpty()){
            usernameinput.error = "Username Required"
        }
        else {
            RetrofitClient.instance.sendOtp(username)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                            saveUsernameforogotpassword(username)
                        startActivity(intentforgotpasswor2)
                        if(response.code() == 500)
                           // Toast.makeText(applicationContext, "user doesn't exist" , Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@ForgotPassword1Activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("User Doesn' Exist !")
                               // .setContentText("You Submitted The Wrong Code!")
                                .show()
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }

    }
}