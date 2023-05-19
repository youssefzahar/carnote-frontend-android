package com.example.car.UserActivities

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

class ForgotPassword2Activity : AppCompatActivity() {

    lateinit var btn_next : Button
    lateinit var btn_cancel : Button
    lateinit var otp_1 : EditText
    lateinit var otp_2 : EditText
    lateinit var otp_3 : EditText
    lateinit var otp_4 : EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password2)

        otp_1 = findViewById(R.id.otp1)
        otp_2 = findViewById(R.id.otp2)
        otp_3 = findViewById(R.id.otp3)
        otp_4 = findViewById(R.id.otp4)
        btn_next = findViewById(R.id.nextbutton)
        btn_cancel = findViewById(R.id.cancelbutton)

        btn_next.setOnClickListener{
            checkcredentials()
        }

        btn_cancel.setOnClickListener{
            val intent = Intent(this,ForgotPassword1Activity::class.java)
            startActivity(intent)
        }
    }

    private fun checkcredentials() {
        val intentforgotpassword3 = Intent(this, ForgotPassword3Activity::class.java)
        val otp1 = otp_1.text.toString().trim()
        val otp2 = otp_2.text.toString().trim()
        val otp3 = otp_3.text.toString().trim()
        val otp4 = otp_4.text.toString().trim()


        if(otp1.isEmpty()){
            otp_1.error = ""
        }
        if(otp2.isEmpty()){
            otp_2.error = ""
        }
        if(otp3.isEmpty()){
            otp_3.error = ""
        }
        if(otp4.isEmpty()){
            otp_4.error = ""
        }
        else {
            val otp = "$otp1$otp2$otp3$otp4"
            RetrofitClient.instance.verifyOTP(otp)
                .enqueue(object: Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.code() == 200)
                            startActivity(intentforgotpassword3)
                        if(response.code() == 500)
                            //Toast.makeText(applicationContext, "wrong otp" , Toast.LENGTH_LONG).show()
                            SweetAlertDialog(this@ForgotPassword2Activity, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Wrong Otp")
                                 .setContentText("You Submitted The Wrong Code!")
                                .show()
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message , Toast.LENGTH_LONG).show()
                    }

                })
        }

    }
}