package com.example.car.UserActivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.car.Api.RetrofitClient
import com.example.car.MainActivity
import com.example.car.Models.UserResponse
import com.example.car.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserActivity : AppCompatActivity() {

    lateinit var emailinput : EditText
    lateinit var passwordinput : EditText
    lateinit var btn_update: Button
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        emailinput = findViewById(R.id.email)
        passwordinput = findViewById(R.id.password)
        btn_update = findViewById(R.id.updatebutton)

        btn_update.setOnClickListener {
            checkcredentials();
        }
    }

    private fun checkcredentials() {
        val intentProfile = Intent(this, ProfileFragment::class.java)
        val email = emailinput.text.toString().trim()
        val password = passwordinput.text.toString().trim()
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if(email.isEmpty()){
            emailinput.error = "Email Required"
        }

        if(password.isEmpty()) {
            passwordinput.error = "Password Required"
        }

        else {
            RetrofitClient.instance.updateuser("Bearer $token",email,password)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                        {
                            startActivity(intentProfile)
                            //Toast.makeText(applicationContext, "new user", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@UpdateUserActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("User Updated Successfully")
                                .show();
                        }
                        else if(response.code()==500)
                        {
                            //Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@UpdateUserActivity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("An Error Has Occured")
                                //.setContentText("You Submitted The Wrong Code!")
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